package org.jsonddl.generator.model;
/**
     * A property is a pair of a name and a type.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-12T13:46:35")
public class Property implements org.jsonddl.JsonDdlObject<Property> {
private String comment;
/**
	 * This comment will be stored in this property.
	 */
public String getComment() {return comment;}
private String name;
public String getName() {return name;}
private Type type;
public Type getType() {return type;}
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Property> {
private Property obj = new Property();
public Property build() {
Property toReturn = obj;
obj = null;
return toReturn;
}
public Builder withComment(String value) { obj.comment = value; return this;}
public Builder withName(String value) { obj.name = value; return this;}
public Builder withType(Type value) { obj.type = value; return this;}
public Builder from(Property from) {
withComment(from.getComment());
withName(from.getName());
withType(from.getType());
return this;}
}
private Property(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.Context.ObjectContext.Builder<Property>().withValue(this).build().traverse(visitor);
}
public Property acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.Context.ObjectContext.Builder<Property>().withValue(this).withMutability(true).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.Context.ValueContext.Builder<String>()
.withMutability(false)
.withProperty("comment")
.withValue(this.comment)
.build().traverse(visitor);
new org.jsonddl.Context.ValueContext.Builder<String>()
.withMutability(false)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor);
new org.jsonddl.Context.ObjectContext.Builder<Type>()
.withMutability(false)
.withProperty("type")
.withValue(this.type)
.build().traverse(visitor);

}
public Property traverseMutable(org.jsonddl.JsonDdlVisitor visitor) {
Builder builder = newInstance();
builder.withComment(
new org.jsonddl.Context.ValueContext.Builder<String>()
.withMutability(true)
.withProperty("comment")
.withValue(this.comment)
.build().traverse(visitor));
builder.withName(
new org.jsonddl.Context.ValueContext.Builder<String>()
.withMutability(true)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor));
builder.withType(
new org.jsonddl.Context.ObjectContext.Builder<Type>()
.withMutability(true)
.withProperty("type")
.withValue(this.type)
.build().traverse(visitor));

return builder.build();
}
}
