package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-14T00:36:14")
public class Type implements org.jsonddl.JsonDdlObject<Type> {
private java.lang.String name;
public java.lang.String getName() {return name;}
private Kind kind;
public Kind getKind() {return kind;}
private Type listElement;
public Type getListElement() {return listElement;}
private Type mapKey;
public Type getMapKey() {return mapKey;}
private Type mapValue;
public Type getMapValue() {return mapValue;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Type> {
private Type obj;
public Builder() {this(new Type());}
public Builder(Type instance) {this.obj = instance;}
public Type build() {
Type toReturn = obj;
obj = null;
return toReturn;
}
public Builder withName(java.lang.String value) { obj.name = value;return this;}
public Builder withKind(Kind value) { obj.kind = value;return this;}
public Builder withListElement(Type value) { obj.listElement = value;return this;}
public Builder withMapKey(Type value) { obj.mapKey = value;return this;}
public Builder withMapValue(Type value) { obj.mapValue = value;return this;}
public Builder acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>().withValue(obj).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
return this;
}
public Builder from(Type from) {
withName(from.getName());
withKind(from.getKind());
withListElement(from.getListElement());
withMapKey(from.getMapKey());
withMapValue(from.getMapValue());
return this;}
public Builder from(java.util.Map<String, Object> map){
acceptMutable(org.jsonddl.JsonStringVisitor.fromJsonMap(map));
return this;
}
public Builder traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
withName(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(true)
.withProperty("name")
.withValue(obj.name)
.build().traverse(visitor));
withKind(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<Kind>()
.withKind(org.jsonddl.model.Kind.ENUM)
.withLeafType(Kind.class)
.withMutability(true)
.withProperty("kind")
.withValue(obj.kind)
.build().traverse(visitor));
withListElement(
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(true)
.withProperty("listElement")
.withValue(obj.listElement)
.build().traverse(visitor));
withMapKey(
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(true)
.withProperty("mapKey")
.withValue(obj.mapKey)
.build().traverse(visitor));
withMapValue(
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(true)
.withProperty("mapValue")
.withValue(obj.mapValue)
.build().traverse(visitor));

return this;
}
}
private Type(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public String toJson() { return org.jsonddl.JsonStringVisitor.toJsonString(this); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(false)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<Kind>()
.withKind(org.jsonddl.model.Kind.ENUM)
.withLeafType(Kind.class)
.withMutability(false)
.withProperty("kind")
.withValue(this.kind)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(false)
.withProperty("listElement")
.withValue(this.listElement)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(false)
.withProperty("mapKey")
.withValue(this.mapKey)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(false)
.withProperty("mapValue")
.withValue(this.mapValue)
.build().traverse(visitor);

}
}
