package org.jsonddl.model;
/**
     * A Schema is the top-level object that encapsulates the normalized form of
     * a json-ddl schema.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-16T21:33:15")
public interface Schema extends org.jsonddl.JsonDdlObject<Schema> {
/**
	 * A map of object models by simple name.
	 */
java.util.Map<java.lang.String,Model> getModels();
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Schema>, Schema {
private SchemaImpl obj;
public Builder() {this(new SchemaImpl());}
Builder(SchemaImpl instance) {this.obj = instance;}
public Schema.Builder builder() { return this; }
public Class<Schema> getDdlObjectType() { return Schema.class;}
public Schema.Builder newInstance() { return new Schema.Builder(); }
public java.util.Map<String, Object> toJsonObject() { return obj.toJsonObject(); }
public java.util.Map<java.lang.String,Model> getModels() { return obj.models; }
public void setModels(java.util.Map<java.lang.String,Model> value) { withModels(value);}public Schema.Builder withModels(java.util.Map<java.lang.String,Model> value) { obj.models = value;return this;}
public Schema.Builder accept(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor).builder().obj;
return this;
}
public Schema build() {
SchemaImpl toReturn = obj;
obj = null;
toReturn.models = org.jsonddl.impl.Protected.object(toReturn.models);
return toReturn;
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
Builder builder();
Builder newInstance();
}
