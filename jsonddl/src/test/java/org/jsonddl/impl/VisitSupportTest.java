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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VisitSupport}.
 */
public class VisitSupportTest {

  static interface EmptyVisitor extends JsonDdlVisitor {}

  /**
   * Tests that a parameter of {@link JsonDdlObject} will match any type.
   */
  static class FallbackVisitor implements JsonDdlVisitor {
    boolean didEndVisit;
    JsonDdlObject<?> visited;

    public void endVisit(JsonDdlObject<?> x) {
      assertSame(visited, x);
      didEndVisit = true;
    }

    public boolean visit(JsonDdlObject<?> x) {
      visited = x;
      return true;
    }
  }

  static interface Foo extends JsonDdlObject<Foo> {}

  static class FooContextVisitor implements JsonDdlVisitor {
    Context<Foo> context;
    boolean didEndVisit;
    Foo visited;
    boolean visitReturn;

    public void endVisit(Foo x, Context<Foo> ctx) {
      assertSame(visited, x);
      assertSame(context, ctx);
      didEndVisit = true;
    }

    public boolean visit(Foo x, Context<Foo> ctx) {
      visited = x;
      context = ctx;
      return visitReturn;
    }
  }

  static class FooVisitor implements JsonDdlVisitor {
    boolean didEndVisit;
    Foo visited;
    boolean visitReturn;

    public void endVisit(Foo x) {
      assertSame(visited, x);
      didEndVisit = true;
    }

    public boolean visit(Foo x) {
      visited = x;
      return visitReturn;
    }
  }

  Context<Foo> ctx;
  Foo foo;

  @After
  public void after() {
    verify(foo, ctx);
  }

  @Before
  @SuppressWarnings("unchecked")
  public void before() {
    foo = createStrictMock(Foo.class);
    expect(foo.getDdlObjectType()).andReturn(Foo.class).times(2);
    ctx = createStrictMock(Context.class);
    replay(foo, ctx);
  }

  @Test
  public void testFallback() {
    FallbackVisitor v = new FallbackVisitor();
    assertTrue(VisitSupport.visit(v, foo, ctx));
    assertSame(v.visited, foo);
    VisitSupport.endVisit(v, foo, ctx);
    assertTrue(v.didEndVisit);
  }

  @Test
  public void testOneArgVisit() {
    FooVisitor v = new FooVisitor();
    v.visitReturn = true;
    assertTrue(VisitSupport.visit(v, foo, ctx));
    assertSame(foo, v.visited);
    VisitSupport.endVisit(v, foo, ctx);
    assertTrue(v.didEndVisit);
  }

  @Test
  public void testTwoArgVisit() {
    FooContextVisitor v = new FooContextVisitor();
    assertFalse(VisitSupport.visit(v, foo, ctx));
    assertSame(foo, v.visited);
    assertSame(ctx, v.context);
    VisitSupport.endVisit(v, foo, ctx);
    assertTrue(v.didEndVisit);
  }

  @Test
  public void trivialVisitation() {
    EmptyVisitor v = createStrictMock(EmptyVisitor.class);
    replay(v);
    assertTrue(VisitSupport.visit(v, foo, ctx));
    VisitSupport.endVisit(v, foo, ctx);
  }
}
