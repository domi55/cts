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

package dxc.junit.opcodes.invokestatic.jm;

public class TestClass {
    
    public static int test(){
        return 777;
    }
    
    public static int test1(int i1){
        int i = i1 + 5;
        return i;
    }
    
    public static int testArgsOrder(int i1, int i2){
        int a = 3454;
        int b = 2302;
        int i = i1 / i2;
        return i;
    }
    
    protected static int testP(){
        return 888;
    }
    
    private static void testPvt(){
        
    }
}

