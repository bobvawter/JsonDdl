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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jsonddl.JsonDdlVisitor.Context;
import org.jsonddl.impl.ContextImpl.PropertyContext;
import org.jsonddl.impl.ContextImpl.ValueContext;
import org.jsonddl.model.Kind;
import org.junit.Test;

/**
 * Tests of the various {@link Context} types.
 */
public class ContextImplTest {

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
}
