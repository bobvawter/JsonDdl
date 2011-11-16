package org.jsonddl.model;
/**
     * A property is a pair of a name and a type.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-15T21:53:00")
public class Property implements org.jsonddl.JsonDdlObject<Property> {
private java.lang.String comment;
/**
	 * This comment will be stored in this property.
	 */
public java.lang.String getComment() {return comment;}
private java.lang.String name;
public java.lang.String getName() {return name;}
private Type type;
public Type getType() {return type;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Property> {
private Property obj;
public Builder() {this(new Property());}
public Builder(Property instance) {this.obj = instance;}
public Property build() {
Property toReturn = obj;
obj = null;
return toReturn;
}
public Builder withComment(java.lang.String value) { obj.comment = value;return this;}
public Builder withName(java.lang.String value) { obj.name = value;return this;}
public Builder withType(Type value) { obj.type = value;return this;}
public Builder acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Property>().withValue(obj).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
return this;
}
public Builder from(Property from) {
withComment(from.getComment());
withName(from.getName());
withType(from.getType());
return this;}
public Builder from(java.util.Map<String, Object> map){
acceptMutable(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
return this;
}
public Builder traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
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
private Property(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Property>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.JsonMapVisitor.toJsonObject(this); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(false)
.withProperty("comment")
.withValue(this.comment)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(false)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(false)
.withProperty("type")
.withValue(this.type)
.build().traverse(visitor);

}
}
