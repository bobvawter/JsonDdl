package org.jsonddl.generator.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-09T23:40:52")
public class Property implements org.jsonddl.JsonDdlObject<Property> {
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
public Builder withName(String value) { obj.name = value; return this;}
public Builder withType(Type value) { obj.type = value; return this;}
public Builder from(Property from) {
withName(from.getName());
withType(from.getType());
return this;}
}
private Property(){}
public void accept(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.Context.ImmutableContext<Property>(null,this).traverse(visitor);
}
public Property acceptMutable(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.Context.SettableContext<Property>(null,this).traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public void traverse(org.jsonddl.JsonDdlVisitor visitor, org.jsonddl.Context<Property> ctx) {
new org.jsonddl.Context.ImmutableContext<Type>("type", this.type).traverse(visitor);

}
public Property traverseMutable(org.jsonddl.JsonDdlVisitor visitor, org.jsonddl.Context<Property> ctx) {
Builder builder = builder();
builder.withType(new org.jsonddl.Context.SettableContext<Type>("type", this.type).traverse(visitor));

return builder.build();
}
}
