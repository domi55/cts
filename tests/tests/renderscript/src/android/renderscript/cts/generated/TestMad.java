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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/generate.sh.

package android.renderscript.cts;

import android.renderscript.Allocation;
import android.renderscript.RSRuntimeException;
import android.renderscript.Element;

import java.util.Arrays;

public class TestMad extends RSBaseCompute {

    private ScriptC_TestMad script;
    private ScriptC_TestMadRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestMad(mRS);
        scriptRelaxed = new ScriptC_TestMadRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloatFloat {
        public float inMultiplicand1;
        public float inMultiplicand2;
        public float inOffset;
        public Target.Floaty out;
    }

    private void checkMadFloatFloatFloatFloat() {
        Allocation inMultiplicand1 = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x40b4de48l, false);
        Allocation inMultiplicand2 = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x40b4de49l, false);
        Allocation inOffset = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xc50ce0fcl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInMultiplicand2(inMultiplicand2);
            script.set_gAllocInOffset(inOffset);
            script.forEach_testMadFloatFloatFloatFloat(inMultiplicand1, out);
            verifyResultsMadFloatFloatFloatFloat(inMultiplicand1, inMultiplicand2, inOffset, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloatFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInMultiplicand2(inMultiplicand2);
            scriptRelaxed.set_gAllocInOffset(inOffset);
            scriptRelaxed.forEach_testMadFloatFloatFloatFloat(inMultiplicand1, out);
            verifyResultsMadFloatFloatFloatFloat(inMultiplicand1, inMultiplicand2, inOffset, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloatFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsMadFloatFloatFloatFloat(Allocation inMultiplicand1, Allocation inMultiplicand2, Allocation inOffset, Allocation out, boolean relaxed) {
        float[] arrayInMultiplicand1 = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInMultiplicand1, (float) 42);
        inMultiplicand1.copyTo(arrayInMultiplicand1);
        float[] arrayInMultiplicand2 = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInMultiplicand2, (float) 42);
        inMultiplicand2.copyTo(arrayInMultiplicand2);
        float[] arrayInOffset = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInOffset, (float) 42);
        inOffset.copyTo(arrayInOffset);
        float[] arrayOut = new float[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inMultiplicand1 = arrayInMultiplicand1[i];
                args.inMultiplicand2 = arrayInMultiplicand2[i];
                args.inOffset = arrayInOffset[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeMad(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMultiplicand1: ");
                        appendVariableToMessage(message, args.inMultiplicand1);
                        message.append("\n");
                        message.append("Input inMultiplicand2: ");
                        appendVariableToMessage(message, args.inMultiplicand2);
                        message.append("\n");
                        message.append("Input inOffset: ");
                        appendVariableToMessage(message, args.inOffset);
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
        assertFalse("Incorrect output for checkMadFloatFloatFloatFloat" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkMadFloat2Float2Float2Float2() {
        Allocation inMultiplicand1 = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x8a5fd7c0l, false);
        Allocation inMultiplicand2 = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x8a5fd7c1l, false);
        Allocation inOffset = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x318f8924l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInMultiplicand2(inMultiplicand2);
            script.set_gAllocInOffset(inOffset);
            script.forEach_testMadFloat2Float2Float2Float2(inMultiplicand1, out);
            verifyResultsMadFloat2Float2Float2Float2(inMultiplicand1, inMultiplicand2, inOffset, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat2Float2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInMultiplicand2(inMultiplicand2);
            scriptRelaxed.set_gAllocInOffset(inOffset);
            scriptRelaxed.forEach_testMadFloat2Float2Float2Float2(inMultiplicand1, out);
            verifyResultsMadFloat2Float2Float2Float2(inMultiplicand1, inMultiplicand2, inOffset, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat2Float2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsMadFloat2Float2Float2Float2(Allocation inMultiplicand1, Allocation inMultiplicand2, Allocation inOffset, Allocation out, boolean relaxed) {
        float[] arrayInMultiplicand1 = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInMultiplicand1, (float) 42);
        inMultiplicand1.copyTo(arrayInMultiplicand1);
        float[] arrayInMultiplicand2 = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInMultiplicand2, (float) 42);
        inMultiplicand2.copyTo(arrayInMultiplicand2);
        float[] arrayInOffset = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInOffset, (float) 42);
        inOffset.copyTo(arrayInOffset);
        float[] arrayOut = new float[INPUTSIZE * 2];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inMultiplicand1 = arrayInMultiplicand1[i * 2 + j];
                args.inMultiplicand2 = arrayInMultiplicand2[i * 2 + j];
                args.inOffset = arrayInOffset[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeMad(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMultiplicand1: ");
                        appendVariableToMessage(message, args.inMultiplicand1);
                        message.append("\n");
                        message.append("Input inMultiplicand2: ");
                        appendVariableToMessage(message, args.inMultiplicand2);
                        message.append("\n");
                        message.append("Input inOffset: ");
                        appendVariableToMessage(message, args.inOffset);
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
        assertFalse("Incorrect output for checkMadFloat2Float2Float2Float2" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkMadFloat3Float3Float3Float3() {
        Allocation inMultiplicand1 = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xe1551f1cl, false);
        Allocation inMultiplicand2 = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xe1551f1dl, false);
        Allocation inOffset = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xfb77d218l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInMultiplicand2(inMultiplicand2);
            script.set_gAllocInOffset(inOffset);
            script.forEach_testMadFloat3Float3Float3Float3(inMultiplicand1, out);
            verifyResultsMadFloat3Float3Float3Float3(inMultiplicand1, inMultiplicand2, inOffset, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat3Float3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInMultiplicand2(inMultiplicand2);
            scriptRelaxed.set_gAllocInOffset(inOffset);
            scriptRelaxed.forEach_testMadFloat3Float3Float3Float3(inMultiplicand1, out);
            verifyResultsMadFloat3Float3Float3Float3(inMultiplicand1, inMultiplicand2, inOffset, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat3Float3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsMadFloat3Float3Float3Float3(Allocation inMultiplicand1, Allocation inMultiplicand2, Allocation inOffset, Allocation out, boolean relaxed) {
        float[] arrayInMultiplicand1 = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInMultiplicand1, (float) 42);
        inMultiplicand1.copyTo(arrayInMultiplicand1);
        float[] arrayInMultiplicand2 = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInMultiplicand2, (float) 42);
        inMultiplicand2.copyTo(arrayInMultiplicand2);
        float[] arrayInOffset = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInOffset, (float) 42);
        inOffset.copyTo(arrayInOffset);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inMultiplicand1 = arrayInMultiplicand1[i * 4 + j];
                args.inMultiplicand2 = arrayInMultiplicand2[i * 4 + j];
                args.inOffset = arrayInOffset[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeMad(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMultiplicand1: ");
                        appendVariableToMessage(message, args.inMultiplicand1);
                        message.append("\n");
                        message.append("Input inMultiplicand2: ");
                        appendVariableToMessage(message, args.inMultiplicand2);
                        message.append("\n");
                        message.append("Input inOffset: ");
                        appendVariableToMessage(message, args.inOffset);
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
        assertFalse("Incorrect output for checkMadFloat3Float3Float3Float3" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkMadFloat4Float4Float4Float4() {
        Allocation inMultiplicand1 = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x384a6678l, false);
        Allocation inMultiplicand2 = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x384a6679l, false);
        Allocation inOffset = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xc5601b0cl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInMultiplicand2(inMultiplicand2);
            script.set_gAllocInOffset(inOffset);
            script.forEach_testMadFloat4Float4Float4Float4(inMultiplicand1, out);
            verifyResultsMadFloat4Float4Float4Float4(inMultiplicand1, inMultiplicand2, inOffset, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat4Float4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInMultiplicand2(inMultiplicand2);
            scriptRelaxed.set_gAllocInOffset(inOffset);
            scriptRelaxed.forEach_testMadFloat4Float4Float4Float4(inMultiplicand1, out);
            verifyResultsMadFloat4Float4Float4Float4(inMultiplicand1, inMultiplicand2, inOffset, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat4Float4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsMadFloat4Float4Float4Float4(Allocation inMultiplicand1, Allocation inMultiplicand2, Allocation inOffset, Allocation out, boolean relaxed) {
        float[] arrayInMultiplicand1 = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInMultiplicand1, (float) 42);
        inMultiplicand1.copyTo(arrayInMultiplicand1);
        float[] arrayInMultiplicand2 = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInMultiplicand2, (float) 42);
        inMultiplicand2.copyTo(arrayInMultiplicand2);
        float[] arrayInOffset = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInOffset, (float) 42);
        inOffset.copyTo(arrayInOffset);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inMultiplicand1 = arrayInMultiplicand1[i * 4 + j];
                args.inMultiplicand2 = arrayInMultiplicand2[i * 4 + j];
                args.inOffset = arrayInOffset[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeMad(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMultiplicand1: ");
                        appendVariableToMessage(message, args.inMultiplicand1);
                        message.append("\n");
                        message.append("Input inMultiplicand2: ");
                        appendVariableToMessage(message, args.inMultiplicand2);
                        message.append("\n");
                        message.append("Input inOffset: ");
                        appendVariableToMessage(message, args.inOffset);
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
        assertFalse("Incorrect output for checkMadFloat4Float4Float4Float4" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public void testMad() {
        checkMadFloatFloatFloatFloat();
        checkMadFloat2Float2Float2Float2();
        checkMadFloat3Float3Float3Float3();
        checkMadFloat4Float4Float4Float4();
    }
}
