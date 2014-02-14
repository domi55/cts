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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/gen_runtime.

package android.renderscript.cts;

import android.renderscript.Allocation;
import android.renderscript.RSRuntimeException;
import android.renderscript.Element;

public class TestTgamma extends RSBaseCompute {

    private ScriptC_TestTgamma script;
    private ScriptC_TestTgammaRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestTgamma(mRS);
        scriptRelaxed = new ScriptC_TestTgammaRelaxed(mRS);
    }

    private void checkTgammaFloatFloat() {
        Allocation in = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x321eee2f0950016aL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testTgammaFloatFloat(in, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testTgammaFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testTgammaFloatFloat(in, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testTgammaFloatFloat: " + e.toString());
        }
    }

    private void checkTgammaFloat2Float2() {
        Allocation in = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xc380189b2ad6cf46L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.forEach_testTgammaFloat2Float2(in, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testTgammaFloat2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testTgammaFloat2Float2(in, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testTgammaFloat2Float2: " + e.toString());
        }
    }

    private void checkTgammaFloat3Float3() {
        Allocation in = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xee0d391b563782c0L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.forEach_testTgammaFloat3Float3(in, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testTgammaFloat3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testTgammaFloat3Float3(in, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testTgammaFloat3Float3: " + e.toString());
        }
    }

    private void checkTgammaFloat4Float4() {
        Allocation in = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x189a599b8198363aL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.forEach_testTgammaFloat4Float4(in, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testTgammaFloat4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testTgammaFloat4Float4(in, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testTgammaFloat4Float4: " + e.toString());
        }
    }

    public void testTgamma() {
        checkTgammaFloatFloat();
        checkTgammaFloat2Float2();
        checkTgammaFloat3Float3();
        checkTgammaFloat4Float4();
    }
}
