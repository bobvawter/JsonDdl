package org.jsonddl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VisitSupport {
  public static <J extends JsonDdlObject<J>> void endVisit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    Exception ex;
    try {
      Method m = visitor.getClass().getMethod("endVisit", obj.getClass(), Context.class);
      m.setAccessible(true);
      m.invoke(visitor, obj, ctx);
      return;
    } catch (NoSuchMethodException e) {
      return;
    } catch (SecurityException e) {
      ex = e;
    } catch (IllegalArgumentException e) {
      ex = e;
    } catch (IllegalAccessException e) {
      ex = e;
    } catch (InvocationTargetException e) {
      ex = e;
    }
    throw new RuntimeException("Could not visit a " + visitor.getClass().getSimpleName()
      + " with a " + obj.getClass().getSimpleName(), ex);
  }

  public static <J extends JsonDdlObject<J>> boolean visit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    Exception ex;
    try {
      Method m = visitor.getClass().getMethod("visit", obj.getClass(), Context.class);
      m.setAccessible(true);
      Object returned = m.invoke(visitor, obj, ctx);
      return !Boolean.FALSE.equals(returned);
    } catch (NoSuchMethodException e) {
      return true;
    } catch (SecurityException e) {
      ex = e;
    } catch (IllegalArgumentException e) {
      ex = e;
    } catch (IllegalAccessException e) {
      ex = e;
    } catch (InvocationTargetException e) {
      ex = e;
    }
    throw new RuntimeException("Could not visit a " + visitor.getClass().getSimpleName()
      + " with a " + obj.getClass().getSimpleName(), ex);
  }
}
