package com.example.michaelwaterworth.r_kit;

/**
 * Created by michaelwaterworth on 03/09/15. Copyright Michael Waterworth
 */
/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//        import com.example.android.testing.blueprint.HelloTestingBlueprintActivity;
//        import com.example.android.testing.blueprint.R;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {

    @Rule
    public ActivityTestRule<BioviciReaderTask> mActivityRule =
            new ActivityTestRule<>(BioviciReaderTask.class);

    @Test
    public void clickButtonBluetoothOff() {
//        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        BluetoothAdapter.getDefaultAdapter().disable();
//        // Find Button and Click on it
//
//        onView(withId(R.id.biovici_page1_next)).perform(click());

        // Find TextView and verify the correct text that is displayed

//        matches(withText(
//                mActivityRule.getActivity().getString(R.string.task_biovici_calibrate_subtitle)))
    }
}