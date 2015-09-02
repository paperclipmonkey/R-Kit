package com.example.michaelwaterworth.r_kit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by michaelwaterworth on 31/07/15. Copyright Michael Waterworth
 */
public class IntroActivity extends FragmentActivity {
    private final String TAG = "IntroActivity";
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private ViewFlipper flipper;
    private SignaturePad mSignaturePad;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        flipper = (ViewFlipper) findViewById(R.id.intro_switcher);


        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
    }

    public void buttonNext(View view) {
        flipper.showNext();  // Switches to the next view
    }

    public void buttonDone(View v) {
        if (mSignaturePad.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.empty_signature, Toast.LENGTH_SHORT).show();
            return;
        }
        //Save the signature
        storeImage(mSignaturePad.getTransparentSignatureBitmap());
        this.finish();
    }

    private void storeImage(Bitmap image) {
        try {
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm", Locale.UK).format(new Date());

            FileOutputStream fos = openFileOutput(timeStamp + ".png", MODE_PRIVATE);

            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Log.d(TAG, "File saved successfully");
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error accessing file: " + e.getMessage());
        }
    }
}