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

public class TestNativeAtan2 extends RSBaseCompute {

    private ScriptC_TestNativeAtan2 script;
    private ScriptC_TestNativeAtan2Relaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestNativeAtan2(mRS);
        scriptRelaxed = new ScriptC_TestNativeAtan2Relaxed(mRS);
    }

    public class ArgumentsFloatFloatFloat {
        public float inY;
        public float inX;
        public Target.Floaty out;
    }

    private void checkNativeAtan2FloatFloatFloat() {
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x4ecf7d3b9e2a276fl, false);
        Allocation inX = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x4ecf7d3b9e2a276el, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInX(inX);
            script.forEach_testNativeAtan2FloatFloatFloat(inY, out);
            verifyResultsNativeAtan2FloatFloatFloat(inY, inX, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2FloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInX(inX);
            scriptRelaxed.forEach_testNativeAtan2FloatFloatFloat(inY, out);
            verifyResultsNativeAtan2FloatFloatFloat(inY, inX, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2FloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsNativeAtan2FloatFloatFloat(Allocation inY, Allocation inX, Allocation out, boolean relaxed) {
        float[] arrayInY = new float[INPUTSIZE * 1];
        inY.copyTo(arrayInY);
        float[] arrayInX = new float[INPUTSIZE * 1];
        inX.copyTo(arrayInX);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inY = arrayInY[i];
                args.inX = arrayInX[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeAtan2(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j], 0.0005)) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inY: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inY, Float.floatToRawIntBits(args.inY), args.inY));
                    message.append("\n");
                    message.append("Input inX: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inX, Float.floatToRawIntBits(args.inX), args.inX));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 1 + j], Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    if (!args.out.couldBe(arrayOut[i * 1 + j], 0.0005)) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativeAtan2FloatFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeAtan2Float2Float2Float2() {
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x3c2d1a09d1c2a8c7l, false);
        Allocation inX = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x3c2d1a09d1c2a8c6l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInX(inX);
            script.forEach_testNativeAtan2Float2Float2Float2(inY, out);
            verifyResultsNativeAtan2Float2Float2Float2(inY, inX, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2Float2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInX(inX);
            scriptRelaxed.forEach_testNativeAtan2Float2Float2Float2(inY, out);
            verifyResultsNativeAtan2Float2Float2Float2(inY, inX, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2Float2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsNativeAtan2Float2Float2Float2(Allocation inY, Allocation inX, Allocation out, boolean relaxed) {
        float[] arrayInY = new float[INPUTSIZE * 2];
        inY.copyTo(arrayInY);
        float[] arrayInX = new float[INPUTSIZE * 2];
        inX.copyTo(arrayInX);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inY = arrayInY[i * 2 + j];
                args.inX = arrayInX[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeAtan2(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j], 0.0005)) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inY: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inY, Float.floatToRawIntBits(args.inY), args.inY));
                    message.append("\n");
                    message.append("Input inX: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inX, Float.floatToRawIntBits(args.inX), args.inX));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 2 + j], Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    if (!args.out.couldBe(arrayOut[i * 2 + j], 0.0005)) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativeAtan2Float2Float2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeAtan2Float3Float3Float3() {
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x9091f829d3a0aa68l, false);
        Allocation inX = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x9091f829d3a0aa67l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInX(inX);
            script.forEach_testNativeAtan2Float3Float3Float3(inY, out);
            verifyResultsNativeAtan2Float3Float3Float3(inY, inX, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2Float3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInX(inX);
            scriptRelaxed.forEach_testNativeAtan2Float3Float3Float3(inY, out);
            verifyResultsNativeAtan2Float3Float3Float3(inY, inX, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2Float3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsNativeAtan2Float3Float3Float3(Allocation inY, Allocation inX, Allocation out, boolean relaxed) {
        float[] arrayInY = new float[INPUTSIZE * 4];
        inY.copyTo(arrayInY);
        float[] arrayInX = new float[INPUTSIZE * 4];
        inX.copyTo(arrayInX);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inY = arrayInY[i * 4 + j];
                args.inX = arrayInX[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeAtan2(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j], 0.0005)) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inY: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inY, Float.floatToRawIntBits(args.inY), args.inY));
                    message.append("\n");
                    message.append("Input inX: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inX, Float.floatToRawIntBits(args.inX), args.inX));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j], 0.0005)) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativeAtan2Float3Float3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeAtan2Float4Float4Float4() {
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xe4f6d649d57eac09l, false);
        Allocation inX = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xe4f6d649d57eac08l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInX(inX);
            script.forEach_testNativeAtan2Float4Float4Float4(inY, out);
            verifyResultsNativeAtan2Float4Float4Float4(inY, inX, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2Float4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInX(inX);
            scriptRelaxed.forEach_testNativeAtan2Float4Float4Float4(inY, out);
            verifyResultsNativeAtan2Float4Float4Float4(inY, inX, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2Float4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsNativeAtan2Float4Float4Float4(Allocation inY, Allocation inX, Allocation out, boolean relaxed) {
        float[] arrayInY = new float[INPUTSIZE * 4];
        inY.copyTo(arrayInY);
        float[] arrayInX = new float[INPUTSIZE * 4];
        inX.copyTo(arrayInX);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inY = arrayInY[i * 4 + j];
                args.inX = arrayInX[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeAtan2(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j], 0.0005)) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inY: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inY, Float.floatToRawIntBits(args.inY), args.inY));
                    message.append("\n");
                    message.append("Input inX: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inX, Float.floatToRawIntBits(args.inX), args.inX));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j], 0.0005)) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativeAtan2Float4Float4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testNativeAtan2() {
        checkNativeAtan2FloatFloatFloat();
        checkNativeAtan2Float2Float2Float2();
        checkNativeAtan2Float3Float3Float3();
        checkNativeAtan2Float4Float4Float4();
    }
}
