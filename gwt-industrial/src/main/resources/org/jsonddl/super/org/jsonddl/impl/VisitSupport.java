package org.jsonddl.impl;

import java.io.UnsupportedEncodingException;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;
import org.jsonddl.impl.ContextImpl.ObjectContext;

public class VisitSupport {
  private static final String MESSAGE = "Cannot use ad-hoc visitors with GWT mode. " +
      "Extend the package's visitor base class instead";

  public static <J extends JsonDdlObject<J>> void endVisit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    // throw new UnsupportedOperationException(MESSAGE);
  }

  public static <J extends JsonDdlObject<J>> boolean visit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    return true;
    // throw new UnsupportedOperationException(MESSAGE);
  }

  @SuppressWarnings("unchecked")
  static <T> Class<? extends T> asSubclass(Class<?> base, Class<T> desired) {
    return (Class<? extends T>) base;
  }

  @SuppressWarnings("unchecked")
  static <T> T cast(Object object, Class<T> to) {
    return (T) object;
  }

  static JsonDdlObject.Builder<?> create(Class<?> leafType) {
    return null;
  }

  static byte[] getBytes(String string) {
    try {
      return string.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  static <B extends JsonDdlObject<B>> ObjectContext.Builder<B> objectContextBuilder(Class<B> toBuild) {
    return new ObjectContext.Builder<B>();
  }
}
