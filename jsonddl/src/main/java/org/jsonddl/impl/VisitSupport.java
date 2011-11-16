package org.jsonddl.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;

/**
 * Provides runtime support for dynamic visitor methods. This type should not be referenced except
 * by generated code.
 */
public class VisitSupport {

  public static <J extends JsonDdlObject> void endVisit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    invoke(visitor, obj, ctx, "endVisit");
  }

  public static <J extends JsonDdlObject> boolean visit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    Object o = invoke(visitor, obj, ctx, "visit");
    return !Boolean.FALSE.equals(o);
  }

  private static Method findMethod(Class<?> visitor, String name, Class<?> searchFor) {
    while (searchFor != null) {
      try {
        Method m = visitor.getMethod(name, searchFor, Context.class);
        m.setAccessible(true);
        return m;
      } catch (SecurityException e) {
        throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
        searchFor = searchFor.getSuperclass();
        if (Object.class.equals(searchFor)) {
          searchFor = JsonDdlObject.class;
        }
      }
    }
    return null;
  }

  private static <J extends JsonDdlObject> Object invoke(JsonDdlVisitor visitor, J obj,
      Context<J> ctx, String name) {
    Throwable ex;
    try {
      Method m = findMethod(visitor.getClass(), name, obj.getDdlObjectType());
      if (m == null) {
        return null;
      }
      return m.invoke(visitor, obj, ctx);
    } catch (IllegalAccessException e) {
      ex = e;
    } catch (InvocationTargetException e) {
      ex = e.getCause();
    }
    throw new RuntimeException("Could not visit a " + visitor.getClass().getSimpleName()
      + " with a " + obj.getClass().getSimpleName(), ex);
  }
}
