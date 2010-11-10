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

package android.test;

import com.android.internal.util.Predicate;
import com.android.internal.util.Predicates;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.SideEffect;

import android.annotation.cts.RequiredFeatures;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.test.suitebuilder.annotation.HasAnnotation;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;

/**
 * This test runner extends the default InstrumentationTestRunner. It overrides
 * the {@code onCreate(Bundle)} method and sets the system properties necessary
 * for many core tests to run. This is needed because there are some core tests
 * that need writing access to the file system. We also need to set the harness
 * Thread's context ClassLoader. Otherwise some classes and resources will not
 * be found. Finally, we add a means to free memory allocated by a TestCase
 * after its execution.
 *
 * @hide
 */
public class InstrumentationCtsTestRunner extends InstrumentationTestRunner {

    /**
     * Convenience definition of our log tag.
     */
    private static final String TAG = "InstrumentationCtsTestRunner";

    private static final String REPORT_VALUE_ID = "InstrumentationCtsTestRunner";

    private static final int REPORT_VALUE_RESULT_OMITTED = -3;

    /**
     * True if (and only if) we are running in single-test mode (as opposed to
     * batch mode).
     */
    private boolean mSingleTest = false;

    @Override
    public void onCreate(Bundle arguments) {
        // We might want to move this to /sdcard, if is is mounted/writable.
        File cacheDir = getTargetContext().getCacheDir();

        // Set some properties that the core tests absolutely need.
        System.setProperty("user.language", "en");
        System.setProperty("user.region", "US");

        System.setProperty("java.home", cacheDir.getAbsolutePath());
        System.setProperty("user.home", cacheDir.getAbsolutePath());
        System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());
        System.setProperty("user.dir", cacheDir.getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStore",
                "/etc/security/cacerts.bks");

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        if (arguments != null) {
            String classArg = arguments.getString(ARGUMENT_TEST_CLASS);
            mSingleTest = classArg != null && classArg.contains("#");
        }

        // attempt to disable keyguard,  if current test has permission to do so
        // TODO: move this to a better place, such as InstrumentationTestRunner ?
        if (getContext().checkCallingOrSelfPermission(android.Manifest.permission.DISABLE_KEYGUARD)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Disabling keyguard");
            KeyguardManager keyguardManager =
                (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.newKeyguardLock("cts").disableKeyguard();
        } else {
            Log.i(TAG, "Test lacks permission to disable keyguard. " +
                    "UI based tests may fail if keyguard is up");
        }

        super.onCreate(arguments);
    }

    @Override
    protected AndroidTestRunner getAndroidTestRunner() {
        AndroidTestRunner runner = super.getAndroidTestRunner();

        runner.addTestListener(new TestListener() {
            /**
             * The last test class we executed code from.
             */
            private Class<?> lastClass;

            /**
             * The minimum time we expect a test to take.
             */
            private static final int MINIMUM_TIME = 100;

            /**
             * The start time of our current test in System.currentTimeMillis().
             */
            private long startTime;

            public void startTest(Test test) {
                if (test.getClass() != lastClass) {
                    lastClass = test.getClass();
                    printMemory(test.getClass());
                }

                Thread.currentThread().setContextClassLoader(
                        test.getClass().getClassLoader());

                startTime = System.currentTimeMillis();
            }

            public void endTest(Test test) {
                if (test instanceof TestCase) {
                    cleanup((TestCase)test);

                    /*
                     * Make sure all tests take at least MINIMUM_TIME to
                     * complete. If they don't, we wait a bit. The Cupcake
                     * Binder can't handle too many operations in a very
                     * short time, which causes headache for the CTS.
                     */
                    long timeTaken = System.currentTimeMillis() - startTime;

                    if (timeTaken < MINIMUM_TIME) {
                        try {
                            Thread.sleep(MINIMUM_TIME - timeTaken);
                        } catch (InterruptedException ignored) {
                            // We don't care.
                        }
                    }
                }
            }

            public void addError(Test test, Throwable t) {
                // This space intentionally left blank.
            }

            public void addFailure(Test test, AssertionFailedError t) {
                // This space intentionally left blank.
            }

            /**
             * Dumps some memory info.
             */
            private void printMemory(Class<? extends Test> testClass) {
                Runtime runtime = Runtime.getRuntime();

                long total = runtime.totalMemory();
                long free = runtime.freeMemory();
                long used = total - free;

                Log.d(TAG, "Total memory  : " + total);
                Log.d(TAG, "Used memory   : " + used);
                Log.d(TAG, "Free memory   : " + free);
                Log.d(TAG, "Now executing : " + testClass.getName());
            }

            /**
             * Nulls all non-static reference fields in the given test class.
             * This method helps us with those test classes that don't have an
             * explicit tearDown() method. Normally the garbage collector should
             * take care of everything, but since JUnit keeps references to all
             * test cases, a little help might be a good idea.
             */
            private void cleanup(TestCase test) {
                Class<?> clazz = test.getClass();

                while (clazz != TestCase.class) {
                    Field[] fields = clazz.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        Field f = fields[i];
                        if (!f.getType().isPrimitive() &&
                                !Modifier.isStatic(f.getModifiers())) {
                            try {
                                f.setAccessible(true);
                                f.set(test, null);
                            } catch (Exception ignored) {
                                // Nothing we can do about it.
                            }
                        }
                    }

                    clazz = clazz.getSuperclass();
                }
            }

        });

        return runner;
    }

    @Override
    List<Predicate<TestMethod>> getBuilderRequirements() {
        List<Predicate<TestMethod>> builderRequirements =
                super.getBuilderRequirements();

        Predicate<TestMethod> brokenTestPredicate =
                Predicates.not(new HasAnnotation(BrokenTest.class));
        builderRequirements.add(brokenTestPredicate);

        builderRequirements.add(getFeaturePredicate());

        if (!mSingleTest) {
            Predicate<TestMethod> sideEffectPredicate =
                    Predicates.not(new HasAnnotation(SideEffect.class));
            builderRequirements.add(sideEffectPredicate);
        }
        return builderRequirements;
    }

    /**
     * Send back an indication that a test was omitted. InstrumentationTestRunner won't run omitted
     * tests, but CTS needs to know that the test was omitted. Otherwise, it will attempt to rerun
     * the test thinking that ADB must have crashed or something.
     */
    private void sendOmittedStatus(TestMethod t) {
        Bundle bundle = new Bundle();
        bundle.putString(Instrumentation.REPORT_KEY_IDENTIFIER, REPORT_VALUE_ID);
        bundle.putInt(InstrumentationTestRunner.REPORT_KEY_NUM_TOTAL, 1);
        bundle.putInt(InstrumentationTestRunner.REPORT_KEY_NUM_CURRENT, 1);
        bundle.putString(InstrumentationTestRunner.REPORT_KEY_NAME_CLASS,
                t.getEnclosingClassname());
        bundle.putString(InstrumentationTestRunner.REPORT_KEY_NAME_TEST,
                t.getName());

        // First status message causes CTS to print out the test name like "Class#test..."
        sendStatus(InstrumentationTestRunner.REPORT_VALUE_RESULT_START, bundle);

        // Second status message causes CTS to complete the line like "Class#test...(omitted)"
        sendStatus(REPORT_VALUE_RESULT_OMITTED, bundle);
    }


    private Predicate<TestMethod> getFeaturePredicate() {
        return new Predicate<TestMethod>() {
            public boolean apply(TestMethod t) {
                if (isValidTest(t)) {
                    // InstrumentationTestRunner will run the test and send back results.
                    return true;
                } else {
                    // InstrumentationTestRunner WON'T run the test, so send back omitted status.
                    sendOmittedStatus(t);
                    return false;
                }
            }

            private boolean isValidTest(TestMethod t) {
                Set<String> features = new HashSet<String>();
                add(features, t.getAnnotation(RequiredFeatures.class));
                add(features, t.getEnclosingClass().getAnnotation(RequiredFeatures.class));

                // Run the test only if the device supports all the features.
                PackageManager packageManager = getContext().getPackageManager();
                FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
                if (featureInfos != null) {
                    for (FeatureInfo featureInfo : featureInfos) {
                        features.remove(featureInfo.name);
                    }
                }
                return features.isEmpty();
            }

            private void add(Set<String> features, RequiredFeatures annotation) {
                if (annotation != null) {
                    Collections.addAll(features, annotation.value());
                }
            }
        };
    }
}
