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

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides runtime support for dynamic visitor methods. This type should not be referenced except
 * by generated code.
 */
public class VisitSupport {

  private static class Lookup {
    private final String name;
    private final Class<?> searchFor;
    private final Class<?> visitor;
    private final int hash;

    public Lookup(Class<?> visitor, String name, Class<?> searchFor) {
      this.name = name.intern();
      this.searchFor = searchFor;
      this.visitor = visitor;
      hash = name.hashCode() * 13 + searchFor.hashCode() * 11 + searchFor.hashCode() * 7;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Lookup)) {
        return false;
      }
      Lookup other = (Lookup) o;
      return name.equals(other.name) && searchFor.equals(other.searchFor)
        && visitor.equals(other.visitor);
    }

    @Override
    public int hashCode() {
      return hash;
    }
  }

  /**
   * Method lookups in findMethod() have been demonstrated to be expensive, so we'll cache them.
   */
  private static final Map<Lookup, Method> lookup = new ConcurrentHashMap<Lookup, Method>();
  private static final Method NULL_METHOD;
  static {
    try {
      NULL_METHOD = Object.class.getMethod("hashCode");
    } catch (SecurityException e) {
      // Should never happen
      throw new RuntimeException("Cannot find Object.hashCode", e);
    } catch (NoSuchMethodException e) {
      // Should never happen
      throw new RuntimeException("Cannot find Object.hashCode", e);
    }
  }

  public static <J extends JsonDdlObject<J>> void endVisit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    invoke(visitor, obj, ctx, "endVisit");
  }

  public static <J extends JsonDdlObject<J>> boolean visit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    Object o = invoke(visitor, obj, ctx, "visit");
    return !Boolean.FALSE.equals(o);
  }

  private static Method findMethod(Class<?> visitor, String name, Class<?> searchFor) {
    Lookup l = new Lookup(visitor, name, searchFor);
    Method found = lookup.get(l);
    if (found != null) {
      return found == NULL_METHOD ? null : found;
    }
    while (searchFor != null) {
      try {
        Method m;
        try {
          m = visitor.getMethod(name, searchFor, Context.class);
        } catch (NoSuchMethodException e) {
          m = visitor.getMethod(name, searchFor);
        }
        m.setAccessible(true);
        lookup.put(l, m);
        return m;
      } catch (SecurityException e) {
        throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
        if (JsonDdlObject.class.equals(searchFor)) {
          break;
        }
        searchFor = searchFor.getSuperclass();
        if (searchFor == null) {
          searchFor = JsonDdlObject.class;
        }
      }
    }
    lookup.put(l, NULL_METHOD);
    return null;
  }

  private static <J extends JsonDdlObject<J>> Object invoke(JsonDdlVisitor visitor, J obj,
      Context<J> ctx, String name) {
    Throwable ex;
    try {
      Method m = findMethod(visitor.getClass(), name, obj.getDdlObjectType());
      if (m == null) {
        return null;
      }
      switch (m.getParameterTypes().length) {
        case 1:
          return m.invoke(visitor, obj);
        case 2:
          return m.invoke(visitor, obj, ctx);
        default:
          throw new RuntimeException("Should not have found method " + m.getName());
      }
    } catch (IllegalAccessException e) {
      ex = e;
    } catch (InvocationTargetException e) {
      ex = e.getCause();
    }
    throw new RuntimeException("Could not visit a " + visitor.getClass().getSimpleName()
      + " with a " + obj.getClass().getSimpleName(), ex);
  }
}
