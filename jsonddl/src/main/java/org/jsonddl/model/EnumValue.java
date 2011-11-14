package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-13T15:59:39")
public class EnumValue implements org.jsonddl.JsonDdlObject<EnumValue> {
private String comment;
public String getComment() {return comment;}
private String name;
public String getName() {return name;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<EnumValue> {
private EnumValue obj = new EnumValue();
public EnumValue build() {
EnumValue toReturn = obj;
obj = null;
return toReturn;
}
public Builder withComment(String value) { obj.comment = value;return this;}
public Builder withName(String value) { obj.name = value;return this;}
public Builder from(EnumValue from) {
withComment(from.getComment());
withName(from.getName());
return this;}
}
private EnumValue(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public EnumValue acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<EnumValue>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public String toJson() { return org.jsonddl.JsonStringVisitor.toJsonString(this); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withMutability(false)
.withProperty("comment")
.withValue(this.comment)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withMutability(false)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor);

}
public EnumValue traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
Builder builder = newInstance();
builder.withComment(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withMutability(true)
.withProperty("comment")
.withValue(this.comment)
.build().traverse(visitor));
builder.withName(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withMutability(true)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor));

return builder.build();
}
}
