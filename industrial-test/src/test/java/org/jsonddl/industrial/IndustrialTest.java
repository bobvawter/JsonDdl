/*
 * Copyright 2011 Robert W. Vawter III <bob@vawter.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jsonddl.industrial;

import static org.junit.Assert.*;
import org.junit.Test;

public class IndustrialTest {

  @Test
  public void testExtension() {
    Extended.Builder b = new Extended.Builder();
    assertTrue(b instanceof Base);
    assertTrue(b instanceof Base.Impl);
    assertFalse(b.isStringSet());
    b.setString("foo");
    assertTrue(b.isStringSet());
    b.setRandomString();

    Extended ext = b.build();
    assertTrue(b instanceof Base);
    assertTrue(b instanceof Base.Impl);
    assertTrue(ext.isStringSet());
    assertEquals("Hello World!", ext.getString());

    try {
      ext.setRandomString();
      fail();
    } catch (IllegalStateException expected) {}
  }
}
