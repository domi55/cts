/*
 * Copyright (C) 2011 The Android Open Source Project
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

package android.renderscript.cts;

import android.content.Context;
import android.content.res.Resources;
import android.renderscript.RenderScript;
import android.renderscript.RenderScript.RSMessageHandler;
import android.test.AndroidTestCase;
import com.android.cts.stub.R;

public class ComputeTest extends RSBaseCompute {

    private boolean initializeGlobals(ScriptC_primitives s) {
        float pF = s.get_floatTest();
        if (pF != 1.99f) {
            return false;
        }
        s.set_floatTest(2.99f);

        double pD = s.get_doubleTest();
        if (pD != 2.05) {
            return false;
        }
        s.set_doubleTest(3.05);

        byte pC = s.get_charTest();
        if (pC != -8) {
            return false;
        }
        s.set_charTest((byte)-16);

        short pS = s.get_shortTest();
        if (pS != -16) {
            return false;
        }
        s.set_shortTest((short)-32);

        int pI = s.get_intTest();
        if (pI != -32) {
            return false;
        }
        s.set_intTest(-64);

        long pL = s.get_longTest();
        if (pL != 17179869184l) {
            return false;
        }
        s.set_longTest(17179869185l);

        long puL = s.get_ulongTest();
        if (puL != 4611686018427387904L) {
            return false;
        }
        s.set_ulongTest(4611686018427387903L);


        long pLL = s.get_longlongTest();
        if (pLL != 68719476736L) {
            return false;
        }
        s.set_longlongTest(68719476735L);

        long pu64 = s.get_uint64_tTest();
        if (pu64 != 117179869184l) {
            return false;
        }
        s.set_uint64_tTest(117179869185l);

        return true;
    }

    /**
     * Test primitive types.
     */
    public void testPrimitives() {
        ScriptC_primitives t = new ScriptC_primitives(mRS,
                                                      mRes,
                                                      R.raw.primitives);

        assertTrue(initializeGlobals(t));
        t.invoke_test();
        waitForMessage();
        assertEquals(result, RS_MSG_TEST_PASSED);
    }

}
