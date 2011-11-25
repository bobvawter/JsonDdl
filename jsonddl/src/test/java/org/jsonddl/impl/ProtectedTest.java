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
package org.jsonddl.impl;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.annotation.ElementType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsonddl.JsonDdlObject;
import org.junit.Test;

/**
 * Unit tests for {@link Protected}.
 */
public class ProtectedTest {

  @Test
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void testJsonDdlObject() {
    JsonDdlObject<?> obj = createStrictMock(JsonDdlObject.class);
    JsonDdlObject.Builder builder = createStrictMock(JsonDdlObject.Builder.class);
    expect(builder.build()).andReturn(obj).times(2);
    replay(obj, builder);

    assertSame(obj, Protected.object((Object) obj));
    assertSame(obj, Protected.object((Object) builder));
    assertSame(obj, Protected.object((JsonDdlObject) obj));
    assertSame(obj, Protected.object((JsonDdlObject) builder));

    verify(obj, builder);
  }

  @Test
  public void testList() {
    assertNull(Protected.object((List<?>) null));
    assertSame(Collections.emptyList(), Protected.object(Collections.emptyList()));
    assertEquals(Collections.singletonList("a").getClass(), Protected.object(Arrays.asList("b"))
        .getClass());
    List<String> input = Arrays.asList("a", "b", "c");
    List<String> output = Protected.object(input);
    assertEquals(input, output);
    assertNotSame(input, output);

    // Protecting a protected list should be idempotent
    assertSame(output, Protected.object(output));

    // But re-protecting the input list shouldn't be
    assertNotSame(output, Protected.object(input));

    // Mutations to input list aren't visible
    input.set(0, "b");
    assertEquals("a", output.get(0));

    // Changes to the protected list fail
    try {
      output.set(0, "b");
      fail();
    } catch (UnsupportedOperationException expected) {}
  }

  @Test
  public void testMap() {
    assertNull(Protected.object((Map<?, ?>) null));
    assertSame(Collections.emptyMap(), Protected.object(Collections.emptyMap()));
    Map<String, String> singletonMap = new HashMap<String, String>();
    singletonMap.put("a", "b");
    assertEquals(Collections.singletonMap("a", "b").getClass(), Protected.object(singletonMap)
        .getClass());

    Map<String, String> input = new HashMap<String, String>();
    input.put("a", "b");
    input.put("c", "d");
    input.put("e", "f");

    Map<String, String> output = Protected.object(input);
    assertEquals(input, output);
    assertNotSame(input, output);

    // Protecting a protected list should be idempotent
    assertSame(output, Protected.object(output));

    // But re-protecting the input list shouldn't be
    assertNotSame(output, Protected.object(input));

    // Mutations to input list aren't visible
    input.put("a", "q");
    assertEquals("b", output.get("a"));

    // Changes to the protected list fail
    try {
      output.put("a", "b");
      fail();
    } catch (UnsupportedOperationException expected) {}
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testMixedTypes() {
    List<Object> input = Arrays.<Object> asList(null, true, 1, 1.0, "Hello world",
        ElementType.METHOD, Arrays.asList(1, 2, 3), Collections.singletonMap("a", "b"));
    List<Object> output = Protected.object(input);
    assertEquals(input, output);

    try {
      ((List<Object>) output.get(6)).set(0, 0);
      fail();
    } catch (UnsupportedOperationException expected) {}
    try {
      ((Map<Object, Object>) output.get(7)).put(0, 0);
      fail();
    } catch (UnsupportedOperationException expected) {}
  }

  @Test
  public void testUnknown() {
    try {
      Protected.object(new Object());
      fail();
    } catch (IllegalArgumentException expected) {}
  }
}
