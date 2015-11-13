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

package android.util.cts;

import android.util.Patterns;

import junit.framework.TestCase;

/**
 * Test {@link Patterns}.
 */
public class PatternsTest extends TestCase {

    public void testWebUrl_matchesUrlsWithCommasInRequestParameterValues() throws Exception {
        String url = "https://android.com/path?ll=37.4221,-122.0836&z=17&pll=37.4221,-122.0836";
        assertTrue("WEB_URL pattern should match commas", Patterns.WEB_URL.matcher(url).matches());
    }
}
