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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;
import org.jsonddl.impl.ContextImpl.ListContext;
import org.jsonddl.impl.ContextImpl.MapContext;
import org.jsonddl.impl.ContextImpl.PropertyContext;
import org.jsonddl.impl.ContextImpl.ValueContext;
import org.jsonddl.model.Kind;
import org.junit.Test;

/**
 * Tests of the various {@link Context} types.
 */
public class ContextImplTest {

  interface FooBuilder extends JsonDdlObject.Builder<FooBuilder>, Traversable<FooBuilder> {}

  @Test
  public void testListContext() {
    final FooBuilder foo1 = makeFoo();
    final FooBuilder foo2 = makeFoo();
    List<FooBuilder> list = new ArrayList<FooBuilder>();
    list.add(foo1);
    list.add(foo2);
    // Simulate traversing list properties of a builder that was created from an immutable object
    list = Protected.object(list);
    ListContext<FooBuilder> ctx = new ListContext.Builder<FooBuilder>()
        .withKind(Kind.LIST)
        .withLeafType(FooBuilder.class)
        .withMutability(true)
        .withNestedKinds(Arrays.asList(Kind.DDL))
        .withProperty("someList")
        .withValue(list)
        .build();
    // Insert before and after foo1, removing foo2
    class V implements JsonDdlVisitor {
      FooBuilder after;
      FooBuilder before;
      Set<FooBuilder> seen = new HashSet<FooBuilder>();
      FooBuilder replacement;

      @SuppressWarnings("unused")
      public void endVisit(FooBuilder x, Context<FooBuilder> ctx) {
        assertEquals(Kind.DDL, ctx.getKind());
        assertEquals(FooBuilder.class, ctx.getLeafType());
        assertEquals("someList", ctx.getProperty());
        assertTrue("Re-traversing already-seen object", seen.add(x));
        assertTrue(ctx.canInsert());
        assertTrue(ctx.canRemove());
        assertTrue(ctx.canReplace());
        if (x == foo1) {
          before = makeFoo();
          ctx.insertBefore(before);

          after = makeFoo();
          ctx.insertAfter(after);
        } else if (x == after) {
          replacement = makeFoo();
          ctx.replace(replacement);
        } else if (x == foo2) {
          ctx.remove();
        } else {
          fail("Saw unexpecetd list element");
        }
      }
    }
    V v = new V();
    list = ctx.traverse(v);
    assertEquals(Arrays.asList(v.before, foo1, v.replacement), list);
    verify(foo1, foo2, v.after, v.before, v.replacement);
  }

  @Test
  public void testMapContext() {
    final FooBuilder foo1 = makeFoo();
    final FooBuilder foo2 = makeFoo();
    Map<String, FooBuilder> map = new TreeMap<String, FooBuilder>();
    map.put("foo1", foo1);
    map.put("foo2", foo2);
    // Simulate traversing properties of a builder that was created from an immutable object
    map = Protected.object(map);
    MapContext<FooBuilder> ctx = new MapContext.Builder<FooBuilder>()
        .withKind(Kind.LIST)
        .withLeafType(FooBuilder.class)
        .withMutability(true)
        .withNestedKinds(Arrays.asList(Kind.DDL))
        .withProperty("someList")
        .withValue(map)
        .build();
    // Replace foo1, remove foo2
    class V implements JsonDdlVisitor {
      Set<FooBuilder> seen = new HashSet<FooBuilder>();
      FooBuilder replacement;

      @SuppressWarnings("unused")
      public void endVisit(FooBuilder x, Context<FooBuilder> ctx) {
        assertEquals(Kind.DDL, ctx.getKind());
        assertEquals(FooBuilder.class, ctx.getLeafType());
        assertEquals("someList", ctx.getProperty());
        assertTrue("Re-traversing already-seen object", seen.add(x));
        assertFalse(ctx.canInsert());
        assertTrue(ctx.canRemove());
        assertTrue(ctx.canReplace());
        if (x == foo1) {
          replacement = makeFoo();
          ctx.replace(replacement);
        } else if (x == foo2) {
          ctx.remove();
        } else {
          fail("Saw unexpecetd list element");
        }
      }
    }
    V v = new V();
    map = ctx.traverse(v);

    assertEquals(Collections.singletonMap("foo1", v.replacement), map);
    verify(foo1, foo2, v.replacement);
  }

  /**
   * Tests {@link PropertyContext} and {@link ValueContext}.
   */
  @Test
  public void testValueContextImmutable() {
    ValueContext<Boolean> ctx = new ValueContext.Builder<Boolean>().withKind(Kind.BOOLEAN)
        .withLeafType(Boolean.class).withProperty("property").withValue(true).build();
    assertEquals(Kind.BOOLEAN, ctx.getKind());
    assertEquals("property", ctx.getProperty());
    assertEquals(Boolean.class, ctx.getLeafType());

    assertTrue(ctx.getNestedKinds().isEmpty());
    try {
      ctx.getNestedKinds().add(Kind.DDL);
      fail();
    } catch (UnsupportedOperationException expected) {}

    try {
      assertFalse(ctx.canInsert());
      ctx.insertBefore(false);
      fail();
    } catch (UnsupportedOperationException expected) {}
    try {
      assertFalse(ctx.canInsert());
      ctx.insertAfter(false);
      fail();
    } catch (UnsupportedOperationException expected) {}
    try {
      assertFalse(ctx.canReplace());
      ctx.replace(false);
      fail();
    } catch (UnsupportedOperationException expected) {}
    try {
      assertFalse(ctx.canRemove());
      ctx.remove();
      fail();
    } catch (UnsupportedOperationException expected) {}
    assertTrue(ctx.traverse(null));
  }

  /**
   * Tests {@link PropertyContext} and {@link ValueContext}.
   */
  @Test
  public void testValueContextMutable() {
    ValueContext<Boolean> ctx = new ValueContext.Builder<Boolean>().withKind(Kind.BOOLEAN)
        .withLeafType(Boolean.class).withMutability(true).withProperty("property").withValue(true)
        .build();
    assertEquals(Kind.BOOLEAN, ctx.getKind());
    assertEquals("property", ctx.getProperty());
    assertEquals(Boolean.class, ctx.getLeafType());

    assertTrue(ctx.getNestedKinds().isEmpty());
    try {
      ctx.getNestedKinds().add(Kind.DDL);
      fail();
    } catch (UnsupportedOperationException expected) {}

    try {
      assertFalse(ctx.canInsert());
      ctx.insertBefore(false);
      fail();
    } catch (UnsupportedOperationException expected) {}
    try {
      assertFalse(ctx.canInsert());
      ctx.insertAfter(false);
      fail();
    } catch (UnsupportedOperationException expected) {}

    assertTrue(ctx.canReplace());
    ctx.replace(false);

    try {
      assertFalse(ctx.canRemove());
      ctx.remove();
      fail();
    } catch (UnsupportedOperationException expected) {}

    assertFalse(ctx.traverse(null));
  }

  private FooBuilder makeFoo() {
    FooBuilder toReturn = createMock(FooBuilder.class);
    expect(toReturn.build()).andReturn(toReturn).anyTimes();
    expect(toReturn.builder()).andReturn(toReturn).anyTimes();
    expect(toReturn.getDdlObjectType()).andReturn(FooBuilder.class).anyTimes();
    expect(toReturn.traverse(anyObject(JsonDdlVisitor.class))).andReturn(toReturn).anyTimes();
    replay(toReturn);
    return toReturn;
  }
}
