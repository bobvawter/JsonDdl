package org.jsonddl.generator.model;
/**
     * A model represents a single kind of object within the schema. There is a
     * 1:1 correspondence between Models and Java classes or JS type closures.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-10T01:24:56")
public class Model implements org.jsonddl.JsonDdlObject<Model> {
private String comment;
public String getComment() {return comment;}
private String name;
public String getName() {return name;}
private java.util.List<Property> properties;
public java.util.List<Property> getProperties() {return properties;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Model> {
private Model obj = new Model();
public Model build() {
Model toReturn = obj;
obj = null;
return toReturn;
}
public Builder withComment(String value) { obj.comment = value; return this;}
public Builder withName(String value) { obj.name = value; return this;}
public Builder withProperties(java.util.List<Property> value) { obj.properties = value; return this;}
public Builder from(Model from) {
withComment(from.getComment());
withName(from.getName());
withProperties(from.getProperties());
return this;}
}
private Model(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.Context.ImmutableContext<Model>(null,this).traverse(visitor);
}
public Model acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.Context.SettableContext<Model>(null,this).traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor, org.jsonddl.Context<Model> ctx) {
if (org.jsonddl.VisitSupport.visit(visitor, this,ctx)) {
new org.jsonddl.Context.ImmutableListContext<Property>("properties", this.properties).traverse(visitor);

}
org.jsonddl.VisitSupport.endVisit(visitor, this, ctx);
}
public Model traverseMutable(org.jsonddl.JsonDdlVisitor visitor, org.jsonddl.Context<Model> ctx) {
Builder builder = builder();
if (org.jsonddl.VisitSupport.visit(visitor, this,ctx)) {
builder.withProperties(new org.jsonddl.Context.ListContext<Property>("properties", this.properties).traverse(visitor));

}
org.jsonddl.VisitSupport.endVisit(visitor, this, ctx);
return builder.build();
}
}
