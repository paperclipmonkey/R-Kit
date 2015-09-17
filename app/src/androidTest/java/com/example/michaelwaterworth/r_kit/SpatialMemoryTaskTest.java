package com.example.michaelwaterworth.r_kit;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by michaelwaterworth on 03/09/15. Copyright Michael Waterworth
 */
public class SpatialMemoryTaskTest extends ActivityInstrumentationTestCase2<SpatialMemoryTask> {
    private SpatialMemoryTask mTestActivity;

    public SpatialMemoryTaskTest() {
        super(SpatialMemoryTask.class);
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
}
