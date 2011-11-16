package org.jsonddl.model;

@javax.annotation.Generated(value = "org.jsonddl.generator.Generator", date = "2011-11-15T21:53:00")
public class EnumValue implements org.jsonddl.JsonDdlObject<EnumValue> {
  public static class Builder implements org.jsonddl.JsonDdlObject.Builder<EnumValue> {
    private EnumValue obj;

    public Builder() {
      this(new EnumValue());
    }

    public Builder(EnumValue instance) {
      this.obj = instance;
    }

    @Override
    public Builder acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
      obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(obj)
          .withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
      return this;
    }

    @Override
    public EnumValue build() {
      EnumValue toReturn = obj;
      obj = null;
      return toReturn;
    }

    @Override
    public Builder from(EnumValue from) {
      withComment(from.getComment());
      withName(from.getName());
      return this;
    }

    @Override
    public Builder from(java.util.Map<String, Object> map) {
      acceptMutable(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
      return this;
    }

    @Override
    public Builder traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
      withComment(new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
          .withKind(org.jsonddl.model.Kind.STRING)
          .withLeafType(java.lang.String.class)
          .withMutability(true)
          .withProperty("comment")
          .withValue(obj.comment)
          .build().traverse(visitor));
      withName(new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
          .withKind(org.jsonddl.model.Kind.STRING)
          .withLeafType(java.lang.String.class)
          .withMutability(true)
          .withProperty("name")
          .withValue(obj.name)
          .build().traverse(visitor));

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
  }

  private java.lang.String comment;

  private java.lang.String name;

  private EnumValue() {}

  @Override
  public void accept(org.jsonddl.JsonDdlVisitor visitor) {
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
  }

  @Override
  public Builder builder() {
    return newInstance().from(this);
  }

  public java.lang.String getComment() {
    return comment;
  }

  public java.lang.String getName() {
    return name;
  }

  @Override
  public Builder newInstance() {
    return new Builder();
  }

  @Override
  public java.util.Map<String, Object> toJsonObject() {
    return org.jsonddl.JsonMapVisitor.toJsonObject(this);
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

  }
}
