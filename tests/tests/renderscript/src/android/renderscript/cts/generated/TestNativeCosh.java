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

public class TestNativeCosh extends RSBaseCompute {

    private ScriptC_TestNativeCosh script;
    private ScriptC_TestNativeCoshRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestNativeCosh(mRS);
        scriptRelaxed = new ScriptC_TestNativeCoshRelaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float inV;
        public Target.Floaty out;
    }

    private void checkNativeCoshFloatFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x7bb5784aa43ffcf5l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testNativeCoshFloatFloat(inV, out);
            verifyResultsNativeCoshFloatFloat(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeCoshFloatFloat(inV, out);
            verifyResultsNativeCoshFloatFloat(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsNativeCoshFloatFloat(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i];
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.FLOAT, relaxed);
                CoreMathVerifier.computeNativeCosh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
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
        assertFalse("Incorrect output for checkNativeCoshFloatFloat" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeCoshFloat2Float2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x818c886316a44671l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.forEach_testNativeCoshFloat2Float2(inV, out);
            verifyResultsNativeCoshFloat2Float2(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshFloat2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testNativeCoshFloat2Float2(inV, out);
            verifyResultsNativeCoshFloat2Float2(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshFloat2Float2: " + e.toString());
        }
    }

    private void verifyResultsNativeCoshFloat2Float2(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 2];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.FLOAT, relaxed);
                CoreMathVerifier.computeNativeCosh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
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
        assertFalse("Incorrect output for checkNativeCoshFloat2Float2" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeCoshFloat3Float3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x818e517e0cbf674fl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.forEach_testNativeCoshFloat3Float3(inV, out);
            verifyResultsNativeCoshFloat3Float3(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshFloat3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testNativeCoshFloat3Float3(inV, out);
            verifyResultsNativeCoshFloat3Float3(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshFloat3Float3: " + e.toString());
        }
    }

    private void verifyResultsNativeCoshFloat3Float3(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.FLOAT, relaxed);
                CoreMathVerifier.computeNativeCosh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
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
        assertFalse("Incorrect output for checkNativeCoshFloat3Float3" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeCoshFloat4Float4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x81901a9902da882dl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.forEach_testNativeCoshFloat4Float4(inV, out);
            verifyResultsNativeCoshFloat4Float4(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshFloat4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testNativeCoshFloat4Float4(inV, out);
            verifyResultsNativeCoshFloat4Float4(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshFloat4Float4: " + e.toString());
        }
    }

    private void verifyResultsNativeCoshFloat4Float4(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.FLOAT, relaxed);
                CoreMathVerifier.computeNativeCosh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
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
        assertFalse("Incorrect output for checkNativeCoshFloat4Float4" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public class ArgumentsHalfHalf {
        public short inV;
        public double inVDouble;
        public Target.Floaty out;
    }

    private void checkNativeCoshHalfHalf() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_16, 1, 0x41862c01f3df9f87l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            script.forEach_testNativeCoshHalfHalf(inV, out);
            verifyResultsNativeCoshHalfHalf(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshHalfHalf: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeCoshHalfHalf(inV, out);
            verifyResultsNativeCoshHalfHalf(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshHalfHalf: " + e.toString());
        }
    }

    private void verifyResultsNativeCoshHalfHalf(Allocation inV, Allocation out, boolean relaxed) {
        short[] arrayInV = new short[INPUTSIZE * 1];
        Arrays.fill(arrayInV, (short) 42);
        inV.copyTo(arrayInV);
        short[] arrayOut = new short[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (short) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsHalfHalf args = new ArgumentsHalfHalf();
                args.inV = arrayInV[i];
                args.inVDouble = Float16Utils.convertFloat16ToDouble(args.inV);
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.HALF, relaxed);
                CoreMathVerifier.computeNativeCosh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i * 1 + j]))) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
                        message.append("\n");
                        message.append("Expected output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append("Actual   output out: ");
                        appendVariableToMessage(message, arrayOut[i * 1 + j]);
                        message.append("\n");
                        message.append("Actual   output out (in double): ");
                        appendVariableToMessage(message, Float16Utils.convertFloat16ToDouble(arrayOut[i * 1 + j]));
                        if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i * 1 + j]))) {
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
        assertFalse("Incorrect output for checkNativeCoshHalfHalf" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeCoshHalf2Half2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_16, 2, 0x4212e9fe115e4431l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 2), INPUTSIZE);
            script.forEach_testNativeCoshHalf2Half2(inV, out);
            verifyResultsNativeCoshHalf2Half2(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshHalf2Half2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 2), INPUTSIZE);
            scriptRelaxed.forEach_testNativeCoshHalf2Half2(inV, out);
            verifyResultsNativeCoshHalf2Half2(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshHalf2Half2: " + e.toString());
        }
    }

    private void verifyResultsNativeCoshHalf2Half2(Allocation inV, Allocation out, boolean relaxed) {
        short[] arrayInV = new short[INPUTSIZE * 2];
        Arrays.fill(arrayInV, (short) 42);
        inV.copyTo(arrayInV);
        short[] arrayOut = new short[INPUTSIZE * 2];
        Arrays.fill(arrayOut, (short) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsHalfHalf args = new ArgumentsHalfHalf();
                args.inV = arrayInV[i * 2 + j];
                args.inVDouble = Float16Utils.convertFloat16ToDouble(args.inV);
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.HALF, relaxed);
                CoreMathVerifier.computeNativeCosh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i * 2 + j]))) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
                        message.append("\n");
                        message.append("Expected output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append("Actual   output out: ");
                        appendVariableToMessage(message, arrayOut[i * 2 + j]);
                        message.append("\n");
                        message.append("Actual   output out (in double): ");
                        appendVariableToMessage(message, Float16Utils.convertFloat16ToDouble(arrayOut[i * 2 + j]));
                        if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i * 2 + j]))) {
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
        assertFalse("Incorrect output for checkNativeCoshHalf2Half2" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeCoshHalf3Half3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_16, 3, 0x4212f49f70660925l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 3), INPUTSIZE);
            script.forEach_testNativeCoshHalf3Half3(inV, out);
            verifyResultsNativeCoshHalf3Half3(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshHalf3Half3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 3), INPUTSIZE);
            scriptRelaxed.forEach_testNativeCoshHalf3Half3(inV, out);
            verifyResultsNativeCoshHalf3Half3(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshHalf3Half3: " + e.toString());
        }
    }

    private void verifyResultsNativeCoshHalf3Half3(Allocation inV, Allocation out, boolean relaxed) {
        short[] arrayInV = new short[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (short) 42);
        inV.copyTo(arrayInV);
        short[] arrayOut = new short[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (short) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsHalfHalf args = new ArgumentsHalfHalf();
                args.inV = arrayInV[i * 4 + j];
                args.inVDouble = Float16Utils.convertFloat16ToDouble(args.inV);
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.HALF, relaxed);
                CoreMathVerifier.computeNativeCosh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i * 4 + j]))) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
                        message.append("\n");
                        message.append("Expected output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append("Actual   output out: ");
                        appendVariableToMessage(message, arrayOut[i * 4 + j]);
                        message.append("\n");
                        message.append("Actual   output out (in double): ");
                        appendVariableToMessage(message, Float16Utils.convertFloat16ToDouble(arrayOut[i * 4 + j]));
                        if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i * 4 + j]))) {
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
        assertFalse("Incorrect output for checkNativeCoshHalf3Half3" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeCoshHalf4Half4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_16, 4, 0x4212ff40cf6dce19l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 4), INPUTSIZE);
            script.forEach_testNativeCoshHalf4Half4(inV, out);
            verifyResultsNativeCoshHalf4Half4(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshHalf4Half4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 4), INPUTSIZE);
            scriptRelaxed.forEach_testNativeCoshHalf4Half4(inV, out);
            verifyResultsNativeCoshHalf4Half4(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeCoshHalf4Half4: " + e.toString());
        }
    }

    private void verifyResultsNativeCoshHalf4Half4(Allocation inV, Allocation out, boolean relaxed) {
        short[] arrayInV = new short[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (short) 42);
        inV.copyTo(arrayInV);
        short[] arrayOut = new short[INPUTSIZE * 4];
        Arrays.fill(arrayOut, (short) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsHalfHalf args = new ArgumentsHalfHalf();
                args.inV = arrayInV[i * 4 + j];
                args.inVDouble = Float16Utils.convertFloat16ToDouble(args.inV);
                // Figure out what the outputs should have been.
                Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.HALF, relaxed);
                CoreMathVerifier.computeNativeCosh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i * 4 + j]))) {
                    valid = false;
                }
                if (!valid) {
                    if (!errorFound) {
                        errorFound = true;
                        message.append("Input inV: ");
                        appendVariableToMessage(message, args.inV);
                        message.append("\n");
                        message.append("Expected output out: ");
                        appendVariableToMessage(message, args.out);
                        message.append("\n");
                        message.append("Actual   output out: ");
                        appendVariableToMessage(message, arrayOut[i * 4 + j]);
                        message.append("\n");
                        message.append("Actual   output out (in double): ");
                        appendVariableToMessage(message, Float16Utils.convertFloat16ToDouble(arrayOut[i * 4 + j]));
                        if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i * 4 + j]))) {
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
        assertFalse("Incorrect output for checkNativeCoshHalf4Half4" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public void testNativeCosh() {
        checkNativeCoshFloatFloat();
        checkNativeCoshFloat2Float2();
        checkNativeCoshFloat3Float3();
        checkNativeCoshFloat4Float4();
        checkNativeCoshHalfHalf();
        checkNativeCoshHalf2Half2();
        checkNativeCoshHalf3Half3();
        checkNativeCoshHalf4Half4();
    }
}
