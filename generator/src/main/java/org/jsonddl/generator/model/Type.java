package org.jsonddl.generator.model;
/**
     * This is a simplified type system, representing only the types of data
     * that can be directly expressed in JSON.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-10T01:24:56")
public class Type implements org.jsonddl.JsonDdlObject<Type> {
private String name;
public String getName() {return name;}
private Kind kind;
public Kind getKind() {return kind;}
private Type listElement;
public Type getListElement() {return listElement;}
private Type mapKey;
public Type getMapKey() {return mapKey;}
private Type mapValue;
public Type getMapValue() {return mapValue;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Type> {
private Type obj = new Type();
public Type build() {
Type toReturn = obj;
obj = null;
return toReturn;
}
public Builder withName(String value) { obj.name = value; return this;}
public Builder withKind(Kind value) { obj.kind = value; return this;}
public Builder withListElement(Type value) { obj.listElement = value; return this;}
public Builder withMapKey(Type value) { obj.mapKey = value; return this;}
public Builder withMapValue(Type value) { obj.mapValue = value; return this;}
public Builder from(Type from) {
withName(from.getName());
withKind(from.getKind());
withListElement(from.getListElement());
withMapKey(from.getMapKey());
withMapValue(from.getMapValue());
return this;}
}
private Type(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.Context.ImmutableContext<Type>(null,this).traverse(visitor);
}
public Type acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.Context.SettableContext<Type>(null,this).traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor, org.jsonddl.Context<Type> ctx) {
if (org.jsonddl.VisitSupport.visit(visitor, this,ctx)) {
new org.jsonddl.Context.ImmutableContext<Type>("listElement", this.listElement).traverse(visitor);
new org.jsonddl.Context.ImmutableContext<Type>("mapKey", this.mapKey).traverse(visitor);
new org.jsonddl.Context.ImmutableContext<Type>("mapValue", this.mapValue).traverse(visitor);

}
org.jsonddl.VisitSupport.endVisit(visitor, this, ctx);
}
public Type traverseMutable(org.jsonddl.JsonDdlVisitor visitor, org.jsonddl.Context<Type> ctx) {
Builder builder = builder();
if (org.jsonddl.VisitSupport.visit(visitor, this,ctx)) {
builder.withListElement(new org.jsonddl.Context.SettableContext<Type>("listElement", this.listElement).traverse(visitor));
builder.withMapKey(new org.jsonddl.Context.SettableContext<Type>("mapKey", this.mapKey).traverse(visitor));
builder.withMapValue(new org.jsonddl.Context.SettableContext<Type>("mapValue", this.mapValue).traverse(visitor));

}
org.jsonddl.VisitSupport.endVisit(visitor, this, ctx);
return builder.build();
}
}
