/*
 * Copyright (C) 2014 The Android Open Source Project
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

package android.sample.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.lang.Override;

/**
 * A simple activity which logs to Logcat.
 */
public class SampleDeviceActivity extends Activity {

    private static final String TAG = SampleDeviceActivity.class.getSimpleName();

    /**
     * The test string to log.
     */
    private static final String TEST_STRING = "SampleTestString";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Log the test string to Logcat.
        Log.i(TAG, TEST_STRING);
    }

}
