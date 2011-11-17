package org.jsonddl.model;
/**
     * A property is a pair of a name and a type.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-16T21:33:15")
public interface Property extends org.jsonddl.JsonDdlObject<Property> {
/**
	 * This comment will be stored in this property.
	 */
java.lang.String getComment();
java.lang.String getName();
Type getType();
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Property>, Property {
private PropertyImpl obj;
public Builder() {this(new PropertyImpl());}
Builder(PropertyImpl instance) {this.obj = instance;}
public Property.Builder builder() { return this; }
public Class<Property> getDdlObjectType() { return Property.class;}
public Property.Builder newInstance() { return new Property.Builder(); }
public java.util.Map<String, Object> toJsonObject() { return obj.toJsonObject(); }
public java.lang.String getComment() { return obj.comment; }
public void setComment(java.lang.String value) { withComment(value);}public Property.Builder withComment(java.lang.String value) { obj.comment = value;return this;}
public java.lang.String getName() { return obj.name; }
public void setName(java.lang.String value) { withName(value);}public Property.Builder withName(java.lang.String value) { obj.name = value;return this;}
public Type.Builder getType() {
Type.Builder toReturn = obj.type.builder();
obj.type = toReturn;
return toReturn;
}
public void setType(Type value) { withType(value);}public Property.Builder withType(Type value) { obj.type = value;return this;}
public Property.Builder accept(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Property>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor).builder().obj;
return this;
}
public Property build() {
PropertyImpl toReturn = obj;
obj = null;
toReturn.type = org.jsonddl.impl.Protected.object(toReturn.type);
return toReturn;
}
public Property.Builder from(Property from) {
withComment(from.getComment());
withName(from.getName());
withType(from.getType());
return this;}
public Property.Builder from(java.util.Map<String, Object> map){
accept(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
return this;
}
public Property.Builder traverse(org.jsonddl.JsonDdlVisitor visitor) {
withComment(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(true)
.withProperty("comment")
.withValue(obj.comment)
.build().traverse(visitor));
withName(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(true)
.withProperty("name")
.withValue(obj.name)
.build().traverse(visitor));
withType(
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(true)
.withProperty("type")
.withValue(obj.type)
.build().traverse(visitor));
return this;
}
}
Builder builder();
Builder newInstance();
}
