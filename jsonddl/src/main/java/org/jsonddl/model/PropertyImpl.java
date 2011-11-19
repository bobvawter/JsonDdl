package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-18T23:32:13")
class PropertyImpl implements org.jsonddl.impl.Traversable<Property>, Property {
protected PropertyImpl() {}
public Class<Property> getDdlObjectType() { return Property.class;}
java.lang.String comment;
public java.lang.String getComment() {return comment;}
java.lang.String name;
public java.lang.String getName() {return name;}
Type type;
public Type getType() {return type;}
public Property accept(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Property>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Property.Builder builder() { return newInstance().from(this); }
public Property.Builder newInstance() { return new Property.Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.impl.JsonMapVisitor.toJsonObject(this); }
public Property traverse(org.jsonddl.JsonDdlVisitor visitor) {
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
return this;
}
}
