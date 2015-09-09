/*
 * Copyright (C) 2015 The Android Open Source Project
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
package android.assist.testapp;

import android.assist.common.Utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.Override;

public class ScreenshotActivity extends Activity {
    static final String TAG = "ScreenshotActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "ScreenshotActivity created");
        setContentView(R.layout.screenshot_activity);
    }

    @Override
    public void onResume() {
        Log.i(TAG, " in onResume");
        super.onResume();
        int backgroundColor = getIntent().getIntExtra(Utils.SCREENSHOT_COLOR_KEY, Color.WHITE);
        View view = findViewById(R.id.screenshot_activity);
        view.setBackgroundColor(backgroundColor);
        view.requestLayout();

        // Tell service activity is in foreground.
        Intent intent = new Intent(Utils.SCREENSHOT_HASRESUMED);
        sendBroadcast(intent);
        Log.i(TAG, "Resumed broadcast sent.");
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        finish();
        super.onPause();
    }
}
