package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-16T21:33:15")
public interface Type extends org.jsonddl.JsonDdlObject<Type> {
java.lang.String getName();
Kind getKind();
Type getListElement();
Type getMapKey();
Type getMapValue();
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Type>, Type {
private TypeImpl obj;
public Builder() {this(new TypeImpl());}
Builder(TypeImpl instance) {this.obj = instance;}
public Type.Builder builder() { return this; }
public Class<Type> getDdlObjectType() { return Type.class;}
public Type.Builder newInstance() { return new Type.Builder(); }
public java.util.Map<String, Object> toJsonObject() { return obj.toJsonObject(); }
public java.lang.String getName() { return obj.name; }
public void setName(java.lang.String value) { withName(value);}public Type.Builder withName(java.lang.String value) { obj.name = value;return this;}
public Kind getKind() { return obj.kind; }
public void setKind(Kind value) { withKind(value);}public Type.Builder withKind(Kind value) { obj.kind = value;return this;}
public Type.Builder getListElement() {
Type.Builder toReturn = obj.listElement.builder();
obj.listElement = toReturn;
return toReturn;
}
public void setListElement(Type value) { withListElement(value);}public Type.Builder withListElement(Type value) { obj.listElement = value;return this;}
public Type.Builder getMapKey() {
Type.Builder toReturn = obj.mapKey.builder();
obj.mapKey = toReturn;
return toReturn;
}
public void setMapKey(Type value) { withMapKey(value);}public Type.Builder withMapKey(Type value) { obj.mapKey = value;return this;}
public Type.Builder getMapValue() {
Type.Builder toReturn = obj.mapValue.builder();
obj.mapValue = toReturn;
return toReturn;
}
public void setMapValue(Type value) { withMapValue(value);}public Type.Builder withMapValue(Type value) { obj.mapValue = value;return this;}
public Type.Builder accept(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor).builder().obj;
return this;
}
public Type build() {
TypeImpl toReturn = obj;
obj = null;
toReturn.listElement = org.jsonddl.impl.Protected.object(toReturn.listElement);
toReturn.mapKey = org.jsonddl.impl.Protected.object(toReturn.mapKey);
toReturn.mapValue = org.jsonddl.impl.Protected.object(toReturn.mapValue);
return toReturn;
}
public Type.Builder from(Type from) {
withName(from.getName());
withKind(from.getKind());
withListElement(from.getListElement());
withMapKey(from.getMapKey());
withMapValue(from.getMapValue());
return this;}
public Type.Builder from(java.util.Map<String, Object> map){
accept(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
return this;
}
public Type.Builder traverse(org.jsonddl.JsonDdlVisitor visitor) {
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
Builder builder();
Builder newInstance();
}
