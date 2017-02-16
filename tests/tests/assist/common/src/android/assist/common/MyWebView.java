/*
 * Copyright (C) 2017 The Android Open Source Project
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
package android.assist.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewStructure;
import android.webkit.WebView;

/**
 * Custom webview to emulate behavior that is required on test cases.
 */
public final class MyWebView extends WebView {

    private String mUrl;

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Same as {@link #loadData(String, String, String)}, but setting the URL in the view structure.
     */
    public void myLoadData(String url, String data, String mimeType, String encoding) {
        mUrl = url;
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void onProvideStructure(ViewStructure structure) {
        super.onProvideStructure(structure);

        onProvideAutoFillStructureForAssistOrAutoFill(structure);
    }

    @Override
    public void onProvideAutoFillStructure(ViewStructure structure, int flags) {
        super.onProvideAutoFillStructure(structure, flags);

        onProvideAutoFillStructureForAssistOrAutoFill(structure);
    }

    private void onProvideAutoFillStructureForAssistOrAutoFill(ViewStructure structure) {
        if (mUrl != null) {
            structure.setUrl(mUrl);
        }
    }
}