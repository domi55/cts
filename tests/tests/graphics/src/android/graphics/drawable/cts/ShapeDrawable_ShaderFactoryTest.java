/*
 * Copyright (C) 2008 The Android Open Source Project.
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

package android.graphics.drawable.cts;

import junit.framework.TestCase;

import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable.ShaderFactory;
import android.support.test.filters.SmallTest;

@SmallTest
public class ShapeDrawable_ShaderFactoryTest extends TestCase {

    public void testResize() {
        // This is an abstract function, but coverage
        // complains if we don't call it.
        ShaderFactory impl = new ShaderFactoryImpl();
        assertNull(impl.resize(0, 0));
    }

    private class ShaderFactoryImpl extends ShaderFactory {

        @Override
        public Shader resize(int width, int height) {
            return null;
        }
    }
}
