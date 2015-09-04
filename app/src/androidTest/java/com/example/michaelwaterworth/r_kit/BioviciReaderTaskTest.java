package com.example.michaelwaterworth.r_kit;

import android.bluetooth.BluetoothAdapter;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;

/**
 * Created by michaelwaterworth on 03/09/15. Copyright Michael Waterworth
 */
public class BioviciReaderTaskTest extends ActivityInstrumentationTestCase2<BioviciReaderTask> {
    private ActivityTask mTestActivity;

    public BioviciReaderTaskTest() {
        super(BioviciReaderTask.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestActivity = getActivity();
        setActivityInitialTouchMode(true);
    }

    @SmallTest
    public void testPreconditions() {
        assertNotNull("mTestActivity is null", mTestActivity);
    }

    @LargeTest
    public void testProgressBluetoothOn() {
        BluetoothAdapter.getDefaultAdapter().enable();
        // Changing state of bluetooth can take 5 seconds.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TouchUtils.clickView(this, mTestActivity.findViewById(R.id.biovici_page1_next));
        assertTrue(View.VISIBLE == mTestActivity.findViewById(R.id.pair_bluetooth).getVisibility());
        assertTrue(View.GONE == mTestActivity.findViewById(R.id.turn_on_bluetooth).getVisibility());
        //assertEquals(expectedInfoText, mInfoTextView.getText());
    }

    @LargeTest
    public void testProgressBluetoothOff() {
        BluetoothAdapter.getDefaultAdapter().disable();
        // Changing state of bluetooth can take 5 seconds.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TouchUtils.clickView(this, mTestActivity.findViewById(R.id.biovici_page1_next));
        assertTrue(View.GONE == mTestActivity.findViewById(R.id.pair_bluetooth).getVisibility());
        assertTrue(View.VISIBLE == mTestActivity.findViewById(R.id.turn_on_bluetooth).getVisibility());
    }


//    @LargeTest
//    public void testProgressBluetoothOff() {
//        BluetoothAdapter.getDefaultAdapter().disable();
//        //String expectedInfoText = mClickFunActivity.getString(R.string.info_text);
//        TouchUtils.clickView(this, mTestActivity.findViewById(R.id.biovici_page1_next));
//        assertTrue(View.VISIBLE == mTestActivity.findViewById(R.id.biovici_page1_next).getVisibility());
//        //assertEquals(expectedInfoText, mInfoTextView.getText());
//    }

//    public void testMyFirstTestTextView_labelText() {
//        final String expected =
//                mTestActivity.getString(R.string.my_first_test);
//        final String actual = mFirstTestText.getText().toString();
//        assertEquals(expected, actual);
//    }
}
