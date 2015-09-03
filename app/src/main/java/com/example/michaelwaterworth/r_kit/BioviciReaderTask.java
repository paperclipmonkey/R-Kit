package com.example.michaelwaterworth.r_kit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by michaelwaterworth on 25/08/15. Copyright Michael Waterworth
 */
public class BioviciReaderTask extends FlipperActivityTask {
    private static final String TAG = "Biovici Reader";
    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 3;
    // Well known SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String deviceAddress;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private InputStream inStream = null;
    private OutputStream outStream = null;
    private Thread workerThread;
    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;
    private boolean isCalibrating;
    private boolean hasCalibrated;
    private boolean isReading;
    private boolean hasRead;
    private CountDownTimer attemptConnectionTimeout;
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biovici_reader);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    // The BroadcastReceiver that listens for discovered devices
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

    // The on-click listener for all devices in the listViews
    private final AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            deviceAddress = info.substring(info.length() - 17);
            if(connectToDevice(deviceAddress)) {
                attemptConnectionTimeout = new CountDownTimer(5000, 5000) {
                    public void onTick(long millisUntilFinished) {}

                    public void onFinish() {
                        Toast.makeText(getApplicationContext(), "failed to connect", Toast.LENGTH_LONG).show();
                    }
                }.start();
            } else {
                Toast.makeText(getApplicationContext(), "failed to connect", Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Called when a new intent returns
     * @param requestCode Code we sent with the request
     * @param resultCode Status code for return of request
     * @param data Additional intent object
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    pageNext();
                }
                break;
        }
    }

    /**
     * Overrides pageNext functionality for view flipper object
     */
    @Override
    public void pageNext() {
        flipper.showNext();  // Switches to the next view
        float progress = ((float) flipper.getDisplayedChild() / (float) flipper.getChildCount()) * 100;

        setTaskProgress((int) progress);
        if (flipper.getCurrentView().getId() == R.id.turn_on_bluetooth) {
            //Check if bluetooth is turned on, else display help.
            if(checkBTState()){
                pageNext();
            }
            return;
            //TODO - Show image / display text in text view.
        }
        if (flipper.getCurrentView().getId() == R.id.pair_bluetooth) {
            bluetoothPair();
            return;
        }
        if (flipper.getCurrentView().getId() == R.id.calibrate_device) {
        }
        if (flipper.getCurrentView().getId() == R.id.read_device) {
            //Show spinner
        }
    }

    /**
     * Function called when pressing button defined in XML
     * @param view View that initiated request
     */
    public void buttonTurnOnBluetooth(View view) {
        //Prompt user to turn on Bluetooth
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    /**
     * Check the status of Bluetooth. Used during paging to skip turning on bluetooth page
     */
    private boolean checkBTState() {
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            Toast.makeText(this, "Sorry bluetooth is not supported on your device", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sends the specified string to the remote bluetooth device
     * @param string Data string to send to remote bluetooth device. Should be appended with \n character sequence
     */
    private void sendData(String string){
        if(outStream != null){
            try {
                outStream.write(string.getBytes());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Start the event listening thread to handle incoming data over bluetooth
     */
    private void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = inStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            respondToEvent(data);
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        Log.e(TAG, ex.getMessage());
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    /**
     * Perform actions based on the event string returned by remote device
     * @param string String event name
     */
    private void respondToEvent(String string) {
        Log.d(TAG, "Received message: " + string);
        string = string.replace("\n", "").replace("\r", "");
        switch (string) {
            case "Calibration started":
                eventCalibrationStarted();
                break;
            case "Calibration finished":
                eventCalibrationFinished();
                break;
            case "Reading started":
                eventReadingStarted();
                break;
            case "isBiovici:true":
                eventIsBiovici();
                break;
        }
        if (string.contains("Reading:")) {
            try {
                int reading = Integer.parseInt(string.substring(9));
                eventReadingFinished(reading);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Called when calibration started event sent
     * If on calibrate page sets up spinner
     */
    private void eventCalibrationStarted() {
        if (flipper.getCurrentView().getId() == R.id.calibrate_device && !isCalibrating) {
            Toast.makeText(getApplicationContext(), "Calibrating", Toast.LENGTH_SHORT).show();
            isCalibrating = true;
            ProgressBar calibrateDeviceSpinner = (ProgressBar) findViewById(R.id.calibrate_device_progress);
            calibrateDeviceSpinner.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Called when calibration ended event sent
     * If on calibrate page stops spinner
     */
    private void eventCalibrationFinished() {
        if (flipper.getCurrentView().getId() == R.id.calibrate_device && !hasCalibrated) {
            hasCalibrated = true;
            ProgressBar calibrateDeviceSpinner = (ProgressBar) findViewById(R.id.calibrate_device_progress);
            calibrateDeviceSpinner.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Calibrated", Toast.LENGTH_SHORT).show();
            new CountDownTimer(2000, 2000) {
                public void onTick(long millisUntilFinished) {}

                public void onFinish() {
                    pageNext();
                }
            }.start();
        }
    }

    /**
     * Called when reading started event sent
     * If on reading page sets up spinner
     */
    private void eventReadingStarted() {
        if (flipper.getCurrentView().getId() == R.id.read_device && !isReading) {
            ProgressBar calibrateDeviceSpinner = (ProgressBar) findViewById(R.id.reading_progress);
            calibrateDeviceSpinner.setVisibility(View.VISIBLE);
            isReading = true;
            Toast.makeText(getApplicationContext(), "Reading started", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when reading ended event sent
     * If on reading page stops spinner
     */
    private void eventReadingFinished(int reading) {
        if (flipper.getCurrentView().getId() == R.id.read_device && !hasRead) {
            hasRead = true;
            ProgressBar calibrateDeviceSpinner = (ProgressBar) findViewById(R.id.reading_progress);
            calibrateDeviceSpinner.setVisibility(View.INVISIBLE);
            //Show message that a reading has been taken
            Toast.makeText(getApplicationContext(), "Reading taken", Toast.LENGTH_SHORT).show();
            new CountDownTimer(2000, 2000) {
                public void onTick(long millisUntilFinished) {}

                public void onFinish() {
                    pageNext();
                }
            }.start();
        }
    }

    /**
     * Function called when string confirming device is a Biovici reader is detected
     * Initiates move to next page
     */
    private void eventIsBiovici(){
        if(attemptConnectionTimeout != null){
            attemptConnectionTimeout.cancel();//Stop timeout
            pageNext();
        }
    }

    /**
     * Attempt connection to bluetooth device using address
     * @param adr Address of device to connect to
     * @return Returns true if connection has been established, else false
     */
    private boolean connectToDevice(String adr) {
        super.onResume();

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(adr);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "In onResume() and socket create failed: " + e.getMessage() + ".");
            return false;
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
            return false;
        }

        // Create a data stream so we can talk to server.
        try {
            inStream = btSocket.getInputStream();
            outStream = btSocket.getOutputStream();
            sendData("isBiovici\n");
            beginListenForData();
        } catch (IOException e) {
            Log.e(TAG, "In onResume() and output stream creation failed:" + e.getMessage() + ".");
            return false;
        }
        return true;
    }

    /**
     * Set up page to show bluetooth discovery. Initialise views
     */
    private void bluetoothPair() {
        // Setup the window

        // Initialize the button to perform device discovery
        ImageButton scanButton = (ImageButton) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
            }
        });

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        ArrayAdapter<String> mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }
}
