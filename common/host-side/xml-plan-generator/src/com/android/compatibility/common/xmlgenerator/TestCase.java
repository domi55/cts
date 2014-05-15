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

package com.android.compatibility.common.xmlgenerator;

import java.util.ArrayList;

public class TestCase {

    private final String mName;
    private final ArrayList<Test> mTests = new ArrayList<Test>();

    public TestCase(String name) {
        mName = name;
    }

    public void addTest(Test test) {
        mTests.add(test);
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Test> getTests() {
        return mTests;
    }
}
