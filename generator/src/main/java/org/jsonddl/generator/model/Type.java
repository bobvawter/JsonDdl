package org.jsonddl.generator.model;
/**
     * This is a simplified type system, representing only the types of data
     * that can be directly expressed in JSON.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-12T13:46:35")
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
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>().withValue(this).build().traverse(visitor);
}
public Type acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>().withValue(this).withMutability(true).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<String>()
.withMutability(false)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<Kind>()
.withMutability(false)
.withProperty("kind")
.withValue(this.kind)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withMutability(false)
.withProperty("listElement")
.withValue(this.listElement)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withMutability(false)
.withProperty("mapKey")
.withValue(this.mapKey)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withMutability(false)
.withProperty("mapValue")
.withValue(this.mapValue)
.build().traverse(visitor);

}
public Type traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
Builder builder = newInstance();
builder.withName(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<String>()
.withMutability(true)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor));
builder.withKind(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<Kind>()
.withMutability(true)
.withProperty("kind")
.withValue(this.kind)
.build().traverse(visitor));
builder.withListElement(
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withMutability(true)
.withProperty("listElement")
.withValue(this.listElement)
.build().traverse(visitor));
builder.withMapKey(
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withMutability(true)
.withProperty("mapKey")
.withValue(this.mapKey)
.build().traverse(visitor));
builder.withMapValue(
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withMutability(true)
.withProperty("mapValue")
.withValue(this.mapValue)
.build().traverse(visitor));

return builder.build();
}
}
