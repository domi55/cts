/*
 * Copyright (C) 2009 The Android Open Source Project
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

package signature.io.impl;

import signature.compare.model.IApiDelta;
import signature.io.IApiDeltaExternalizer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ToStringExternalizer implements IApiDeltaExternalizer {

    public void externalize(String location, IApiDelta delta)
            throws IOException {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                new FileOutputStream(location));

        if (delta == null) {
            outputStreamWriter.write("No delta found!");
        } else {
            outputStreamWriter.write(delta.toString());
        }
        outputStreamWriter.close();
    }
}
