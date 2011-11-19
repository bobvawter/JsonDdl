package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-18T23:32:13")
class SchemaImpl implements org.jsonddl.impl.Traversable<Schema>, Schema {
protected SchemaImpl() {}
public Class<Schema> getDdlObjectType() { return Schema.class;}
java.util.Map<java.lang.String,Model> models;
public java.util.Map<java.lang.String,Model> getModels() {return models;}
public Schema accept(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Schema.Builder builder() { return newInstance().from(this); }
public Schema.Builder newInstance() { return new Schema.Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.impl.JsonMapVisitor.toJsonObject(this); }
public Schema traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.MapContext.Builder<Model>()
.withKind(org.jsonddl.model.Kind.MAP)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.STRING, org.jsonddl.model.Kind.DDL))
.withLeafType(Model.class)
.withMutability(false)
.withProperty("models")
.withValue(this.models)
.build().traverse(visitor);
return this;
}
}
