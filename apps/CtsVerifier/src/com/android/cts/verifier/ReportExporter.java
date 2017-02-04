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

package com.android.cts.verifier;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

import com.android.compatibility.common.util.IInvocationResult;
import com.android.compatibility.common.util.InvocationResult;
import com.android.compatibility.common.util.ResultHandler;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Background task to generate a report and save it to external storage.
 */
class ReportExporter extends AsyncTask<Void, Void, String> {

    private static final String COMMAND_LINE_ARGS = "CtsVerifier";
    private static final String LOG_URL = null;
    private static final String REFERENCE_URL = null;
    private static final String SUITE_NAME = "ctsverifier";
    private static final String SUITE_PLAN = "CTSVERIFIER";
    private static final String SUITE_BUILD = "0";

    private static final long START_MS = SystemClock.uptimeMillis();
    private static final long END_MS = START_MS;

    private static final int BUFFER_SIZE = 16 * 1024;
    private static final String REPORT_DIRECTORY = "ctsVerifierReports";
    private static final String ZIP_EXTENSION = ".zip";

    protected static final Logger LOG = Logger.getLogger(ReportExporter.class.getName());

    private final Context mContext;
    private final TestListAdapter mAdapter;

    ReportExporter(Context context, TestListAdapter adapter) {
        this.mContext = context;
        this.mAdapter = adapter;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            LOG.log(Level.WARNING, "External storage is not writable.");
            return mContext.getString(R.string.no_storage);
        }
        IInvocationResult result;
        try {
            TestResultsReport report = new TestResultsReport(mContext, mAdapter);
            result = report.generateResult();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Couldn't create test results report", e);
            return mContext.getString(R.string.test_results_error);
        }
        // create a directory for CTS Verifier reports
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File verifierReportsDir = new File(externalStorageDirectory, REPORT_DIRECTORY);
        verifierReportsDir.mkdirs();
        // create a temporary directory for this particular report
        File tempDir = new File(verifierReportsDir, getReportName());
        tempDir.mkdirs();

        // create a File object for a report ZIP file
        File reportZipFile = new File(verifierReportsDir, getReportName() + ZIP_EXTENSION);
        // create a File object for the result XML file generated by ResultHandler
        File tempXmlFile = new File(tempDir, ResultHandler.TEST_RESULT_FILE_NAME);

        try {
            // Serialize the report
            String versionName = Version.getVersionName(mContext);
            ResultHandler.writeResults(SUITE_NAME, versionName, SUITE_PLAN, SUITE_BUILD,
                    result, tempDir, START_MS, END_MS, REFERENCE_URL, LOG_URL,
                    COMMAND_LINE_ARGS);

            // create a compressed ZIP file
            FileOutputStream reportFileStream = new FileOutputStream(reportZipFile);
            ZipOutputStream reportZipStream =
                    new ZipOutputStream(new BufferedOutputStream(reportFileStream));
            ZipEntry entry = new ZipEntry(ResultHandler.TEST_RESULT_FILE_NAME);
            reportZipStream.putNextEntry(entry);

            // TODO: compress all results via tradefed ZipUtil or equivalent;
            // the current implementation only saves the generated result XML

            // write the report to the ZIP file and close the ZIP file
            FileInputStream tempXmlStream = new FileInputStream(tempXmlFile);
            int size = -1;
            byte[] ioBuffer = new byte[BUFFER_SIZE];
            while ((size = tempXmlStream.read(ioBuffer)) != -1) {
                reportZipStream.write(ioBuffer, 0, size);
            }
            reportZipStream.close();

        } catch (IOException | XmlPullParserException e) {
            LOG.log(Level.WARNING, "I/O exception writing report to storage.", e);
            return mContext.getString(R.string.no_storage);
        } finally {
            // delete the temporary results file and directory made for the reports
            tempXmlFile.delete();
            tempDir.delete();
        }
        return mContext.getString(R.string.report_saved, reportZipFile.getPath());
    }

    private String getReportName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss", Locale.ENGLISH);
        String date = dateFormat.format(new Date());
        return String.format( "%s-%s-%s-%s-%s",
                date, Build.MANUFACTURER, Build.PRODUCT, Build.DEVICE, Build.ID);
    }

    @Override
    protected void onPostExecute(String result) {
        new AlertDialog.Builder(mContext)
                .setMessage(result)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
