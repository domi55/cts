/*
 * Copyright (C) 2008 The Android Open Source Project
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

package dxc.junit.opcodes.sipush;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;
import dxc.junit.opcodes.sipush.jm.T_sipush_1;
import dxc.junit.opcodes.sipush.jm.T_sipush_2;
import dxc.junit.opcodes.sipush.jm.T_sipush_3;

public class Test_sipush extends DxTestCase {

    /**
     * @title sipush -13570
     */
    public void testN1() {
        T_sipush_1 t = new T_sipush_1();
        assertEquals(-13570, t.run());
    }

    /**
     * @title sipush 0
     */
    public void testB1() {
        T_sipush_2 t = new T_sipush_2();
        assertEquals(0, t.run());
    }

    /**
     * @title sipush - 1
     */
    public void testB2() {
        T_sipush_3 t = new T_sipush_3();
        assertEquals(-1, t.run());
    }

    /**
     * @constraint 4.8.2.5
     * @title stack size
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.opcodes.sipush.jm.T_sipush_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

}
