package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-16T21:33:15")
public interface EnumValue extends org.jsonddl.JsonDdlObject<EnumValue> {
java.lang.String getComment();
java.lang.String getName();
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<EnumValue>, EnumValue {
private EnumValueImpl obj;
public Builder() {this(new EnumValueImpl());}
Builder(EnumValueImpl instance) {this.obj = instance;}
public EnumValue.Builder builder() { return this; }
public Class<EnumValue> getDdlObjectType() { return EnumValue.class;}
public EnumValue.Builder newInstance() { return new EnumValue.Builder(); }
public java.util.Map<String, Object> toJsonObject() { return obj.toJsonObject(); }
public java.lang.String getComment() { return obj.comment; }
public void setComment(java.lang.String value) { withComment(value);}public EnumValue.Builder withComment(java.lang.String value) { obj.comment = value;return this;}
public java.lang.String getName() { return obj.name; }
public void setName(java.lang.String value) { withName(value);}public EnumValue.Builder withName(java.lang.String value) { obj.name = value;return this;}
public EnumValue.Builder accept(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor).builder().obj;
return this;
}
public EnumValue build() {
EnumValueImpl toReturn = obj;
obj = null;
return toReturn;
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
Builder builder();
Builder newInstance();
}
