package com.example.michaelwaterworth.r_kit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.gcacace.signaturepad.views.SignaturePad;

// Instances of this class are fragments representing a single
// object in our collection.
public class DemoObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        Bundle args = getArguments();

        String resourceId = "intro" + args.getInt(ARG_OBJECT);

        int rId = getResources().getIdentifier(resourceId, "layout", "com.example.michaelwaterworth.r_kit");
        Log.d("Rid", "Resource id: "+rId);
        Log.d("Rid", "Passed int: "+args.getInt(ARG_OBJECT));

        View rootView = inflater.inflate(rId, container, false);
//        ((TextView) rootView.findViewById(R.id.textView)).setText(
//                Integer.toString(args.getInt(ARG_OBJECT)));

        if(rootView.findViewById(R.id.signature_pad)!= null){
            attachSignaturePad(rootView);
        }
        return rootView;
    }

    private void attachSignaturePad(View rootView){
        Log.d("Signature", "Attaching signature events");
        SignaturePad mSignaturePad = (SignaturePad) rootView.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onSigned() {
                Log.d("Signature", "Signed");

                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                Log.d("Signature", "Cleared");
                //Event triggered when the pad is cleared
            }
        });
    }
}