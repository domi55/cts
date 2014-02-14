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

public class TestRadians extends RSBaseCompute {

    private ScriptC_TestRadians script;
    private ScriptC_TestRadiansRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestRadians(mRS);
        scriptRelaxed = new ScriptC_TestRadiansRelaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float inValue;
        public float out;

        public int ulf;
        public int ulfRelaxed;
    }

    private void checkRadiansFloatFloat() {
        Allocation inValue = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xaa72f23429911decL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testRadiansFloatFloat(inValue, out);
            verifyResultsRadiansFloatFloat(inValue, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRadiansFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testRadiansFloatFloat(inValue, out);
            verifyResultsRadiansFloatFloat(inValue, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRadiansFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsRadiansFloatFloat(Allocation inValue, Allocation out, boolean relaxed) {
        float[] arrayInValue = new float[INPUTSIZE * 1];
        inValue.copyTo(arrayInValue);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inValue = arrayInValue[i];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeRadians(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                neededUlf = (int) (Math.abs(args.out - arrayOut[i * 1 + j]) / Math.ulp(args.out) + 0.5);
                if (neededUlf > ulf) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input inValue: %x %.16f", Float.floatToRawIntBits(args.inValue), args.inValue));
                    message.append("\n");
                    message.append(String.format("Expected output out: %x %.16f", Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %x %.16f", Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 1 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkRadiansFloatFloat" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkRadiansFloat2Float2() {
        Allocation inValue = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xb28bd93e3e0b3578L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.forEach_testRadiansFloat2Float2(inValue, out);
            verifyResultsRadiansFloat2Float2(inValue, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRadiansFloat2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testRadiansFloat2Float2(inValue, out);
            verifyResultsRadiansFloat2Float2(inValue, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRadiansFloat2Float2: " + e.toString());
        }
    }

    private void verifyResultsRadiansFloat2Float2(Allocation inValue, Allocation out, boolean relaxed) {
        float[] arrayInValue = new float[INPUTSIZE * 2];
        inValue.copyTo(arrayInValue);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inValue = arrayInValue[i * 2 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeRadians(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                neededUlf = (int) (Math.abs(args.out - arrayOut[i * 2 + j]) / Math.ulp(args.out) + 0.5);
                if (neededUlf > ulf) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input inValue: %x %.16f", Float.floatToRawIntBits(args.inValue), args.inValue));
                    message.append("\n");
                    message.append(String.format("Expected output out: %x %.16f", Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %x %.16f", Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 2 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkRadiansFloat2Float2" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkRadiansFloat3Float3() {
        Allocation inValue = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xd8404ec587495af6L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.forEach_testRadiansFloat3Float3(inValue, out);
            verifyResultsRadiansFloat3Float3(inValue, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRadiansFloat3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testRadiansFloat3Float3(inValue, out);
            verifyResultsRadiansFloat3Float3(inValue, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRadiansFloat3Float3: " + e.toString());
        }
    }

    private void verifyResultsRadiansFloat3Float3(Allocation inValue, Allocation out, boolean relaxed) {
        float[] arrayInValue = new float[INPUTSIZE * 4];
        inValue.copyTo(arrayInValue);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inValue = arrayInValue[i * 4 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeRadians(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                if (neededUlf > ulf) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input inValue: %x %.16f", Float.floatToRawIntBits(args.inValue), args.inValue));
                    message.append("\n");
                    message.append(String.format("Expected output out: %x %.16f", Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %x %.16f", Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkRadiansFloat3Float3" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkRadiansFloat4Float4() {
        Allocation inValue = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xfdf4c44cd0878074L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.forEach_testRadiansFloat4Float4(inValue, out);
            verifyResultsRadiansFloat4Float4(inValue, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRadiansFloat4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testRadiansFloat4Float4(inValue, out);
            verifyResultsRadiansFloat4Float4(inValue, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRadiansFloat4Float4: " + e.toString());
        }
    }

    private void verifyResultsRadiansFloat4Float4(Allocation inValue, Allocation out, boolean relaxed) {
        float[] arrayInValue = new float[INPUTSIZE * 4];
        inValue.copyTo(arrayInValue);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inValue = arrayInValue[i * 4 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeRadians(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                if (neededUlf > ulf) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input inValue: %x %.16f", Float.floatToRawIntBits(args.inValue), args.inValue));
                    message.append("\n");
                    message.append(String.format("Expected output out: %x %.16f", Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %x %.16f", Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkRadiansFloat4Float4" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testRadians() {
        checkRadiansFloatFloat();
        checkRadiansFloat2Float2();
        checkRadiansFloat3Float3();
        checkRadiansFloat4Float4();
    }
}
