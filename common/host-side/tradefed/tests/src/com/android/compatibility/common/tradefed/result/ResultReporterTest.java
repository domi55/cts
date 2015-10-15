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

package com.android.compatibility.common.tradefed.result;

import com.android.compatibility.common.tradefed.build.CompatibilityBuildHelper;
import com.android.compatibility.common.util.AbiUtils;
import com.android.compatibility.common.util.ICaseResult;
import com.android.compatibility.common.util.IInvocationResult;
import com.android.compatibility.common.util.IModuleResult;
import com.android.compatibility.common.util.ITestResult;
import com.android.compatibility.common.util.TestStatus;
import com.android.ddmlib.testrunner.TestIdentifier;
import com.android.tradefed.build.FolderBuildInfo;
import com.android.tradefed.build.IFolderBuildInfo;
import com.android.tradefed.config.OptionSetter;
import com.android.tradefed.util.FileUtil;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.List;

public class ResultReporterTest extends TestCase {

    private static final String ROOT_PROPERTY = "TESTS_ROOT";
    private static final String BUILD_NUMBER = "2";
    private static final String SUITE_PLAN = "cts";
    private static final String DYNAMIC_CONFIG_URL = "";
    private static final String ROOT_DIR_NAME = "root";
    private static final String BASE_DIR_NAME = "android-tests";
    private static final String TESTCASES = "testcases";
    private static final String NAME = "ModuleName";
    private static final String ABI = "mips64";
    private static final String ID = AbiUtils.createId(ABI, NAME);
    private static final String CLASS = "android.test.FoorBar";
    private static final String METHOD_1 = "testBlah1";
    private static final String METHOD_2 = "testBlah2";
    private static final String METHOD_3 = "testBlah3";
    private static final String TEST_1 = String.format("%s#%s", CLASS, METHOD_1);
    private static final String TEST_2 = String.format("%s#%s", CLASS, METHOD_2);
    private static final String TEST_3 = String.format("%s#%s", CLASS, METHOD_3);
    private static final String STACK_TRACE = "Something small is not alright\n " +
            "at four.big.insects.Marley.sing(Marley.java:10)";
    private static final String RESULT_DIR = "result123";
    private static final String[] FORMATTING_FILES = {
        "compatibility_result.css",
        "compatibility_result.xsd",
        "compatibility_result.xsl",
        "logo.png",
        "newrule_green.png"};

    private ResultReporter mReporter;
    private IFolderBuildInfo mBuildInfo;
    private CompatibilityBuildHelper mBuildHelper;

    private File mRoot = null;
    private File mBase = null;
    private File mTests = null;

    @Override
    public void setUp() throws Exception {
        mReporter = new ResultReporter();
        OptionSetter setter = new OptionSetter(mReporter);
        setter.setOptionValue("quiet-output", "true");
        mRoot = FileUtil.createTempDir(ROOT_DIR_NAME);
        mBase = new File(mRoot, BASE_DIR_NAME);
        mBase.mkdirs();
        mTests = new File(mBase, TESTCASES);
        mTests.mkdirs();
        System.setProperty(ROOT_PROPERTY, mRoot.getAbsolutePath());
        mBuildInfo = new FolderBuildInfo(BUILD_NUMBER, "", "");
        mBuildHelper = new CompatibilityBuildHelper(mBuildInfo);
        mBuildHelper.init(SUITE_PLAN, DYNAMIC_CONFIG_URL);
    }

    @Override
    public void tearDown() throws Exception {
        mReporter = null;
        FileUtil.recursiveDelete(mRoot);
    }

    public void testSetup() throws Exception {
        mReporter.invocationStarted(mBuildInfo);
        // Should have created a directory for the logs
        File[] children = mBuildHelper.getLogsDir().listFiles();
        assertTrue("Didn't create logs dir", children.length == 1 && children[0].isDirectory());
        // Should have created a directory for the results
        children = mBuildHelper.getResultsDir().listFiles();
        assertTrue("Didn't create results dir", children.length == 1 && children[0].isDirectory());
        mReporter.invocationEnded(10);
        // Should have created a zip file
        children = mBuildHelper.getResultsDir().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".zip");
            }
        });
        assertTrue("Didn't create results zip",
                children.length == 1 && children[0].isFile() && children[0].length() > 0);
    }

    public void testResultReporting() throws Exception {
        mReporter.invocationStarted(mBuildInfo);
        mReporter.testRunStarted(ID, 2);
        TestIdentifier test1 = new TestIdentifier(CLASS, METHOD_1);
        mReporter.testStarted(test1);
        mReporter.testEnded(test1, new HashMap<String, String>());
        TestIdentifier test2 = new TestIdentifier(CLASS, METHOD_2);
        mReporter.testStarted(test2);
        mReporter.testFailed(test2, STACK_TRACE);
        TestIdentifier test3 = new TestIdentifier(CLASS, METHOD_3);
        mReporter.testStarted(test3);
        mReporter.testFailed(test3, STACK_TRACE);
        mReporter.testRunEnded(10, new HashMap<String, String>());
        mReporter.invocationEnded(10);
        IInvocationResult result = mReporter.getResult();
        assertEquals("Expected 1 pass", 1, result.countResults(TestStatus.PASS));
        assertEquals("Expected 2 failures", 2, result.countResults(TestStatus.FAIL));
        List<IModuleResult> modules = result.getModules();
        assertEquals("Expected 1 module", 1, modules.size());
        IModuleResult module = modules.get(0);
        assertEquals("Incorrect ID", ID, module.getId());
        List<ICaseResult> caseResults = module.getResults();
        assertEquals("Expected 1 test case", 1, caseResults.size());
        ICaseResult caseResult = caseResults.get(0);
        List<ITestResult> testResults = caseResult.getResults();
        assertEquals("Expected 3 tests", 3, testResults.size());
        ITestResult result1 = caseResult.getResult(METHOD_1);
        assertNotNull(String.format("Expected result for %s", TEST_1), result1);
        assertEquals(String.format("Expected pass for %s", TEST_1), TestStatus.PASS,
                result1.getResultStatus());
        ITestResult result2 = caseResult.getResult(METHOD_2);
        assertNotNull(String.format("Expected result for %s", TEST_2), result2);
        assertEquals(String.format("Expected fail for %s", TEST_2), TestStatus.FAIL,
                result2.getResultStatus());
        ITestResult result3 = caseResult.getResult(METHOD_3);
        assertNotNull(String.format("Expected result for %s", TEST_3), result3);
        assertEquals(String.format("Expected fail for %s", TEST_3), TestStatus.FAIL,
                result3.getResultStatus());
    }

    private void makeTestRun(String[] methods, boolean[] passes) {
        mReporter.testRunStarted(ID, methods.length);

        for (int i = 0; i < methods.length; i++) {
            TestIdentifier test = new TestIdentifier(CLASS, methods[i]);
            mReporter.testStarted(test);
            if (!passes[i]) {
                mReporter.testFailed(test, STACK_TRACE);
            }
            mReporter.testEnded(test, new HashMap<String, String>());
        }

        mReporter.testRunEnded(10, new HashMap<String, String>());
    }

    public void testRepeatedExecutions() throws Exception {
        String[] methods = new String[] {METHOD_1, METHOD_2, METHOD_3};

        mReporter.invocationStarted(mBuildInfo);

        makeTestRun(methods, new boolean[] {true, false, true});
        makeTestRun(methods, new boolean[] {true, false, false});
        makeTestRun(methods, new boolean[] {true, true, true});

        mReporter.invocationEnded(10);

        // Verification

        IInvocationResult result = mReporter.getResult();
        assertEquals("Expected 1 pass", 1, result.countResults(TestStatus.PASS));
        assertEquals("Expected 2 failures", 2, result.countResults(TestStatus.FAIL));
        List<IModuleResult> modules = result.getModules();
        assertEquals("Expected 1 module", 1, modules.size());
        IModuleResult module = modules.get(0);
        assertEquals("Incorrect ID", ID, module.getId());
        List<ICaseResult> caseResults = module.getResults();
        assertEquals("Expected 1 test case", 1, caseResults.size());
        ICaseResult caseResult = caseResults.get(0);
        List<ITestResult> testResults = caseResult.getResults();
        assertEquals("Expected 3 tests", 3, testResults.size());

        // Test 1 details
        ITestResult result1 = caseResult.getResult(METHOD_1);
        assertNotNull(String.format("Expected result for %s", TEST_1), result1);
        assertEquals(String.format("Expected pass for %s", TEST_1), TestStatus.PASS,
                result1.getResultStatus());

        // Test 2 details
        ITestResult result2 = caseResult.getResult(METHOD_2);
        assertNotNull(String.format("Expected result for %s", TEST_2), result2);
        assertEquals(String.format("Expected fail for %s", TEST_2), TestStatus.FAIL,
                result2.getResultStatus());
        // TODO: Define requirement. Should have both instances in any case.
        assertEquals(result2.getStackTrace(), STACK_TRACE+"\n"+STACK_TRACE);

        // Test 3 details
        ITestResult result3 = caseResult.getResult(METHOD_3);
        assertNotNull(String.format("Expected result for %s", TEST_3), result3);
        assertEquals(String.format("Expected fail for %s", TEST_3), TestStatus.FAIL,
                result3.getResultStatus());
        assertEquals(result3.getStackTrace(), STACK_TRACE);
    }

    public void testCopyFormattingFiles() throws Exception {
        File resultDir = new File(mBuildHelper.getResultsDir(), RESULT_DIR);
        resultDir.mkdirs();
        ResultReporter.copyFormattingFiles(resultDir);
        for (String filename : FORMATTING_FILES) {
            File file = new File(resultDir, filename);
            assertTrue(String.format("%s (%s) was not created", filename, file.getAbsolutePath()),
                    file.exists() && file.isFile() && file.length() > 0);
        }
    }
}
