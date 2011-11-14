package org.jsonddl.model;

/**
 * A property is a pair of a name and a type.
 */
@javax.annotation.Generated(value = "org.jsonddl.generator.Generator", date = "2011-11-13T23:17:09")
public class Property implements org.jsonddl.JsonDdlObject<Property> {
  public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Property> {
    private Property obj;

    public Builder() {
      this(new Property());
    }

    public Builder(Property instance) {
      this.obj = instance;
    }

    @Override
    public Property build() {
      Property toReturn = obj;
      obj = null;
      return toReturn;
    }

    @Override
    public Builder from(java.util.Map<String, Object> map) {
      obj = obj.acceptMutable(org.jsonddl.JsonStringVisitor.fromJsonMap(map));
      return this;
    }

    @Override
    public Builder from(Property from) {
      withComment(from.getComment());
      withName(from.getName());
      withType(from.getType());
      return this;
    }

    public Builder withComment(java.lang.String value) {
      obj.comment = value;
      return this;
    }

    public Builder withName(java.lang.String value) {
      obj.name = value;
      return this;
    }

    public Builder withType(Type value) {
      obj.type = value;
      return this;
    }
  }

  private java.lang.String comment;
  private java.lang.String name;
  private Type type;

  private Property() {}

  @Override
  public void accept(org.jsonddl.JsonDdlVisitor visitor) {
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Property>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
  }

  @Override
  public Property acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
    return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Property>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
  }

  @Override
  public Builder builder() {
    return newInstance().from(this);
  }

  /**
   * This comment will be stored in this property.
   */
  public java.lang.String getComment() {
    return comment;
  }

  public java.lang.String getName() {
    return name;
  }

  public Type getType() {
    return type;
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
        .withProperty("comment")
        .withValue(this.comment)
        .build().traverse(visitor);
    new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
        .withKind(org.jsonddl.model.Kind.STRING)
        .withLeafType(java.lang.String.class)
        .withMutability(false)
        .withProperty("name")
        .withValue(this.name)
        .build().traverse(visitor);
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
        .withKind(org.jsonddl.model.Kind.DDL)
        .withLeafType(Type.class)
        .withMutability(false)
        .withProperty("type")
        .withValue(this.type)
        .build().traverse(visitor);

  }

  @Override
  public Property traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
    Builder builder = newInstance();
    builder.withComment(
        new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
            .withKind(org.jsonddl.model.Kind.STRING)
            .withLeafType(java.lang.String.class)
            .withMutability(true)
            .withProperty("comment")
            .withValue(this.comment)
            .build().traverse(visitor));
    builder.withName(
        new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
            .withKind(org.jsonddl.model.Kind.STRING)
            .withLeafType(java.lang.String.class)
            .withMutability(true)
            .withProperty("name")
            .withValue(this.name)
            .build().traverse(visitor));
    builder.withType(
        new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
            .withKind(org.jsonddl.model.Kind.DDL)
            .withLeafType(Type.class)
            .withMutability(true)
            .withProperty("type")
            .withValue(this.type)
            .build().traverse(visitor));

    return builder.build();
  }
}
