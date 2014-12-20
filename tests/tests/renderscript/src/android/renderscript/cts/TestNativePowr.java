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

public class TestNativePowr extends RSBaseCompute {

    private ScriptC_TestNativePowr script;
    private ScriptC_TestNativePowrRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestNativePowr(mRS);
        scriptRelaxed = new ScriptC_TestNativePowrRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloat {
        public float inBase;
        public float inExponent;
        public Target.Floaty out;
    }

    private void checkNativePowrFloatFloatFloat() {
        Allocation inBase = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x22637077834d1839l, 0, 256);
        Allocation inExponent = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xef7b0ab4f9181f0fl, -15, 15);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testNativePowrFloatFloatFloat(inBase, out);
            verifyResultsNativePowrFloatFloatFloat(inBase, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativePowrFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testNativePowrFloatFloatFloat(inBase, out);
            verifyResultsNativePowrFloatFloatFloat(inBase, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativePowrFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsNativePowrFloatFloatFloat(Allocation inBase, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInBase = new float[INPUTSIZE * 1];
        inBase.copyTo(arrayInBase);
        float[] arrayInExponent = new float[INPUTSIZE * 1];
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inBase = arrayInBase[i];
                args.inExponent = arrayInExponent[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativePowr(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inBase: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inBase, Float.floatToRawIntBits(args.inBase), args.inBase));
                    message.append("\n");
                    message.append("Input inExponent: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inExponent, Float.floatToRawIntBits(args.inExponent), args.inExponent));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 1 + j], Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativePowrFloatFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativePowrFloat2Float2Float2() {
        Allocation inBase = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x47afceb7283b11a5l, 0, 256);
        Allocation inExponent = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x65ac261bb67d4abbl, -15, 15);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testNativePowrFloat2Float2Float2(inBase, out);
            verifyResultsNativePowrFloat2Float2Float2(inBase, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativePowrFloat2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testNativePowrFloat2Float2Float2(inBase, out);
            verifyResultsNativePowrFloat2Float2Float2(inBase, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativePowrFloat2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsNativePowrFloat2Float2Float2(Allocation inBase, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInBase = new float[INPUTSIZE * 2];
        inBase.copyTo(arrayInBase);
        float[] arrayInExponent = new float[INPUTSIZE * 2];
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inBase = arrayInBase[i * 2 + j];
                args.inExponent = arrayInExponent[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativePowr(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inBase: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inBase, Float.floatToRawIntBits(args.inBase), args.inBase));
                    message.append("\n");
                    message.append("Input inExponent: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inExponent, Float.floatToRawIntBits(args.inExponent), args.inExponent));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 2 + j], Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativePowrFloat2Float2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativePowrFloat3Float3Float3() {
        Allocation inBase = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xe2961d5b10aef718l, 0, 256);
        Allocation inExponent = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x411aa11d0d9fcd3el, -15, 15);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testNativePowrFloat3Float3Float3(inBase, out);
            verifyResultsNativePowrFloat3Float3Float3(inBase, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativePowrFloat3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testNativePowrFloat3Float3Float3(inBase, out);
            verifyResultsNativePowrFloat3Float3Float3(inBase, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativePowrFloat3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsNativePowrFloat3Float3Float3(Allocation inBase, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInBase = new float[INPUTSIZE * 4];
        inBase.copyTo(arrayInBase);
        float[] arrayInExponent = new float[INPUTSIZE * 4];
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inBase = arrayInBase[i * 4 + j];
                args.inExponent = arrayInExponent[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativePowr(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inBase: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inBase, Float.floatToRawIntBits(args.inBase), args.inBase));
                    message.append("\n");
                    message.append("Input inExponent: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inExponent, Float.floatToRawIntBits(args.inExponent), args.inExponent));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativePowrFloat3Float3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativePowrFloat4Float4Float4() {
        Allocation inBase = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x7d7c6bfef922dc8bl, 0, 256);
        Allocation inExponent = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x1c891c1e64c24fc1l, -15, 15);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInExponent(inExponent);
            script.forEach_testNativePowrFloat4Float4Float4(inBase, out);
            verifyResultsNativePowrFloat4Float4Float4(inBase, inExponent, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativePowrFloat4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInExponent(inExponent);
            scriptRelaxed.forEach_testNativePowrFloat4Float4Float4(inBase, out);
            verifyResultsNativePowrFloat4Float4Float4(inBase, inExponent, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativePowrFloat4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsNativePowrFloat4Float4Float4(Allocation inBase, Allocation inExponent, Allocation out, boolean relaxed) {
        float[] arrayInBase = new float[INPUTSIZE * 4];
        inBase.copyTo(arrayInBase);
        float[] arrayInExponent = new float[INPUTSIZE * 4];
        inExponent.copyTo(arrayInExponent);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inBase = arrayInBase[i * 4 + j];
                args.inExponent = arrayInExponent[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativePowr(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inBase: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inBase, Float.floatToRawIntBits(args.inBase), args.inBase));
                    message.append("\n");
                    message.append("Input inExponent: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inExponent, Float.floatToRawIntBits(args.inExponent), args.inExponent));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativePowrFloat4Float4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testNativePowr() {
        checkNativePowrFloatFloatFloat();
        checkNativePowrFloat2Float2Float2();
        checkNativePowrFloat3Float3Float3();
        checkNativePowrFloat4Float4Float4();
    }
}
