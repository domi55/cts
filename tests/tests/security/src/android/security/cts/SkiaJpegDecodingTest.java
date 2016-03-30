/*
 * Copyright (C) 2016 The Android Open Source Project
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

package android.security.cts;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase;
import android.widget.ImageView;
import android.widget.cts.ImageViewStubActivity;

import com.android.cts.stub.R;

public class SkiaJpegDecodingTest extends ActivityInstrumentationTestCase<ImageViewStubActivity> {
    private Activity mActivity;

    public SkiaJpegDecodingTest() {
        super("com.android.cts.stub", ImageViewStubActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    public void testLibskiaOverFlowJpegProcessing() {
        ImageView mImageView = new ImageView(mActivity);
        mImageView.setImageResource(R.drawable.signal_sigsegv_in_jmem_ashmem);
    }

    @Override
    protected void tearDown() throws Exception {
        if (mActivity != null) {
            mActivity.finish();
        }
        super.tearDown();
    }
}
