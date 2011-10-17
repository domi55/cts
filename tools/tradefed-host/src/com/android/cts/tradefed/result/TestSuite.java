/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.cts.tradefed.result;

import com.android.tradefed.result.TestResult;

import org.kxml2.io.KXmlSerializer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data structure that represents a "TestSuite" XML element and its children.
 */
class TestSuite {

    static final String TAG = "TestSuite";

    private String mName;

    // use linked hash map for predictable iteration order
    Map<String, TestSuite> mChildSuiteMap = new LinkedHashMap<String, TestSuite>();
    Map<String, TestCase> mChildTestCaseMap = new LinkedHashMap<String, TestCase>();

    /**
     * @param testSuite
     */
    public TestSuite(String suiteName) {
        mName = suiteName;
    }

    public TestSuite() {
    }

    /**
     * @return the name of this suite
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the name of this suite
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Insert the given test result into this suite.
     *
     * @param suiteNames list of remaining suite names for this test
     * @param testClassName the test class name
     * @param testName the test method name
     * @param testResult the {@link TestResult}
     */
    public void insertTest(List<String> suiteNames, String testClassName, String testName,
            TestResult testResult) {
        if (suiteNames.size() <= 0) {
            // no more package segments
            TestCase testCase = getTestCase(testClassName);
            testCase.insertTest(testName, testResult);
        } else {
            String rootName = suiteNames.remove(0);
            TestSuite suite = getTestSuite(rootName);
            suite.insertTest(suiteNames, testClassName, testName, testResult);
        }
    }

    /**
     * Get the child {@link TestSuite} with given name, creating if necessary.
     *
     * @param suiteName
     * @return the {@link TestSuite}
     */
    private TestSuite getTestSuite(String suiteName) {
        TestSuite testSuite = mChildSuiteMap.get(suiteName);
        if (testSuite == null) {
            testSuite = new TestSuite(suiteName);
            mChildSuiteMap.put(suiteName, testSuite);
        }
        return testSuite;
    }

    /**
     * Get the child {@link TestCase} with given name, creating if necessary.
     * @param testCaseName
     * @return
     */
    private TestCase getTestCase(String testCaseName) {
        TestCase testCase = mChildTestCaseMap.get(testCaseName);
        if (testCase == null) {
            testCase = new TestCase(testCaseName);
            mChildTestCaseMap.put(testCaseName, testCase);
        }
        return testCase;
    }

    /**
     * Serialize this object and all its contents to XML.
     *
     * @param serializer
     * @throws IOException
     */
    public void serialize(KXmlSerializer serializer) throws IOException {
        if (mName != null) {
            serializer.startTag(CtsXmlResultReporter.ns, TAG);
            serializer.attribute(CtsXmlResultReporter.ns, "name", mName);
        }
        for (TestSuite childSuite : mChildSuiteMap.values()) {
            childSuite.serialize(serializer);
        }
        for (TestCase childCase : mChildTestCaseMap.values()) {
            childCase.serialize(serializer);
        }
        if (mName != null) {
            serializer.endTag(CtsXmlResultReporter.ns, TAG);
        }
    }
}
