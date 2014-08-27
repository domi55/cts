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

package dot.junit.opcodes.or_long_2addr;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_1;
import dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_3;

public class Test_or_long_2addr extends DxTestCase {

    /**
     * @title Arguments = 123456789121l, 2l
     */
    public void testN1() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(123456789123l, t.run(123456789121l, 2l));
    }

    /**
     * @title Arguments = 0xffffffffffffff8l, 0xffffffffffffff1l
     */
    public void testN2() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(0xffffffffffffff9l, t.run(0xffffffffffffff8l,
                0xffffffffffffff1l));
    }

    /**
     * @title Arguments = 0xabcdefabcdef & -1
     */
    public void testN3() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(-1l, t.run(0xabcdefabcdefl, -1l));
    }

    /**
     * @title Arguments = 0 & -1
     */
    public void testB1() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(-1l, t.run(0l, -1l));
    }

    /**
     * @title Arguments = Long.MAX_VALUE & Long.MIN_VALUE
     */
    public void testB2() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(-1l, t.run(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    /**
     * @constraint A24 
     * @title number of registers
     */
    public void testVFE1() {
        load("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_2", VerifyError.class);
    }

    

    /**
     * @constraint B1 
     * @title types of arguments - int & long
     */
    public void testVFE2() {
        load("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_4", VerifyError.class);
    }

    /**
     * @constraint B1 
     * @title types of arguments - float & long
     */
    public void testVFE3() {
        load("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_5", VerifyError.class);
    }

    /**
     * @constraint B1 
     * @title types of arguments - reference & long
     */
    public void testVFE4() {
        load("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_6", VerifyError.class);
    }

    /**
     * @constraint B1
     * @title Types of arguments - double, long. The verifier checks that longs
     * and doubles are not used interchangeably.
     */
    public void testVFE5() {
        load("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_3", VerifyError.class);
    }
}
