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

public class TestLog2 extends RSBaseCompute {

    private ScriptC_TestLog2 script;
    private ScriptC_TestLog2Relaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestLog2(mRS);
        scriptRelaxed = new ScriptC_TestLog2Relaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float in;
        public Floaty out;
    }

    private void checkLog2FloatFloat() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x390bea9a53d34bfl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testLog2FloatFloat(in, out);
            verifyResultsLog2FloatFloat(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog2FloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testLog2FloatFloat(in, out);
            verifyResultsLog2FloatFloat(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog2FloatFloat: " + e.toString());
        }
    }

    private void verifyResultsLog2FloatFloat(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 1];
        in.copyTo(arrayIn);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.in = arrayIn[i];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computeLog2(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input in: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g %8x %15a",
                            arrayOut[i * 1 + j], Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLog2FloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLog2Float2Float2() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xc0703946284a72a3l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.forEach_testLog2Float2Float2(in, out);
            verifyResultsLog2Float2Float2(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testLog2Float2Float2(in, out);
            verifyResultsLog2Float2Float2(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsLog2Float2Float2(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 2];
        in.copyTo(arrayIn);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.in = arrayIn[i * 2 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computeLog2(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input in: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g %8x %15a",
                            arrayOut[i * 2 + j], Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLog2Float2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLog2Float3Float3() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xc07043e78751083dl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.forEach_testLog2Float3Float3(in, out);
            verifyResultsLog2Float3Float3(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog2Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testLog2Float3Float3(in, out);
            verifyResultsLog2Float3Float3(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog2Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsLog2Float3Float3(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 4];
        in.copyTo(arrayIn);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.in = arrayIn[i * 4 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computeLog2(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input in: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g %8x %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLog2Float3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLog2Float4Float4() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xc0704e88e6579dd7l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.forEach_testLog2Float4Float4(in, out);
            verifyResultsLog2Float4Float4(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog2Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testLog2Float4Float4(in, out);
            verifyResultsLog2Float4Float4(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog2Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsLog2Float4Float4(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 4];
        in.copyTo(arrayIn);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.in = arrayIn[i * 4 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computeLog2(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input in: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g %8x %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLog2Float4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testLog2() {
        checkLog2FloatFloat();
        checkLog2Float2Float2();
        checkLog2Float3Float3();
        checkLog2Float4Float4();
    }
}
