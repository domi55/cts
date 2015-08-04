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
package com.android.compatibility.common.util;

/**
 * Represents a single test result.
 */
public class TestResult implements ITestResult {

    private final ICaseResult mParent;
    private final String mTestName;
    private long mStartTime;
    private long mEndTime;
    private TestStatus mResult;
    private String mMessage;
    private String mStackTrace;
    private ReportLog mReport;
    private String mBugReport;
    private String mLog;

    /**
     * Create a {@link TestResult} for the given test name.
     */
    public TestResult(ICaseResult parent, String name) {
        mParent = parent;
        mTestName = name;
        resetResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return mTestName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFullName() {
        return String.format("%s#%s", mParent.getName(), getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestStatus getResultStatus() {
        return mResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResultStatus(TestStatus status) {
        mResult = status;
        mEndTime = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return mMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessage(String message) {
        mMessage = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStartTime(long time) {
        mStartTime = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getStartTime() {
        return mStartTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEndTime(long time) {
        mEndTime = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getEndTime() {
        return mEndTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStackTrace() {
        return mStackTrace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStackTrace(String stackTrace) {
        mStackTrace = sanitizeStackTrace(stackTrace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportLog getReportLog() {
        return mReport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReportLog(ReportLog report) {
        mReport = report;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBugReport() {
        return mBugReport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBugReport(String uri) {
        mBugReport = uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLog() {
        return mLog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLog(String uri) {
        mLog = uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(String trace) {
        setResultStatus(TestStatus.FAIL);
        int index = trace.indexOf('\n');
        if (index < 0) {
            // Trace is a single line, just set the message to be the same as the stacktrace.
            setMessage(trace);
        } else {
            setMessage(trace.substring(0, index));
        }
        setStackTrace(trace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void passed(ReportLog report) {
        if (!getResultStatus().equals(TestStatus.FAIL)) {
            setResultStatus(TestStatus.PASS);
            if (report != null) {
                setReportLog(report);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetResult() {
        setResultStatus(TestStatus.NOT_EXECUTED);
        setStartTime(System.currentTimeMillis());
        setMessage(null);
        setStackTrace(null);
        setReportLog(null);
        setBugReport(null);
        setLog(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(ITestResult another) {
        return getName().compareTo(another.getName());
    }

    /**
     * Strip out any invalid XML characters that might cause the report to be unviewable.
     * http://www.w3.org/TR/REC-xml/#dt-character
     */
    static String sanitizeStackTrace(String trace) {
        if (trace != null) {
            return trace.replaceAll("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD]", "");
        } else {
            return null;
        }
    }

}
