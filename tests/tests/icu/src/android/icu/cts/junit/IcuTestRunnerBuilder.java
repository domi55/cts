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

package android.icu.cts.junit;

import android.icu.dev.test.TestFmwk;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

/**
 * A {@link RunnerBuilder} used for running ICU test classes derived from {@link TestFmwk}.
 */
public class IcuTestRunnerBuilder extends RunnerBuilder {

    private final IcuRunnerParams icuRunnerParams;

    public IcuTestRunnerBuilder(IcuRunnerParams icuRunnerParams) {
        this.icuRunnerParams = icuRunnerParams;
    }

    @Override
    public Runner runnerForClass(Class<?> testClass) throws Throwable {
        try {
            // Check for a TestGroup before a TestFmwk class as TestGroup is a subclass of TestFmwk
            if (TestFmwk.TestGroup.class.isAssignableFrom(testClass)) {
                return new IcuTestGroupRunner(testClass.asSubclass(TestFmwk.TestGroup.class), this);
            }

            if (TestFmwk.class.isAssignableFrom(testClass)) {
                // Make sure that in the event of an error the resulting Runner an be filtered out.
                return new IcuTestFmwkRunner(testClass.asSubclass(TestFmwk.class), icuRunnerParams);
            }
        } catch (Exception e) {
            return new ErrorReportingRunner(testClass.getName(), e);
        }

        return null;
    }
}
