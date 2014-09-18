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

public class TestNativeAtan2pi extends RSBaseCompute {

    private ScriptC_TestNativeAtan2pi script;
    private ScriptC_TestNativeAtan2piRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestNativeAtan2pi(mRS);
        scriptRelaxed = new ScriptC_TestNativeAtan2piRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloat {
        public float inY;
        public float inX;
        public Target.Floaty out;
    }

    private void checkNativeAtan2piFloatFloatFloat() {
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x39c4f8fd35fc5dc8l, false);
        Allocation inX = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x39c4f8fd35fc5dc7l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInX(inX);
            script.forEach_testNativeAtan2piFloatFloatFloat(inY, out);
            verifyResultsNativeAtan2piFloatFloatFloat(inY, inX, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2piFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInX(inX);
            scriptRelaxed.forEach_testNativeAtan2piFloatFloatFloat(inY, out);
            verifyResultsNativeAtan2piFloatFloatFloat(inY, inX, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2piFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsNativeAtan2piFloatFloatFloat(Allocation inY, Allocation inX, Allocation out, boolean relaxed) {
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
                CoreMathVerifier.computeNativeAtan2pi(args, target);
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
                    assertTrue("Incorrect output for checkNativeAtan2piFloatFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeAtan2piFloat2Float2Float2() {
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x6aff980c8d47a3e2l, false);
        Allocation inX = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x6aff980c8d47a3e1l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInX(inX);
            script.forEach_testNativeAtan2piFloat2Float2Float2(inY, out);
            verifyResultsNativeAtan2piFloat2Float2Float2(inY, inX, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2piFloat2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInX(inX);
            scriptRelaxed.forEach_testNativeAtan2piFloat2Float2Float2(inY, out);
            verifyResultsNativeAtan2piFloat2Float2Float2(inY, inX, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2piFloat2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsNativeAtan2piFloat2Float2Float2(Allocation inY, Allocation inX, Allocation out, boolean relaxed) {
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
                CoreMathVerifier.computeNativeAtan2pi(args, target);
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
                    assertTrue("Incorrect output for checkNativeAtan2piFloat2Float2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeAtan2piFloat3Float3Float3() {
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xbf64762c8f25a583l, false);
        Allocation inX = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xbf64762c8f25a582l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInX(inX);
            script.forEach_testNativeAtan2piFloat3Float3Float3(inY, out);
            verifyResultsNativeAtan2piFloat3Float3Float3(inY, inX, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2piFloat3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInX(inX);
            scriptRelaxed.forEach_testNativeAtan2piFloat3Float3Float3(inY, out);
            verifyResultsNativeAtan2piFloat3Float3Float3(inY, inX, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2piFloat3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsNativeAtan2piFloat3Float3Float3(Allocation inY, Allocation inX, Allocation out, boolean relaxed) {
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
                CoreMathVerifier.computeNativeAtan2pi(args, target);
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
                    assertTrue("Incorrect output for checkNativeAtan2piFloat3Float3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeAtan2piFloat4Float4Float4() {
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x13c9544c9103a724l, false);
        Allocation inX = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x13c9544c9103a723l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInX(inX);
            script.forEach_testNativeAtan2piFloat4Float4Float4(inY, out);
            verifyResultsNativeAtan2piFloat4Float4Float4(inY, inX, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2piFloat4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInX(inX);
            scriptRelaxed.forEach_testNativeAtan2piFloat4Float4Float4(inY, out);
            verifyResultsNativeAtan2piFloat4Float4Float4(inY, inX, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeAtan2piFloat4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsNativeAtan2piFloat4Float4Float4(Allocation inY, Allocation inX, Allocation out, boolean relaxed) {
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
                CoreMathVerifier.computeNativeAtan2pi(args, target);
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
                    assertTrue("Incorrect output for checkNativeAtan2piFloat4Float4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testNativeAtan2pi() {
        checkNativeAtan2piFloatFloatFloat();
        checkNativeAtan2piFloat2Float2Float2();
        checkNativeAtan2piFloat3Float3Float3();
        checkNativeAtan2piFloat4Float4Float4();
    }
}
