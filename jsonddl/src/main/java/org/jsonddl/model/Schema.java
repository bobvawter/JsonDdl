package org.jsonddl.model;
/**
     * A Schema is the top-level object that encapsulates the normalized form of
     * a json-ddl schema.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-13T15:24:51")
public class Schema implements org.jsonddl.JsonDdlObject<Schema> {
private java.util.Map<String,Model> models;
/**
	 * A map of object models by simple name.
	 */
public java.util.Map<String,Model> getModels() {return models;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Schema> {
private Schema obj = new Schema();
public Schema build() {
Schema toReturn = obj;
obj = null;
return toReturn;
}
public Builder withModels(java.util.Map<String,Model> value) { obj.models = org.jsonddl.impl.Protected.object(value);return this;}
public Builder from(Schema from) {
withModels(from.getModels());
return this;}
}
private Schema(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Schema acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Schema>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.MapContext.Builder<Model>()
.withKind(org.jsonddl.model.Kind.MAP)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.STRING, org.jsonddl.model.Kind.DDL))
.withMutability(false)
.withProperty("models")
.withValue(this.models)
.build().traverse(visitor);

}
public Schema traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
Builder builder = newInstance();
builder.withModels(
new org.jsonddl.impl.ContextImpl.MapContext.Builder<Model>()
.withKind(org.jsonddl.model.Kind.MAP)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.STRING, org.jsonddl.model.Kind.DDL))
.withMutability(true)
.withProperty("models")
.withValue(this.models)
.build().traverse(visitor));

return builder.build();
}
}
