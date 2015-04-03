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

public class TestLength extends RSBaseCompute {

    private ScriptC_TestLength script;
    private ScriptC_TestLengthRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestLength(mRS);
        scriptRelaxed = new ScriptC_TestLengthRelaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float inV;
        public Target.Floaty out;
    }

    private void checkLengthFloatFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x8119352509f7cc9fl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testLengthFloatFloat(inV, out);
            verifyResultsLengthFloatFloat(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLengthFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testLengthFloatFloat(inV, out);
            verifyResultsLengthFloatFloat(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLengthFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsLengthFloatFloat(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsFloatFloat args = new ArgumentsFloatFloat();
            // Create the appropriate sized arrays in args
            // Fill args with the input values
            args.inV = arrayInV[i];
            Target target = new Target(relaxed);
            CoreMathVerifier.computeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(arrayOut[i])) {
                valid = false;
            }
            if (!valid) {
                StringBuilder message = new StringBuilder();
                message.append("Input inV: ");
                message.append(String.format("%14.8g {%8x} %15a",
                        arrayInV[i], Float.floatToRawIntBits(arrayInV[i]), arrayInV[i]));
                message.append("\n");
                message.append("Expected output out: ");
                message.append(args.out.toString());
                message.append("\n");
                message.append("Actual   output out: ");
                message.append(String.format("%14.8g {%8x} %15a",
                        arrayOut[i], Float.floatToRawIntBits(arrayOut[i]), arrayOut[i]));
                if (!args.out.couldBe(arrayOut[i])) {
                    message.append(" FAIL");
                }
                message.append("\n");
                assertTrue("Incorrect output for checkLengthFloatFloat" +
                        (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
            }
        }
    }

    public class ArgumentsFloatNFloat {
        public float[] inV;
        public Target.Floaty out;
    }

    private void checkLengthFloat2Float() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xaf3b0f345dd9595dl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testLengthFloat2Float(inV, out);
            verifyResultsLengthFloat2Float(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLengthFloat2Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testLengthFloat2Float(inV, out);
            verifyResultsLengthFloat2Float(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLengthFloat2Float: " + e.toString());
        }
    }

    private void verifyResultsLengthFloat2Float(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsFloatNFloat args = new ArgumentsFloatNFloat();
            // Create the appropriate sized arrays in args
            args.inV = new float[2];
            // Fill args with the input values
            for (int j = 0; j < 2 ; j++) {
                args.inV[j] = arrayInV[i * 2 + j];
            }
            Target target = new Target(relaxed);
            CoreMathVerifier.computeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(arrayOut[i])) {
                valid = false;
            }
            if (!valid) {
                StringBuilder message = new StringBuilder();
                for (int j = 0; j < 2 ; j++) {
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayInV[i * 2 + j], Float.floatToRawIntBits(arrayInV[i * 2 + j]), arrayInV[i * 2 + j]));
                    message.append("\n");
                }
                message.append("Expected output out: ");
                message.append(args.out.toString());
                message.append("\n");
                message.append("Actual   output out: ");
                message.append(String.format("%14.8g {%8x} %15a",
                        arrayOut[i], Float.floatToRawIntBits(arrayOut[i]), arrayOut[i]));
                if (!args.out.couldBe(arrayOut[i])) {
                    message.append(" FAIL");
                }
                message.append("\n");
                assertTrue("Incorrect output for checkLengthFloat2Float" +
                        (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
            }
        }
    }

    private void checkLengthFloat3Float() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xaf3b19d5bcdfe7bel, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testLengthFloat3Float(inV, out);
            verifyResultsLengthFloat3Float(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLengthFloat3Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testLengthFloat3Float(inV, out);
            verifyResultsLengthFloat3Float(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLengthFloat3Float: " + e.toString());
        }
    }

    private void verifyResultsLengthFloat3Float(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsFloatNFloat args = new ArgumentsFloatNFloat();
            // Create the appropriate sized arrays in args
            args.inV = new float[3];
            // Fill args with the input values
            for (int j = 0; j < 3 ; j++) {
                args.inV[j] = arrayInV[i * 4 + j];
            }
            Target target = new Target(relaxed);
            CoreMathVerifier.computeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(arrayOut[i])) {
                valid = false;
            }
            if (!valid) {
                StringBuilder message = new StringBuilder();
                for (int j = 0; j < 3 ; j++) {
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayInV[i * 4 + j], Float.floatToRawIntBits(arrayInV[i * 4 + j]), arrayInV[i * 4 + j]));
                    message.append("\n");
                }
                message.append("Expected output out: ");
                message.append(args.out.toString());
                message.append("\n");
                message.append("Actual   output out: ");
                message.append(String.format("%14.8g {%8x} %15a",
                        arrayOut[i], Float.floatToRawIntBits(arrayOut[i]), arrayOut[i]));
                if (!args.out.couldBe(arrayOut[i])) {
                    message.append(" FAIL");
                }
                message.append("\n");
                assertTrue("Incorrect output for checkLengthFloat3Float" +
                        (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
            }
        }
    }

    private void checkLengthFloat4Float() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xaf3b24771be6761fl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testLengthFloat4Float(inV, out);
            verifyResultsLengthFloat4Float(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLengthFloat4Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testLengthFloat4Float(inV, out);
            verifyResultsLengthFloat4Float(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLengthFloat4Float: " + e.toString());
        }
    }

    private void verifyResultsLengthFloat4Float(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsFloatNFloat args = new ArgumentsFloatNFloat();
            // Create the appropriate sized arrays in args
            args.inV = new float[4];
            // Fill args with the input values
            for (int j = 0; j < 4 ; j++) {
                args.inV[j] = arrayInV[i * 4 + j];
            }
            Target target = new Target(relaxed);
            CoreMathVerifier.computeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(arrayOut[i])) {
                valid = false;
            }
            if (!valid) {
                StringBuilder message = new StringBuilder();
                for (int j = 0; j < 4 ; j++) {
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayInV[i * 4 + j], Float.floatToRawIntBits(arrayInV[i * 4 + j]), arrayInV[i * 4 + j]));
                    message.append("\n");
                }
                message.append("Expected output out: ");
                message.append(args.out.toString());
                message.append("\n");
                message.append("Actual   output out: ");
                message.append(String.format("%14.8g {%8x} %15a",
                        arrayOut[i], Float.floatToRawIntBits(arrayOut[i]), arrayOut[i]));
                if (!args.out.couldBe(arrayOut[i])) {
                    message.append(" FAIL");
                }
                message.append("\n");
                assertTrue("Incorrect output for checkLengthFloat4Float" +
                        (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
            }
        }
    }

    public void testLength() {
        checkLengthFloatFloat();
        checkLengthFloat2Float();
        checkLengthFloat3Float();
        checkLengthFloat4Float();
    }
}
