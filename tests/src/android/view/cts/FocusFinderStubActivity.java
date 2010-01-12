/*
 * Copyright (C) 2008 The Android Open Source Project
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

package android.view.cts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.content.Context;

/**
 * Holds a few buttons of various sizes and horizontal placements in a vertical
 * layout to excercise some core focus searching.
 */
public class FocusFinderStubActivity extends Activity {

    private LinearLayout mLayout;

    private Button mTopWide;
    private Button mMidSkinny1Left;
    private Button mBottomWide;

    private Button mMidSkinny2Right;
    public static final String mTopWideLable = "top wide";
    public static final String mBottomWideLable = "bottom wide";
    public static final String mMidSkinny1LeftLable = "mid skinny 1(L)";
    public static final String mMidSkinny2RightLable = "mid skinny 2(R)";

    public LinearLayout getLayout() {
        return mLayout;
    }

    public Button getTopWide() {
        return mTopWide;
    }

    public Button getMidSkinny1Left() {
        return mMidSkinny1Left;
    }

    public Button getMidSkinny2Right() {
        return mMidSkinny2Right;
    }

    public Button getBottomWide() {
        return mBottomWide;
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mLayout = new LinearLayout(this);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        mLayout.setHorizontalGravity(Gravity.LEFT);
        mLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

        mTopWide = makeWide(mTopWideLable);
        mLayout.addView(mTopWide);

        mMidSkinny1Left = addSkinny(mLayout, mMidSkinny1LeftLable, false);

        mMidSkinny2Right = addSkinny(mLayout,mMidSkinny2RightLable, true);

        mBottomWide = makeWide(mBottomWideLable);
        mLayout.addView(mBottomWide);

        setContentView(mLayout);
    }

    // just to get toString non-sucky
    private static class MyButton extends Button {

        public MyButton(Context context) {
            super(context);
        }

        @Override
        public String toString() {
            return getText().toString();
        }
    }

    private Button makeWide(String label) {
        Button button = new MyButton(this);
        button.setText(label);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return button;
    }

    /**
     * Add a skinny button that takes up just less than half of the screen
     * horizontally.
     *
     * @param root
     *            The layout to add the button to.
     * @param label
     *            The label of the button.
     * @param atRight
     *            Which side to put the button on.
     * @return The newly created button.
     */
    private Button addSkinny(LinearLayout root, String label, boolean atRight) {
        Button button = new MyButton(this);
        button.setText(label);
        button.setLayoutParams(new LinearLayout.LayoutParams(0, // width
                ViewGroup.LayoutParams.WRAP_CONTENT, 480));

        TextView filler = new TextView(this);
        filler.setText("filler");
        filler.setLayoutParams(new LinearLayout.LayoutParams(0, // width
                ViewGroup.LayoutParams.WRAP_CONTENT, 520));

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (atRight) {
            ll.addView(filler);
            ll.addView(button);
            root.addView(ll);
        } else {
            ll.addView(button);
            ll.addView(filler);
            root.addView(ll);
        }
        return button;
    }

}

