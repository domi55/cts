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

package dot.junit.opcodes.sget_boolean;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_1;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_11;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_5;
import dot.junit.opcodes.sget_boolean.d.T_sget_boolean_9;

public class Test_sget_boolean extends DxTestCase {
    
    /**
     * @title get boolean from static field
     */
    public void testN1() {
        T_sget_boolean_1 t = new T_sget_boolean_1();
        assertEquals(true, t.run());
    }


    /**
     * @title access protected field from subclass
     */
    public void testN3() {
        //@uses dot.junit.opcodes.sget_boolean.d.T_sget_boolean_1
        //@uses dot.junit.opcodes.sget_boolean.d.T_sget_boolean_11
        T_sget_boolean_11 t = new T_sget_boolean_11();
        assertEquals(true, t.run());
    }

    /**
     * @constraint A12
     * @title attempt to access non-static field
     */
    public void testE1() {
        
        T_sget_boolean_5 t = new T_sget_boolean_5();
        try {
            t.run();
            fail("expected IncompatibleClassChangeError");
        } catch (IncompatibleClassChangeError e) {
            // expected
        }
    }
    
    /**
     * @title initialization of referenced class throws exception
     */
    public void testE6() {
        T_sget_boolean_9 t = new T_sget_boolean_9();
        try {
            t.run();
            fail("expected Error");
        } catch (Error e) {
            // expected
        }
    }

   

    /**
     * @constraint A12 
     * @title constant pool index
     */
    public void testVFE1() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint A23 
     * @title number of registers
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * 
     * @constraint B13 
     * @title read boolean from long field - only field with same name but 
     * different type exists
     */
    public void testVFE3() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * @constraint n/a
     * @title Attempt to read inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
     */
    public void testVFE4() {
        //@uses dot.junit.opcodes.sget_boolean.d.T_sget_boolean_6
        //@uses dot.junit.opcodes.sget_boolean.TestStubs
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
     */
    public void testVFE5() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint n/a
     * @title Attempt to read undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
     */
    public void testVFE6() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
     */
    public void testVFE7() {
        //@uses dot.junit.opcodes.sget_boolean.d.T_sget_boolean_12
        //@uses dot.junit.opcodes.sget_boolean.d.T_sget_boolean_1
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
   
    /**
     * @constraint B1 
     * @title sget_boolean shall not work for reference fields
     */
    public void testVFE8() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_14");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * 
     * @constraint B1 
     * @title sget_boolean shall not work for short fields
     */
    public void testVFE9() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_15");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * 
     * @constraint B1 
     * @title sget_boolean shall not work for int fields
     */
    public void testVFE10() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_16");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * 
     * @constraint B1 
     * @title sget_boolean shall not work for char fields
     */
    public void testVFE11() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_17");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * 
     * @constraint B1 
     * @title sget_boolean shall not work for byte fields
     */
    public void testVFE12() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_18");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }    
    
    /**
     * 
     * @constraint B1 
     * @title sget_boolean shall not work for double fields
     */
    public void testVFE13() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_19");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    } 
    
    /**
     * 
     * @constraint B1 
     * @title sget_boolean shall not work for long fields
     */
    public void testVFE14() {
        try {
            Class.forName("dot.junit.opcodes.sget_boolean.d.T_sget_boolean_20");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    } 
}
