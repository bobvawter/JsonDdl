package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-14T00:36:14")
public class EnumValue implements org.jsonddl.JsonDdlObject<EnumValue> {
private java.lang.String comment;
public java.lang.String getComment() {return comment;}
private java.lang.String name;
public java.lang.String getName() {return name;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<EnumValue> {
private EnumValue obj;
public Builder() {this(new EnumValue());}
public Builder(EnumValue instance) {this.obj = instance;}
public EnumValue build() {
EnumValue toReturn = obj;
obj = null;
return toReturn;
}
public Builder withComment(java.lang.String value) { obj.comment = value;return this;}
public Builder withName(java.lang.String value) { obj.name = value;return this;}
public Builder acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(obj).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
return this;
}
public Builder from(EnumValue from) {
withComment(from.getComment());
withName(from.getName());
return this;}
public Builder from(java.util.Map<String, Object> map){
acceptMutable(org.jsonddl.JsonStringVisitor.fromJsonMap(map));
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

return this;
}
}
private EnumValue(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public String toJson() { return org.jsonddl.JsonStringVisitor.toJsonString(this); }
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

}
}
