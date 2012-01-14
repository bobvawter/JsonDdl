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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class IndustrialTest {

  @Test
  public void testCollections() {
    Example.Builder b = new Example.Builder();
    b.addAnIntegralList(1);
    b.putAStringToListOfBooleanMap("foo", Arrays.asList(true));

    Example ex = b.build();

    Example.Builder b2 = ex.builder();
    b2.addAnIntegralList(2);
    b2.putAStringToListOfBooleanMap("bar", Arrays.asList(false));

    Example ex2 = b2.build();
    assertEquals(Arrays.asList(1, 2), ex2.getAnIntegralList());
    assertEquals(2, ex2.getAStringToListOfBooleanMap().size());
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

  @Test
  public void testObjectMethods() {
    // A builder have object-identity equality
    Example.Builder b1 = new Example.Builder();
    assertTrue(b1.hashCode() != 0);
    checkEquality(b1, b1);
    checkInequality(b1, new Object());

    // A built object should also be equal to itself
    Example empty = b1.build();
    checkEquality(empty, empty);
    checkInequality(empty, new Object());

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
    assertFalse(withSimilarMap.toString().contains("true"));
  }

  @Test
  public void testToJsonObject() {
    Example ex = new Example.Builder()
        .withABoolean(true)
        .withAnExample(new Example.Builder().build())
        .addAnExampleList(new Example.Builder().build())
        .addAnIntegralList(42)
        .putAStringToListOfBooleanMap("foo", Arrays.asList(true, false))
        .build();
    Map<String, Object> map = ex.toJsonObject();

    // Assert specific iteration order
    assertEquals(Arrays.asList("aBoolean", "anExample", "anExampleList", "anIntegralList",
        "aStringToListOfBooleanMap"), new ArrayList<String>(map.keySet()));

    assertEquals(true, map.get("aBoolean"));
    assertEquals(0, ((Map<?, ?>) map.get("anExample")).size());
    assertEquals(0, ((Map<?, ?>) ((List<?>) map.get("anExampleList")).get(0)).size());
    assertEquals(Arrays.asList(42), map.get("anIntegralList"));
    assertEquals(Collections.singletonMap("foo", Arrays.asList(true, false)),
        map.get("aStringToListOfBooleanMap"));
  }

  private void checkEquality(Object o1, Object o2) {
    assertEquals(o1, o2);
    assertEquals(o2, o1);
    assertEquals(o1.hashCode(), o2.hashCode());
  }

  private void checkInequality(Object o1, Object o2) {
    // Not pedantically correct, but
    assertTrue(o1.hashCode() != o2.hashCode());
    assertFalse(o1.equals(o2));
    assertFalse(o2.equals(o1));
  }
}
