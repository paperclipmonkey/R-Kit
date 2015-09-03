package com.example.michaelwaterworth.r_kit;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;

/**
 * Created by michaelwaterworth on 03/09/15. Copyright Michael Waterworth
 */
public class BioviciReaderTaskTest extends ActivityInstrumentationTestCase2<BioviciReaderTask> {
    private ActivityTask mFirstTestActivity;

    public BioviciReaderTaskTest() {
        super(BioviciReaderTask.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();
        setActivityInitialTouchMode(true);
//        mFirstTestText =
//                (TextView) mFirstTestActivity
//                        .findViewById(R.id.);
    }

    @SmallTest
    public void testPreconditions() {
        assertNotNull("mFirstTestActivity is null", mFirstTestActivity);
    }

    @MediumTest
    public void testProgressBluetoothOn() {
        //String expectedInfoText = mClickFunActivity.getString(R.string.info_text);
        TouchUtils.clickView(this, mFirstTestActivity.findViewById(R.id.biovici_page1_next));
        assertTrue(View.VISIBLE == mFirstTestActivity.findViewById(R.id.turn_on_bluetooth).getVisibility());
        //assertEquals(expectedInfoText, mInfoTextView.getText());
    }


    @MediumTest
    public void testProgressBluetoothOff() {
        //String expectedInfoText = mClickFunActivity.getString(R.string.info_text);
        TouchUtils.clickView(this, mFirstTestActivity.findViewById(R.id.biovici_page1_next));
        assertTrue(View.VISIBLE == mFirstTestActivity.findViewById(R.id.turn_on_bluetooth).getVisibility());
        //assertEquals(expectedInfoText, mInfoTextView.getText());
    }

//    public void testMyFirstTestTextView_labelText() {
//        final String expected =
//                mFirstTestActivity.getString(R.string.my_first_test);
//        final String actual = mFirstTestText.getText().toString();
//        assertEquals(expected, actual);
//    }
}
