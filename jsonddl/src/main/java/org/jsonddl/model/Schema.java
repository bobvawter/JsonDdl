package org.jsonddl.model;
/**
     * A Schema is the top-level object that encapsulates the normalized form of
     * a json-ddl schema.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-15T21:53:00")
public class Schema implements org.jsonddl.JsonDdlObject<Schema> {
private java.util.Map<java.lang.String,Model> models;
/**
	 * A map of object models by simple name.
	 */
public java.util.Map<java.lang.String,Model> getModels() {return models;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Schema> {
private Schema obj;
public Builder() {this(new Schema());}
public Builder(Schema instance) {this.obj = instance;}
public Schema build() {
Schema toReturn = obj;
obj = null;
return toReturn;
}
public Builder withModels(java.util.Map<java.lang.String,Model> value) { obj.models = org.jsonddl.impl.Protected.object(value);return this;}
public Builder acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(obj).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
return this;
}
public Builder from(Schema from) {
withModels(from.getModels());
return this;}
public Builder from(java.util.Map<String, Object> map){
acceptMutable(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
return this;
}
public Builder traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
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
private Schema(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.JsonMapVisitor.toJsonObject(this); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.MapContext.Builder<Model>()
.withKind(org.jsonddl.model.Kind.MAP)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.STRING, org.jsonddl.model.Kind.DDL))
.withLeafType(Model.class)
.withMutability(false)
.withProperty("models")
.withValue(this.models)
.build().traverse(visitor);

}
}
