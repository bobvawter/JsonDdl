package org.jsonddl.generator.model;

import org.jsonddl.Context;

public enum Kind {
  BOOLEAN(Context.ValueContext.Builder.class),
  DDL(Context.ObjectContext.Builder.class),
  DOUBLE(Context.ValueContext.Builder.class),
  EXTERNAL(Context.ValueContext.Builder.class),
  INTEGER(Context.ValueContext.Builder.class),
  LIST(Context.ListContext.Builder.class),
  MAP(Context.MapContext.Builder.class);

  private final Class<?> builder;

  private Kind(Class<?> builder) {
    this.builder = builder;
  }

  public Class<?> getContextBuilderType() {
    return builder;
  }
}