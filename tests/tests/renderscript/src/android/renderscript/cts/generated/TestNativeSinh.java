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

public class TestNativeSinh extends RSBaseCompute {

    private ScriptC_TestNativeSinh script;
    private ScriptC_TestNativeSinhRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestNativeSinh(mRS);
        scriptRelaxed = new ScriptC_TestNativeSinhRelaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float inV;
        public Target.Floaty out;
    }

    private void checkNativeSinhFloatFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xa11cf844515a0f86l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testNativeSinhFloatFloat(inV, out);
            verifyResultsNativeSinhFloatFloat(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeSinhFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeSinhFloatFloat(inV, out);
            verifyResultsNativeSinhFloatFloat(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeSinhFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsNativeSinhFloatFloat(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeSinh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
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
                    assertTrue("Incorrect output for checkNativeSinhFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeSinhFloat2Float2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xaa17dab657f45fbal, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.forEach_testNativeSinhFloat2Float2(inV, out);
            verifyResultsNativeSinhFloat2Float2(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeSinhFloat2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testNativeSinhFloat2Float2(inV, out);
            verifyResultsNativeSinhFloat2Float2(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeSinhFloat2Float2: " + e.toString());
        }
    }

    private void verifyResultsNativeSinhFloat2Float2(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeSinh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
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
                    assertTrue("Incorrect output for checkNativeSinhFloat2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeSinhFloat3Float3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xaa19a3d14e0f8098l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.forEach_testNativeSinhFloat3Float3(inV, out);
            verifyResultsNativeSinhFloat3Float3(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeSinhFloat3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testNativeSinhFloat3Float3(inV, out);
            verifyResultsNativeSinhFloat3Float3(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeSinhFloat3Float3: " + e.toString());
        }
    }

    private void verifyResultsNativeSinhFloat3Float3(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeSinh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
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
                    assertTrue("Incorrect output for checkNativeSinhFloat3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeSinhFloat4Float4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xaa1b6cec442aa176l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.forEach_testNativeSinhFloat4Float4(inV, out);
            verifyResultsNativeSinhFloat4Float4(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeSinhFloat4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testNativeSinhFloat4Float4(inV, out);
            verifyResultsNativeSinhFloat4Float4(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeSinhFloat4Float4: " + e.toString());
        }
    }

    private void verifyResultsNativeSinhFloat4Float4(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeSinh(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
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
                    assertTrue("Incorrect output for checkNativeSinhFloat4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testNativeSinh() {
        checkNativeSinhFloatFloat();
        checkNativeSinhFloat2Float2();
        checkNativeSinhFloat3Float3();
        checkNativeSinhFloat4Float4();
    }
}
