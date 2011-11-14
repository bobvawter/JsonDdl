package org.jsonddl.model;

@javax.annotation.Generated(value = "org.jsonddl.generator.Generator", date = "2011-11-13T23:17:09")
public class Type implements org.jsonddl.JsonDdlObject<Type> {
  public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Type> {
    private Type obj;

    public Builder() {
      this(new Type());
    }

    public Builder(Type instance) {
      this.obj = instance;
    }

    @Override
    public Type build() {
      Type toReturn = obj;
      obj = null;
      return toReturn;
    }

    @Override
    public Builder from(java.util.Map<String, Object> map) {
      obj = obj.acceptMutable(org.jsonddl.JsonStringVisitor.fromJsonMap(map));
      return this;
    }

    @Override
    public Builder from(Type from) {
      withName(from.getName());
      withKind(from.getKind());
      withListElement(from.getListElement());
      withMapKey(from.getMapKey());
      withMapValue(from.getMapValue());
      return this;
    }

    public Builder withKind(Kind value) {
      obj.kind = value;
      return this;
    }

    public Builder withListElement(Type value) {
      obj.listElement = value;
      return this;
    }

    public Builder withMapKey(Type value) {
      obj.mapKey = value;
      return this;
    }

    public Builder withMapValue(Type value) {
      obj.mapValue = value;
      return this;
    }

    public Builder withName(java.lang.String value) {
      obj.name = value;
      return this;
    }
  }

  private java.lang.String name;
  private Kind kind;
  private Type listElement;
  private Type mapKey;
  private Type mapValue;

  private Type() {}

  @Override
  public void accept(org.jsonddl.JsonDdlVisitor visitor) {
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
  }

  @Override
  public Type acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
    return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
  }

  @Override
  public Builder builder() {
    return newInstance().from(this);
  }

  public Kind getKind() {
    return kind;
  }

  public Type getListElement() {
    return listElement;
  }

  public Type getMapKey() {
    return mapKey;
  }

  public Type getMapValue() {
    return mapValue;
  }

  public java.lang.String getName() {
    return name;
  }

  @Override
  public Builder newInstance() {
    return new Builder();
  }

  @Override
  public String toJson() {
    return org.jsonddl.JsonStringVisitor.toJsonString(this);
  }

  @Override
  public void traverse(org.jsonddl.JsonDdlVisitor visitor) {
    new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
        .withKind(org.jsonddl.model.Kind.STRING)
        .withLeafType(java.lang.String.class)
        .withMutability(false)
        .withProperty("name")
        .withValue(this.name)
        .build().traverse(visitor);
    new org.jsonddl.impl.ContextImpl.ValueContext.Builder<Kind>()
        .withKind(org.jsonddl.model.Kind.ENUM)
        .withLeafType(Kind.class)
        .withMutability(false)
        .withProperty("kind")
        .withValue(this.kind)
        .build().traverse(visitor);
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
        .withKind(org.jsonddl.model.Kind.DDL)
        .withLeafType(Type.class)
        .withMutability(false)
        .withProperty("listElement")
        .withValue(this.listElement)
        .build().traverse(visitor);
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
        .withKind(org.jsonddl.model.Kind.DDL)
        .withLeafType(Type.class)
        .withMutability(false)
        .withProperty("mapKey")
        .withValue(this.mapKey)
        .build().traverse(visitor);
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
        .withKind(org.jsonddl.model.Kind.DDL)
        .withLeafType(Type.class)
        .withMutability(false)
        .withProperty("mapValue")
        .withValue(this.mapValue)
        .build().traverse(visitor);

  }

  @Override
  public Type traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
    Builder builder = newInstance();
    builder.withName(
        new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
            .withKind(org.jsonddl.model.Kind.STRING)
            .withLeafType(java.lang.String.class)
            .withMutability(true)
            .withProperty("name")
            .withValue(this.name)
            .build().traverse(visitor));
    builder.withKind(
        new org.jsonddl.impl.ContextImpl.ValueContext.Builder<Kind>()
            .withKind(org.jsonddl.model.Kind.ENUM)
            .withLeafType(Kind.class)
            .withMutability(true)
            .withProperty("kind")
            .withValue(this.kind)
            .build().traverse(visitor));
    builder.withListElement(
        new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
            .withKind(org.jsonddl.model.Kind.DDL)
            .withLeafType(Type.class)
            .withMutability(true)
            .withProperty("listElement")
            .withValue(this.listElement)
            .build().traverse(visitor));
    builder.withMapKey(
        new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
            .withKind(org.jsonddl.model.Kind.DDL)
            .withLeafType(Type.class)
            .withMutability(true)
            .withProperty("mapKey")
            .withValue(this.mapKey)
            .build().traverse(visitor));
    builder.withMapValue(
        new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
            .withKind(org.jsonddl.model.Kind.DDL)
            .withLeafType(Type.class)
            .withMutability(true)
            .withProperty("mapValue")
            .withValue(this.mapValue)
            .build().traverse(visitor));

    return builder.build();
  }
}
