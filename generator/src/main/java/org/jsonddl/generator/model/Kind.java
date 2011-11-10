package org.jsonddl.generator.model;

import org.jsonddl.Context;

@SuppressWarnings("rawtypes")
public enum Kind {
  DDL(Context.ImmutableContext.class, Context.SettableContext.class),
  EXTERNAL,
  LIST(Context.ImmutableListContext.class, Context.ListContext.class),
  MAP,
  PRIMITIVE;

  private final Class<? extends Context> immutable;
  private final Class<? extends Context> mutable;

  private Kind() {
    this.immutable = null;
    this.mutable = null;
  }

  private Kind(Class<? extends Context> immutable, Class<? extends Context> mutable) {
    this.immutable = immutable;
    this.mutable = mutable;
  }

  public Class<? extends Context> getImmutableContextType() {
    return immutable;
  }

  public Class<? extends Context> getMutableContextType() {
    return mutable;
  }
}