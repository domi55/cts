/*
 * Copyright 2014 The Android Open Source Project
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

package android.hardware.camera2.cts;

import android.app.Activity;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.android.cts.hardware.R;

public class Camera2SurfaceViewCtsActivity extends Activity implements SurfaceHolder.Callback {
    private final static String TAG = "Camera2SurfaceViewCtsActivity";
    private final ConditionVariable surfaceChangedDone = new ConditionVariable();

    private SurfaceView mSurfaceView;
    private int currentWidth = 0;
    private int currentHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.surface_view_2);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);
    }

    public SurfaceView getSurfaceView() {
        return mSurfaceView;
    }

    public boolean waitForSurfaceSizeChanged(int timeOutMs, int expectWidth, int expectHeight) {
        if (timeOutMs <= 0 || expectWidth <= 0 || expectHeight <= 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "timeout(%d), expectWidth(%d), and expectHeight(%d) " +
                            "should all be positive numbers",
                            timeOutMs, expectWidth, expectHeight));
        }

        int waitTimeMs = timeOutMs;
        boolean changeSucceeded = false;
        while (!changeSucceeded && waitTimeMs > 0) {
            long startTimeMs = SystemClock.elapsedRealtime();
            changeSucceeded = surfaceChangedDone.block(waitTimeMs);
            if (!changeSucceeded) {
                Log.e(TAG, "Wait for surface change timed out after " + timeOutMs + " ms");
                return changeSucceeded;
            } else {
                // Get a surface change callback, need to check if the size is expected.
                surfaceChangedDone.close();
                if (currentWidth == expectWidth && currentHeight == expectHeight) {
                    return changeSucceeded;
                }
                // Do a further iteration surface change check as surfaceChanged could be called
                // again.
                changeSucceeded = false;
            }
            waitTimeMs -= (SystemClock.elapsedRealtime() - startTimeMs);
        }

        // Couldn't get expected surface size change.
        return false;
     }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "Surface Changed to: " + width + "x" + height);
        currentWidth = width;
        currentHeight = height;
        surfaceChangedDone.open();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
