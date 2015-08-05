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

public class TestLdexp extends RSBaseCompute {

    private ScriptC_TestLdexp script;
    private ScriptC_TestLdexpRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestLdexp(mRS);
        scriptRelaxed = new ScriptC_TestLdexpRelaxed(mRS);
    }

    public class ArgumentsFloatIntFloat {
        public float inMantissa;
        public int inExponent;
        public Target.Floaty out;
    }

    private void checkLdexpFloatIntFloat() {
        Allocation inMantissa = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x90f0e26fl, false);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 1, 0x2e4133c4l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testLdexpFloatIntFloat(inMantissa, out);
            verifyResultsLdexpFloatIntFloat(inMantissa, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloatIntFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testLdexpFloatIntFloat(inMantissa, out);
            verifyResultsLdexpFloatIntFloat(inMantissa, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloatIntFloat: " + e.toString());
        }
    }

    private void verifyResultsLdexpFloatIntFloat(Allocation inMantissa, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInMantissa = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInMantissa, (float) 42);
        inMantissa.copyTo(arrayInMantissa);
        int[] arrayInExponent = new int[INPUTSIZE * 1];
        Arrays.fill(arrayInExponent, (int) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inMantissa = arrayInMantissa[i];
                args.inExponent = arrayInExponent[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLdexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMantissa: ");
                        appendVariableToMessage(message, args.inMantissa);
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
        assertFalse("Incorrect output for checkLdexpFloatIntFloat" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkLdexpFloat2Int2Float2() {
        Allocation inMantissa = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x4fe92893l, false);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 2, 0xed3979e8l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testLdexpFloat2Int2Float2(inMantissa, out);
            verifyResultsLdexpFloat2Int2Float2(inMantissa, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat2Int2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testLdexpFloat2Int2Float2(inMantissa, out);
            verifyResultsLdexpFloat2Int2Float2(inMantissa, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat2Int2Float2: " + e.toString());
        }
    }

    private void verifyResultsLdexpFloat2Int2Float2(Allocation inMantissa, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInMantissa = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInMantissa, (float) 42);
        inMantissa.copyTo(arrayInMantissa);
        int[] arrayInExponent = new int[INPUTSIZE * 2];
        Arrays.fill(arrayInExponent, (int) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 2];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inMantissa = arrayInMantissa[i * 2 + j];
                args.inExponent = arrayInExponent[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLdexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMantissa: ");
                        appendVariableToMessage(message, args.inMantissa);
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
        assertFalse("Incorrect output for checkLdexpFloat2Int2Float2" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkLdexpFloat3Int3Float3() {
        Allocation inMantissa = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x3fa3335el, false);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 3, 0xdcf384b3l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testLdexpFloat3Int3Float3(inMantissa, out);
            verifyResultsLdexpFloat3Int3Float3(inMantissa, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat3Int3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testLdexpFloat3Int3Float3(inMantissa, out);
            verifyResultsLdexpFloat3Int3Float3(inMantissa, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat3Int3Float3: " + e.toString());
        }
    }

    private void verifyResultsLdexpFloat3Int3Float3(Allocation inMantissa, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInMantissa = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInMantissa, (float) 42);
        inMantissa.copyTo(arrayInMantissa);
        int[] arrayInExponent = new int[INPUTSIZE * 4];
        Arrays.fill(arrayInExponent, (int) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inMantissa = arrayInMantissa[i * 4 + j];
                args.inExponent = arrayInExponent[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLdexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMantissa: ");
                        appendVariableToMessage(message, args.inMantissa);
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
        assertFalse("Incorrect output for checkLdexpFloat3Int3Float3" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkLdexpFloat4Int4Float4() {
        Allocation inMantissa = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x2f5d3e29l, false);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 4, 0xccad8f7el, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testLdexpFloat4Int4Float4(inMantissa, out);
            verifyResultsLdexpFloat4Int4Float4(inMantissa, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat4Int4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testLdexpFloat4Int4Float4(inMantissa, out);
            verifyResultsLdexpFloat4Int4Float4(inMantissa, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat4Int4Float4: " + e.toString());
        }
    }

    private void verifyResultsLdexpFloat4Int4Float4(Allocation inMantissa, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInMantissa = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInMantissa, (float) 42);
        inMantissa.copyTo(arrayInMantissa);
        int[] arrayInExponent = new int[INPUTSIZE * 4];
        Arrays.fill(arrayInExponent, (int) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inMantissa = arrayInMantissa[i * 4 + j];
                args.inExponent = arrayInExponent[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLdexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMantissa: ");
                        appendVariableToMessage(message, args.inMantissa);
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
        assertFalse("Incorrect output for checkLdexpFloat4Int4Float4" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkLdexpFloat2IntFloat2() {
        Allocation inMantissa = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x38bd38d1l, false);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 1, 0xd60d8a26l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testLdexpFloat2IntFloat2(inMantissa, out);
            verifyResultsLdexpFloat2IntFloat2(inMantissa, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat2IntFloat2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testLdexpFloat2IntFloat2(inMantissa, out);
            verifyResultsLdexpFloat2IntFloat2(inMantissa, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat2IntFloat2: " + e.toString());
        }
    }

    private void verifyResultsLdexpFloat2IntFloat2(Allocation inMantissa, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInMantissa = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInMantissa, (float) 42);
        inMantissa.copyTo(arrayInMantissa);
        int[] arrayInExponent = new int[INPUTSIZE * 1];
        Arrays.fill(arrayInExponent, (int) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 2];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inMantissa = arrayInMantissa[i * 2 + j];
                args.inExponent = arrayInExponent[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLdexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMantissa: ");
                        appendVariableToMessage(message, args.inMantissa);
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
        assertFalse("Incorrect output for checkLdexpFloat2IntFloat2" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkLdexpFloat3IntFloat3() {
        Allocation inMantissa = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x5150f83dl, false);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 1, 0xeea14992l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testLdexpFloat3IntFloat3(inMantissa, out);
            verifyResultsLdexpFloat3IntFloat3(inMantissa, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat3IntFloat3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testLdexpFloat3IntFloat3(inMantissa, out);
            verifyResultsLdexpFloat3IntFloat3(inMantissa, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat3IntFloat3: " + e.toString());
        }
    }

    private void verifyResultsLdexpFloat3IntFloat3(Allocation inMantissa, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInMantissa = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInMantissa, (float) 42);
        inMantissa.copyTo(arrayInMantissa);
        int[] arrayInExponent = new int[INPUTSIZE * 1];
        Arrays.fill(arrayInExponent, (int) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inMantissa = arrayInMantissa[i * 4 + j];
                args.inExponent = arrayInExponent[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLdexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMantissa: ");
                        appendVariableToMessage(message, args.inMantissa);
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
        assertFalse("Incorrect output for checkLdexpFloat3IntFloat3" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkLdexpFloat4IntFloat4() {
        Allocation inMantissa = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x69e4b7a9l, false);
        Allocation inExponent = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 1, 0x73508fel, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testLdexpFloat4IntFloat4(inMantissa, out);
            verifyResultsLdexpFloat4IntFloat4(inMantissa, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat4IntFloat4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testLdexpFloat4IntFloat4(inMantissa, out);
            verifyResultsLdexpFloat4IntFloat4(inMantissa, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLdexpFloat4IntFloat4: " + e.toString());
        }
    }

    private void verifyResultsLdexpFloat4IntFloat4(Allocation inMantissa, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInMantissa = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInMantissa, (float) 42);
        inMantissa.copyTo(arrayInMantissa);
        int[] arrayInExponent = new int[INPUTSIZE * 1];
        Arrays.fill(arrayInExponent, (int) 42);
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inMantissa = arrayInMantissa[i * 4 + j];
                args.inExponent = arrayInExponent[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLdexp(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inMantissa: ");
                        appendVariableToMessage(message, args.inMantissa);
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
        assertFalse("Incorrect output for checkLdexpFloat4IntFloat4" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public void testLdexp() {
        checkLdexpFloatIntFloat();
        checkLdexpFloat2Int2Float2();
        checkLdexpFloat3Int3Float3();
        checkLdexpFloat4Int4Float4();
        checkLdexpFloat2IntFloat2();
        checkLdexpFloat3IntFloat3();
        checkLdexpFloat4IntFloat4();
    }
}
