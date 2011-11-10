package org.jsonddl.generator.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-10T00:59:41")
public class Schema implements org.jsonddl.JsonDdlObject<Schema> {
private java.util.Map<String,Model> models;
public java.util.Map<String,Model> getModels() {return models;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Schema> {
private Schema obj = new Schema();
public Schema build() {
Schema toReturn = obj;
obj = null;
return toReturn;
}
public Builder withModels(java.util.Map<String,Model> value) { obj.models = value; return this;}
public Builder from(Schema from) {
withModels(from.getModels());
return this;}
}
private Schema(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.Context.ImmutableContext<Schema>(null,this).traverse(visitor);
}
public Schema acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.Context.SettableContext<Schema>(null,this).traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor, org.jsonddl.Context<Schema> ctx) {
if (org.jsonddl.VisitSupport.visit(visitor, this,ctx)) {
new org.jsonddl.Context.ImmutableMapContext<Model>("models", this.models).traverse(visitor);

}
org.jsonddl.VisitSupport.endVisit(visitor, this, ctx);
}
public Schema traverseMutable(org.jsonddl.JsonDdlVisitor visitor, org.jsonddl.Context<Schema> ctx) {
Builder builder = builder();
if (org.jsonddl.VisitSupport.visit(visitor, this,ctx)) {
builder.withModels(new org.jsonddl.Context.MapContext<Model>("models", this.models).traverse(visitor));

}
org.jsonddl.VisitSupport.endVisit(visitor, this, ctx);
return builder.build();
}
}
