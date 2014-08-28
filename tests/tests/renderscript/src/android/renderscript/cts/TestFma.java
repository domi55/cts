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

public class TestFma extends RSBaseCompute {

    private ScriptC_TestFma script;
    private ScriptC_TestFmaRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestFma(mRS);
        scriptRelaxed = new ScriptC_TestFmaRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloatFloat {
        public float inA;
        public float inB;
        public float inC;
        public Target.Floaty out;
    }

    private void checkFmaFloatFloatFloatFloat() {
        Allocation inA = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x5f6b3ee0c3466c2l, false);
        Allocation inB = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x5f6b3ee0c3466c3l, false);
        Allocation inC = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x5f6b3ee0c3466c4l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInB(inB);
            script.set_gAllocInC(inC);
            script.forEach_testFmaFloatFloatFloatFloat(inA, out);
            verifyResultsFmaFloatFloatFloatFloat(inA, inB, inC, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFmaFloatFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInB(inB);
            scriptRelaxed.set_gAllocInC(inC);
            scriptRelaxed.forEach_testFmaFloatFloatFloatFloat(inA, out);
            verifyResultsFmaFloatFloatFloatFloat(inA, inB, inC, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFmaFloatFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsFmaFloatFloatFloatFloat(Allocation inA, Allocation inB, Allocation inC, Allocation out, boolean relaxed) {
        float[] arrayInA = new float[INPUTSIZE * 1];
        inA.copyTo(arrayInA);
        float[] arrayInB = new float[INPUTSIZE * 1];
        inB.copyTo(arrayInB);
        float[] arrayInC = new float[INPUTSIZE * 1];
        inC.copyTo(arrayInC);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inA = arrayInA[i];
                args.inB = arrayInB[i];
                args.inC = arrayInC[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeFma(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inA: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inA, Float.floatToRawIntBits(args.inA), args.inA));
                    message.append("\n");
                    message.append("Input inB: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inB, Float.floatToRawIntBits(args.inB), args.inB));
                    message.append("\n");
                    message.append("Input inC: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inC, Float.floatToRawIntBits(args.inC), args.inC));
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
                    assertTrue("Incorrect output for checkFmaFloatFloatFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkFmaFloat2Float2Float2Float2() {
        Allocation inA = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x47b62b8849bc43dal, false);
        Allocation inB = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x47b62b8849bc43dbl, false);
        Allocation inC = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x47b62b8849bc43dcl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInB(inB);
            script.set_gAllocInC(inC);
            script.forEach_testFmaFloat2Float2Float2Float2(inA, out);
            verifyResultsFmaFloat2Float2Float2Float2(inA, inB, inC, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFmaFloat2Float2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInB(inB);
            scriptRelaxed.set_gAllocInC(inC);
            scriptRelaxed.forEach_testFmaFloat2Float2Float2Float2(inA, out);
            verifyResultsFmaFloat2Float2Float2Float2(inA, inB, inC, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFmaFloat2Float2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsFmaFloat2Float2Float2Float2(Allocation inA, Allocation inB, Allocation inC, Allocation out, boolean relaxed) {
        float[] arrayInA = new float[INPUTSIZE * 2];
        inA.copyTo(arrayInA);
        float[] arrayInB = new float[INPUTSIZE * 2];
        inB.copyTo(arrayInB);
        float[] arrayInC = new float[INPUTSIZE * 2];
        inC.copyTo(arrayInC);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inA = arrayInA[i * 2 + j];
                args.inB = arrayInB[i * 2 + j];
                args.inC = arrayInC[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeFma(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inA: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inA, Float.floatToRawIntBits(args.inA), args.inA));
                    message.append("\n");
                    message.append("Input inB: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inB, Float.floatToRawIntBits(args.inB), args.inB));
                    message.append("\n");
                    message.append("Input inC: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inC, Float.floatToRawIntBits(args.inC), args.inC));
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
                    assertTrue("Incorrect output for checkFmaFloat2Float2Float2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkFmaFloat3Float3Float3Float3() {
        Allocation inA = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x1d2fcf231c237d76l, false);
        Allocation inB = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x1d2fcf231c237d77l, false);
        Allocation inC = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x1d2fcf231c237d78l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInB(inB);
            script.set_gAllocInC(inC);
            script.forEach_testFmaFloat3Float3Float3Float3(inA, out);
            verifyResultsFmaFloat3Float3Float3Float3(inA, inB, inC, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFmaFloat3Float3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInB(inB);
            scriptRelaxed.set_gAllocInC(inC);
            scriptRelaxed.forEach_testFmaFloat3Float3Float3Float3(inA, out);
            verifyResultsFmaFloat3Float3Float3Float3(inA, inB, inC, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFmaFloat3Float3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsFmaFloat3Float3Float3Float3(Allocation inA, Allocation inB, Allocation inC, Allocation out, boolean relaxed) {
        float[] arrayInA = new float[INPUTSIZE * 4];
        inA.copyTo(arrayInA);
        float[] arrayInB = new float[INPUTSIZE * 4];
        inB.copyTo(arrayInB);
        float[] arrayInC = new float[INPUTSIZE * 4];
        inC.copyTo(arrayInC);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inA = arrayInA[i * 4 + j];
                args.inB = arrayInB[i * 4 + j];
                args.inC = arrayInC[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeFma(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inA: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inA, Float.floatToRawIntBits(args.inA), args.inA));
                    message.append("\n");
                    message.append("Input inB: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inB, Float.floatToRawIntBits(args.inB), args.inB));
                    message.append("\n");
                    message.append("Input inC: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inC, Float.floatToRawIntBits(args.inC), args.inC));
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
                    assertTrue("Incorrect output for checkFmaFloat3Float3Float3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkFmaFloat4Float4Float4Float4() {
        Allocation inA = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xf2a972bdee8ab712l, false);
        Allocation inB = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xf2a972bdee8ab713l, false);
        Allocation inC = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xf2a972bdee8ab714l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInB(inB);
            script.set_gAllocInC(inC);
            script.forEach_testFmaFloat4Float4Float4Float4(inA, out);
            verifyResultsFmaFloat4Float4Float4Float4(inA, inB, inC, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFmaFloat4Float4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInB(inB);
            scriptRelaxed.set_gAllocInC(inC);
            scriptRelaxed.forEach_testFmaFloat4Float4Float4Float4(inA, out);
            verifyResultsFmaFloat4Float4Float4Float4(inA, inB, inC, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testFmaFloat4Float4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsFmaFloat4Float4Float4Float4(Allocation inA, Allocation inB, Allocation inC, Allocation out, boolean relaxed) {
        float[] arrayInA = new float[INPUTSIZE * 4];
        inA.copyTo(arrayInA);
        float[] arrayInB = new float[INPUTSIZE * 4];
        inB.copyTo(arrayInB);
        float[] arrayInC = new float[INPUTSIZE * 4];
        inC.copyTo(arrayInC);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inA = arrayInA[i * 4 + j];
                args.inB = arrayInB[i * 4 + j];
                args.inC = arrayInC[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeFma(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inA: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inA, Float.floatToRawIntBits(args.inA), args.inA));
                    message.append("\n");
                    message.append("Input inB: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inB, Float.floatToRawIntBits(args.inB), args.inB));
                    message.append("\n");
                    message.append("Input inC: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inC, Float.floatToRawIntBits(args.inC), args.inC));
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
                    assertTrue("Incorrect output for checkFmaFloat4Float4Float4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testFma() {
        checkFmaFloatFloatFloatFloat();
        checkFmaFloat2Float2Float2Float2();
        checkFmaFloat3Float3Float3Float3();
        checkFmaFloat4Float4Float4Float4();
    }
}
