package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-18T23:32:13")
class EnumValueImpl implements org.jsonddl.impl.Traversable<EnumValue>, EnumValue {
protected EnumValueImpl() {}
public Class<EnumValue> getDdlObjectType() { return EnumValue.class;}
java.lang.String comment;
public java.lang.String getComment() {return comment;}
java.lang.String name;
public java.lang.String getName() {return name;}
public EnumValue accept(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public EnumValue.Builder builder() { return newInstance().from(this); }
public EnumValue.Builder newInstance() { return new EnumValue.Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.impl.JsonMapVisitor.toJsonObject(this); }
public EnumValue traverse(org.jsonddl.JsonDdlVisitor visitor) {
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
return this;
}
}
