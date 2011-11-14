package org.jsonddl.model;

/**
 * A Schema is the top-level object that encapsulates the normalized form of a json-ddl schema.
 */
@javax.annotation.Generated(value = "org.jsonddl.generator.Generator", date = "2011-11-13T23:17:09")
public class Schema implements org.jsonddl.JsonDdlObject<Schema> {
  public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Schema> {
    private Schema obj;

    public Builder() {
      this(new Schema());
    }

    public Builder(Schema instance) {
      this.obj = instance;
    }

    @Override
    public Schema build() {
      Schema toReturn = obj;
      obj = null;
      return toReturn;
    }

    @Override
    public Builder from(java.util.Map<String, Object> map) {
      obj = obj.acceptMutable(org.jsonddl.JsonStringVisitor.fromJsonMap(map));
      return this;
    }

    @Override
    public Builder from(Schema from) {
      withModels(from.getModels());
      return this;
    }

    public Builder withModels(java.util.Map<java.lang.String, Model> value) {
      obj.models = org.jsonddl.impl.Protected.object(value);
      return this;
    }
  }

  private java.util.Map<java.lang.String, Model> models;

  private Schema() {}

  @Override
  public void accept(org.jsonddl.JsonDdlVisitor visitor) {
    new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
  }

  @Override
  public Schema acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
    return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this)
        .withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
  }

  @Override
  public Builder builder() {
    return newInstance().from(this);
  }

  /**
   * A map of object models by simple name.
   */
  public java.util.Map<java.lang.String, Model> getModels() {
    return models;
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
    new org.jsonddl.impl.ContextImpl.MapContext.Builder<Model>()
        .withKind(org.jsonddl.model.Kind.MAP)
        .withNestedKinds(
            java.util.Arrays.asList(org.jsonddl.model.Kind.STRING, org.jsonddl.model.Kind.DDL))
        .withLeafType(Model.class)
        .withMutability(false)
        .withProperty("models")
        .withValue(this.models)
        .build().traverse(visitor);

  }

  @Override
  public Schema traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
    Builder builder = newInstance();
    builder.withModels(
        new org.jsonddl.impl.ContextImpl.MapContext.Builder<Model>()
            .withKind(org.jsonddl.model.Kind.MAP)
            .withNestedKinds(
                java.util.Arrays.asList(org.jsonddl.model.Kind.STRING, org.jsonddl.model.Kind.DDL))
            .withLeafType(Model.class)
            .withMutability(true)
            .withProperty("models")
            .withValue(this.models)
            .build().traverse(visitor));

    return builder.build();
  }
}
