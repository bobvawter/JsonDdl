package org.jsonddl.generator;

import java.util.List;
import java.util.Map;

import org.jsonddl.Context;

class Type {
  @SuppressWarnings("rawtypes")
  enum Kind {
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

  private final int hashCode;
  private final Kind kind;
  private final String simpleName;
  private Type nested;
  private Type key;

  private Type value;

  public Type(Kind kind, String simpleName) {
    this.kind = kind;
    this.simpleName = simpleName;
    hashCode = kind.hashCode() * 7 + simpleName.hashCode() * 13;
  }

  public Type(Type nested) {
    this(Kind.LIST, List.class.getCanonicalName() + "<" + nested.getQualifiedName() + ">");
    this.nested = nested;
  }

  public Type(Type key, Type value) {
    this(Kind.MAP, Map.class.getCanonicalName() + "<" + key.getQualifiedName() + ", "
      + value.getQualifiedName() + ">");
    this.key = key;
    this.value = value;
  }

  public boolean canTraverse() {
    switch (kind) {
      case DDL:
        return true;
      case LIST:
        return Kind.DDL.equals(getNested().getKind());
    }
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Type)) {
      return false;
    }
    Type other = (Type) obj;
    return kind.equals(other.kind) && simpleName.equals(other.simpleName);
  }

  public String getContextParameterization() {
    switch (kind) {
      case DDL:
        return getQualifiedName();
      case LIST:
        return getNested().getQualifiedName();
    }
    throw new IllegalStateException();
  }

  public Type getKey() {
    return key;
  }

  public Kind getKind() {
    return kind;
  }

  public Type getNested() {
    return nested;
  }

  public String getQualifiedName() {
    return simpleName;
  }

  public Kind getType() {
    return kind;
  }

  public Type getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return kind.name() + " " + simpleName;
  }
}
