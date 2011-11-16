package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-16T00:09:10")
public interface EnumValue extends org.jsonddl.JsonDdlObject<EnumValue> {
java.lang.String getComment();
java.lang.String getName();
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<EnumValue>, EnumValue {
private EnumValue.Impl obj;
public Builder() {this(new EnumValue.Impl());}
public Builder(EnumValue.Impl instance) {this.obj = instance;}
public EnumValue build() {
EnumValue toReturn = obj;
obj = null;
return toReturn;
}
public EnumValue.Builder builder() { return this; }
public Class<EnumValue> getDdlObjectType() { return EnumValue.class;}
public EnumValue.Builder newInstance() { return new EnumValue.Builder(); }
public java.util.Map<String, Object> toJsonObject() { return obj.toJsonObject(); }
public java.lang.String getComment() { return obj.comment; }
public EnumValue.Builder withComment(java.lang.String value) { obj.comment = value;return this;}
public java.lang.String getName() { return obj.name; }
public EnumValue.Builder withName(java.lang.String value) { obj.name = value;return this;}
public EnumValue.Builder accept(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor).builder().obj;
return this;
}
public EnumValue.Builder from(EnumValue from) {
withComment(from.getComment());
withName(from.getName());
return this;}
public EnumValue.Builder from(java.util.Map<String, Object> map){
accept(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
return this;
}
public EnumValue.Builder traverse(org.jsonddl.JsonDdlVisitor visitor) {
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

return this;
}
}
public static class Impl implements EnumValue {
protected Impl() {}
public Class<EnumValue> getDdlObjectType() { return EnumValue.class;}
private java.lang.String comment;
public java.lang.String getComment() {return comment;}
private java.lang.String name;
public java.lang.String getName() {return name;}
public EnumValue accept(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public EnumValue.Builder builder() { return newInstance().from(this); }
public EnumValue.Builder newInstance() { return new Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.JsonMapVisitor.toJsonObject(this); }
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
Builder builder();
Builder newInstance();
}
