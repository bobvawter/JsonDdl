package org.jsonddl.generator.model;

import org.jsonddl.impl.ContextImpl;

public enum Kind {
  BOOLEAN(ContextImpl.ValueContext.Builder.class),
  DDL(ContextImpl.ObjectContext.Builder.class),
  DOUBLE(ContextImpl.ValueContext.Builder.class),
  EXTERNAL(ContextImpl.ValueContext.Builder.class),
  INTEGER(ContextImpl.ValueContext.Builder.class),
  LIST(ContextImpl.ListContext.Builder.class),
  MAP(ContextImpl.MapContext.Builder.class);

  private final Class<?> builder;

  private Kind(Class<?> builder) {
    this.builder = builder;
  }

  public Class<?> getContextBuilderType() {
    return builder;
  }
}