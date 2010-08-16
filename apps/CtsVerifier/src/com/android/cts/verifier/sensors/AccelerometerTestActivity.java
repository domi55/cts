/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.cts.verifier.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * CTS Verifier case for verifying correct integration of accelerometer.
 * Displays a wedge using OpenGL that, on a correctly-integrated device, always
 * points down.
 */
public class AccelerometerTestActivity extends Activity {
    private GLSurfaceView mGLSurfaceView;

    private SensorManager mSensorManager;

    private SensorEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getApplicationContext().getSystemService(
                Context.SENSOR_SERVICE);
        mGLSurfaceView = new GLSurfaceView(this);
        AccelerometerTestRenderer renderer = new AccelerometerTestRenderer(this);
        mListener = renderer;
        mGLSurfaceView.setRenderer(renderer);
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mListener);
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
        mSensorManager.registerListener(mListener, mSensorManager.getSensorList(
                Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_UI);
    }
}
