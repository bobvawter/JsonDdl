package org.jsonddl.model;

/**
 * A model represents a single kind of object within the schema. There is a 1:1 correspondence
 * between Models and Java classes or JS type closures.
 */
@javax.annotation.Generated(value = "org.jsonddl.generator.Generator", date = "2011-11-13T23:17:09")
public class Model implements org.jsonddl.JsonDdlObject<Model> {
  public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Model> {
    private Model obj;

    public Builder() {
      this(new Model());
    }

    public Builder(Model instance) {
      this.obj = instance;
    }

    @Override
    public Model build() {
      Model toReturn = obj;
      obj = null;
      return toReturn;
    }

    @Override
    public Builder from(java.util.Map<String, Object> map) {
      obj = obj.acceptMutable(org.jsonddl.JsonStringVisitor.fromJsonMap(map));
      return this;
    }

    @Override
    public Builder from(Model from) {
      withComment(from.getComment());
      withEnumValues(from.getEnumValues());
      withName(from.getName());
      withProperties(from.getProperties());
      return this;
    }

    public Builder withComment(java.lang.String value) {
      obj.comment = value;
      return this;
    }

    public Builder withEnumValues(java.util.List<EnumValue> value) {
      obj.enumValues = org.jsonddl.impl.Protected.object(value);
      return this;
    }

    public Builder withName(java.lang.String value) {
      obj.name = value;
      return this;
    }

    public Builder withProperties(java.util.List<Property> value) {
      obj.properties = org.jsonddl.impl.Protected.object(value);
      return this;
    }
  }

  private java.lang.String comment;
  private java.util.List<EnumValue> enumValues;
  private java.lang.String name;
  private java.util.List<Property> properties;

  private Model() {}

  @Override
  public void accept(org.jsonddl.JsonDdlVisitor visitor) {
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Model>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
  }

  @Override
  public Model acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
    return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Model>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
  }

  @Override
  public Builder builder() {
    return newInstance().from(this);
  }

  public java.lang.String getComment() {
    return comment;
  }

  public java.util.List<EnumValue> getEnumValues() {
    return enumValues;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.util.List<Property> getProperties() {
    return properties;
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
    new org.jsonddl.impl.ContextImpl.ListContext.Builder<EnumValue>()
        .withKind(org.jsonddl.model.Kind.LIST)
        .withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.DDL))
        .withLeafType(EnumValue.class)
        .withMutability(false)
        .withProperty("enumValues")
        .withValue(this.enumValues)
        .build().traverse(visitor);
    new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
        .withKind(org.jsonddl.model.Kind.STRING)
        .withLeafType(java.lang.String.class)
        .withMutability(false)
        .withProperty("name")
        .withValue(this.name)
        .build().traverse(visitor);
    new org.jsonddl.impl.ContextImpl.ListContext.Builder<Property>()
        .withKind(org.jsonddl.model.Kind.LIST)
        .withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.DDL))
        .withLeafType(Property.class)
        .withMutability(false)
        .withProperty("properties")
        .withValue(this.properties)
        .build().traverse(visitor);

  }

  @Override
  public Model traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
    Builder builder = newInstance();
    builder.withComment(
        new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
            .withKind(org.jsonddl.model.Kind.STRING)
            .withLeafType(java.lang.String.class)
            .withMutability(true)
            .withProperty("comment")
            .withValue(this.comment)
            .build().traverse(visitor));
    builder.withEnumValues(
        new org.jsonddl.impl.ContextImpl.ListContext.Builder<EnumValue>()
            .withKind(org.jsonddl.model.Kind.LIST)
            .withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.DDL))
            .withLeafType(EnumValue.class)
            .withMutability(true)
            .withProperty("enumValues")
            .withValue(this.enumValues)
            .build().traverse(visitor));
    builder.withName(
        new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
            .withKind(org.jsonddl.model.Kind.STRING)
            .withLeafType(java.lang.String.class)
            .withMutability(true)
            .withProperty("name")
            .withValue(this.name)
            .build().traverse(visitor));
    builder.withProperties(
        new org.jsonddl.impl.ContextImpl.ListContext.Builder<Property>()
            .withKind(org.jsonddl.model.Kind.LIST)
            .withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.DDL))
            .withLeafType(Property.class)
            .withMutability(true)
            .withProperty("properties")
            .withValue(this.properties)
            .build().traverse(visitor));

    return builder.build();
  }
}
