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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/generate.sh.

package android.renderscript.cts;

import android.renderscript.Allocation;
import android.renderscript.RSRuntimeException;
import android.renderscript.Element;
import android.renderscript.cts.Target;

import java.util.Arrays;

public class TestPowr extends RSBaseCompute {

    private ScriptC_TestPowr script;
    private ScriptC_TestPowrRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestPowr(mRS);
        scriptRelaxed = new ScriptC_TestPowrRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloat {
        public float inBase;
        public float inExponent;
        public Target.Floaty out;
    }

    private void checkPowrFloatFloatFloat() {
        Allocation inBase = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xc490ca4l, 0, 3000);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xab21ab0al, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testPowrFloatFloatFloat(inBase, out);
            verifyResultsPowrFloatFloatFloat(inBase, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testPowrFloatFloatFloat(inBase, out);
            verifyResultsPowrFloatFloatFloat(inBase, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsPowrFloatFloatFloat(Allocation inBase, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInBase = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInBase, (float) 42);
        inBase.copyTo(arrayInBase);
        float[] arrayInExponent = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInExponent, (float) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inBase = arrayInBase[i];
                args.inExponent = arrayInExponent[i];
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, relaxed);
                CoreMathVerifier.computePowr(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inBase: ");
                        appendVariableToMessage(message, args.inBase);
                        message.append("\n");
                        message.append("Input inExponent: ");
                        appendVariableToMessage(message, args.inExponent);
                        message.append("\n");
                        message.append("Expected output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append("Actual   output out: ");
                        appendVariableToMessage(message, arrayOut[i * 1 + j]);
                        if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                            message.append(" FAIL");
                        }
                        message.append("\n");
                        message.append("Errors at");
                    }
                    message.append(" [");
                    message.append(Integer.toString(i));
                    message.append(", ");
                    message.append(Integer.toString(j));
                    message.append("]");
                }
            }
        }
        assertFalse("Incorrect output for checkPowrFloatFloatFloat" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkPowrFloat2Float2Float2() {
        Allocation inBase = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xeae1fd16l, 0, 3000);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x817a9ddcl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testPowrFloat2Float2Float2(inBase, out);
            verifyResultsPowrFloat2Float2Float2(inBase, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testPowrFloat2Float2Float2(inBase, out);
            verifyResultsPowrFloat2Float2Float2(inBase, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsPowrFloat2Float2Float2(Allocation inBase, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInBase = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInBase, (float) 42);
        inBase.copyTo(arrayInBase);
        float[] arrayInExponent = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInExponent, (float) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 2];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inBase = arrayInBase[i * 2 + j];
                args.inExponent = arrayInExponent[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, relaxed);
                CoreMathVerifier.computePowr(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inBase: ");
                        appendVariableToMessage(message, args.inBase);
                        message.append("\n");
                        message.append("Input inExponent: ");
                        appendVariableToMessage(message, args.inExponent);
                        message.append("\n");
                        message.append("Expected output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append("Actual   output out: ");
                        appendVariableToMessage(message, arrayOut[i * 2 + j]);
                        if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                            message.append(" FAIL");
                        }
                        message.append("\n");
                        message.append("Errors at");
                    }
                    message.append(" [");
                    message.append(Integer.toString(i));
                    message.append(", ");
                    message.append(Integer.toString(j));
                    message.append("]");
                }
            }
        }
        assertFalse("Incorrect output for checkPowrFloat2Float2Float2" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkPowrFloat3Float3Float3() {
        Allocation inBase = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xd355e289l, 0, 3000);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xd89d205fl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testPowrFloat3Float3Float3(inBase, out);
            verifyResultsPowrFloat3Float3Float3(inBase, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testPowrFloat3Float3Float3(inBase, out);
            verifyResultsPowrFloat3Float3Float3(inBase, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsPowrFloat3Float3Float3(Allocation inBase, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInBase = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInBase, (float) 42);
        inBase.copyTo(arrayInBase);
        float[] arrayInExponent = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInExponent, (float) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inBase = arrayInBase[i * 4 + j];
                args.inExponent = arrayInExponent[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, relaxed);
                CoreMathVerifier.computePowr(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inBase: ");
                        appendVariableToMessage(message, args.inBase);
                        message.append("\n");
                        message.append("Input inExponent: ");
                        appendVariableToMessage(message, args.inExponent);
                        message.append("\n");
                        message.append("Expected output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append("Actual   output out: ");
                        appendVariableToMessage(message, arrayOut[i * 4 + j]);
                        if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                            message.append(" FAIL");
                        }
                        message.append("\n");
                        message.append("Errors at");
                    }
                    message.append(" [");
                    message.append(Integer.toString(i));
                    message.append(", ");
                    message.append(Integer.toString(j));
                    message.append("]");
                }
            }
        }
        assertFalse("Incorrect output for checkPowrFloat3Float3Float3" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkPowrFloat4Float4Float4() {
        Allocation inBase = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xbbc9c7fcl, 0, 3000);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x2fbfa2e2l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testPowrFloat4Float4Float4(inBase, out);
            verifyResultsPowrFloat4Float4Float4(inBase, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testPowrFloat4Float4Float4(inBase, out);
            verifyResultsPowrFloat4Float4Float4(inBase, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsPowrFloat4Float4Float4(Allocation inBase, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInBase = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInBase, (float) 42);
        inBase.copyTo(arrayInBase);
        float[] arrayInExponent = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInExponent, (float) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inBase = arrayInBase[i * 4 + j];
                args.inExponent = arrayInExponent[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, relaxed);
                CoreMathVerifier.computePowr(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inBase: ");
                        appendVariableToMessage(message, args.inBase);
                        message.append("\n");
                        message.append("Input inExponent: ");
                        appendVariableToMessage(message, args.inExponent);
                        message.append("\n");
                        message.append("Expected output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append("Actual   output out: ");
                        appendVariableToMessage(message, arrayOut[i * 4 + j]);
                        if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                            message.append(" FAIL");
                        }
                        message.append("\n");
                        message.append("Errors at");
                    }
                    message.append(" [");
                    message.append(Integer.toString(i));
                    message.append(", ");
                    message.append(Integer.toString(j));
                    message.append("]");
                }
            }
        }
        assertFalse("Incorrect output for checkPowrFloat4Float4Float4" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public void testPowr() {
        checkPowrFloatFloatFloat();
        checkPowrFloat2Float2Float2();
        checkPowrFloat3Float3Float3();
        checkPowrFloat4Float4Float4();
    }
}
