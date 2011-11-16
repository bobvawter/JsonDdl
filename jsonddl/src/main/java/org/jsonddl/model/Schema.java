package org.jsonddl.model;
/**
     * A Schema is the top-level object that encapsulates the normalized form of
     * a json-ddl schema.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-16T00:09:10")
public interface Schema extends org.jsonddl.JsonDdlObject<Schema> {
/**
	 * A map of object models by simple name.
	 */
java.util.Map<java.lang.String,Model> getModels();
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Schema>, Schema {
private Schema.Impl obj;
public Builder() {this(new Schema.Impl());}
public Builder(Schema.Impl instance) {this.obj = instance;}
public Schema build() {
Schema toReturn = obj;
obj = null;
return toReturn;
}
public Schema.Builder builder() { return this; }
public Class<Schema> getDdlObjectType() { return Schema.class;}
public Schema.Builder newInstance() { return new Schema.Builder(); }
public java.util.Map<String, Object> toJsonObject() { return obj.toJsonObject(); }
public java.util.Map<java.lang.String,Model> getModels() { return obj.models; }
public Schema.Builder withModels(java.util.Map<java.lang.String,Model> value) { obj.models = org.jsonddl.impl.Protected.object(value);return this;}
public Schema.Builder accept(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor).builder().obj;
return this;
}
public Schema.Builder from(Schema from) {
withModels(from.getModels());
return this;}
public Schema.Builder from(java.util.Map<String, Object> map){
accept(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
return this;
}
public Schema.Builder traverse(org.jsonddl.JsonDdlVisitor visitor) {
withModels(
new org.jsonddl.impl.ContextImpl.MapContext.Builder<Model>()
.withKind(org.jsonddl.model.Kind.MAP)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.STRING, org.jsonddl.model.Kind.DDL))
.withLeafType(Model.class)
.withMutability(true)
.withProperty("models")
.withValue(obj.models)
.build().traverse(visitor));

return this;
}
}
public static class Impl implements Schema {
protected Impl() {}
public Class<Schema> getDdlObjectType() { return Schema.class;}
private java.util.Map<java.lang.String,Model> models;
public java.util.Map<java.lang.String,Model> getModels() {return models;}
public Schema accept(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Schema.Builder builder() { return newInstance().from(this); }
public Schema.Builder newInstance() { return new Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.JsonMapVisitor.toJsonObject(this); }
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
Builder builder();
Builder newInstance();
}
