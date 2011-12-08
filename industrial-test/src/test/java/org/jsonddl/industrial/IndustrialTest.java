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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.Arrays;

public class IndustrialTest {

  @Test
  public void testDigest() {
    // A builder have object-identity equality
    Example.Builder b1 = new Example.Builder();
    assertTrue(b1.hashCode() != 0);
    checkEquality(b1, b1);

    // A built object should also be equal to itself
    Example empty = b1.build();
    checkEquality(empty, empty);

    // Ensure a new builder is equal to the object that built it
    Example.Builder b2 = empty.builder();
    checkEquality(b2, empty);

    // Check that a newly-built object is equal to its predecessor
    Example empty2 = b2.build();
    checkEquality(empty, empty2);

    Example withString = empty2.builder().withAString("Hello a world!").withDouble(4.2).build();
    checkInequality(empty2, withString);

    Example withList = withString.builder().addAnIntegralList(42).build();
    checkInequality(withString, withList);

    Example withSimilarList = empty2.builder().addAnIntegralList(null).build();
    checkInequality(withList, withSimilarList);

    Example withMap = withList.builder().putAStringToListOfBooleanMap("foo",
        Arrays.asList(true, false)).build();
    checkInequality(withList, withMap);

    Example withSimilarMap = withList.builder().putAStringToListOfBooleanMap("foo",
        Arrays.asList(false, true)).build();
    checkInequality(withMap, withSimilarMap);
    checkEquality(withSimilarMap, withSimilarMap.builder());
  }

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

  private void checkEquality(Object o1, Object o2) {
    assertEquals(o1, o2);
    assertEquals(o2, o1);
    assertEquals(o1.hashCode(), o2.hashCode());
  }

  private void checkInequality(Object o1, Object o2) {
    // Not pedentically correct, but
    assertTrue(o1.hashCode() != o2.hashCode());
    assertFalse(o1.equals(o2));
    assertFalse(o2.equals(o1));
  }
}
