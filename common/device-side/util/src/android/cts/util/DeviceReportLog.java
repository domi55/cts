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

package android.cts.util;

import android.cts.util.ReportLog;

import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;

/**
 * Handles adding results to the report for device side tests.
 *
 * NOTE: tests MUST call {@link #submit(Instrumentation)} in the test's tearDown method.
 */
public class DeviceReportLog extends ReportLog {
    private static final String TAG = DeviceReportLog.class.getSimpleName();
    private static final String CTS_RESULT = "CTS_RESULT";
    private static final int INST_STATUS_IN_PROGRESS = 2;

    public void submit(Instrumentation instrumentation) {
        Log.i(TAG, "submit");
        Bundle output = new Bundle();
        output.putSerializable(CTS_RESULT, this);
        instrumentation.sendStatus(INST_STATUS_IN_PROGRESS, output);
    }
}
