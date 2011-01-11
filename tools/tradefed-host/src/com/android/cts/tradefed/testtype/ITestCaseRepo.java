/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.cts.tradefed.testtype;


/**
 * Interface for accessing tests from the CTS repository.
 */
interface ITestCaseRepo {

    /**
     * Get a {@link TestPackageDef} given a uri
     *
     * @param testUri the string uris
     * @return a {@link TestPackageDef} or <code>null</code> if the uri cannot be found in repo
     */
    public ITestPackageDef getTestPackage(String testUri);

}
