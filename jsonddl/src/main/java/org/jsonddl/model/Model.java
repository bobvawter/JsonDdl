package org.jsonddl.model;
/**
     * A model represents a single kind of object within the schema. There is a
     * 1:1 correspondence between Models and Java classes or JS type closures.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-15T23:47:54")
public interface Model extends org.jsonddl.JsonDdlObject {
java.lang.String getComment();
java.util.List<EnumValue> getEnumValues();
java.lang.String getName();
java.util.List<Property> getProperties();
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Model>, Model {
private Model.Impl obj;
public Builder() {this(new Model.Impl());}
public Builder(Model.Impl instance) {this.obj = instance;}
public Model build() {
Model toReturn = obj;
obj = null;
return toReturn;
}
public Builder builder() { return this; }
public Class<Model> getDdlObjectType() { return Model.class;}
public Builder newInstance() { return new Builder(); }
public java.util.Map<String, Object> toJsonObject() { return obj.toJsonObject(); }
public java.lang.String getComment() { return obj.comment; }
public Builder withComment(java.lang.String value) { obj.comment = value;return this;}
public java.util.List<EnumValue> getEnumValues() { return obj.enumValues; }
public Builder withEnumValues(java.util.List<EnumValue> value) { obj.enumValues = org.jsonddl.impl.Protected.object(value);return this;}
public java.lang.String getName() { return obj.name; }
public Builder withName(java.lang.String value) { obj.name = value;return this;}
public java.util.List<Property> getProperties() { return obj.properties; }
public Builder withProperties(java.util.List<Property> value) { obj.properties = org.jsonddl.impl.Protected.object(value);return this;}
public Builder accept(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Model>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor).builder().obj;
return this;
}
public Builder from(Model from) {
withComment(from.getComment());
withEnumValues(from.getEnumValues());
withName(from.getName());
withProperties(from.getProperties());
return this;}
public Builder from(java.util.Map<String, Object> map){
accept(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
return this;
}
public Builder traverse(org.jsonddl.JsonDdlVisitor visitor) {
withComment(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(true)
.withProperty("comment")
.withValue(obj.comment)
.build().traverse(visitor));
withEnumValues(
new org.jsonddl.impl.ContextImpl.ListContext.Builder<EnumValue>()
.withKind(org.jsonddl.model.Kind.LIST)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.DDL))
.withLeafType(EnumValue.class)
.withMutability(true)
.withProperty("enumValues")
.withValue(obj.enumValues)
.build().traverse(visitor));
withName(
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(true)
.withProperty("name")
.withValue(obj.name)
.build().traverse(visitor));
withProperties(
new org.jsonddl.impl.ContextImpl.ListContext.Builder<Property>()
.withKind(org.jsonddl.model.Kind.LIST)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.DDL))
.withLeafType(Property.class)
.withMutability(true)
.withProperty("properties")
.withValue(obj.properties)
.build().traverse(visitor));

return this;
}
}
public static class Impl implements Model {
protected Impl() {}
public Class<Model> getDdlObjectType() { return Model.class;}
private java.lang.String comment;
public java.lang.String getComment() {return comment;}
private java.util.List<EnumValue> enumValues;
public java.util.List<EnumValue> getEnumValues() {return enumValues;}
private java.lang.String name;
public java.lang.String getName() {return name;}
private java.util.List<Property> properties;
public java.util.List<Property> getProperties() {return properties;}
public Model accept(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Model>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Builder builder() { return newInstance().from(this); }
public Builder newInstance() { return new Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.JsonMapVisitor.toJsonObject(this); }
public Model traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(false)
.withProperty("comment")
.withValue(this.comment)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ListContext.Builder<EnumValue>()
.withKind(org.jsonddl.model.Kind.LIST)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.DDL))
.withLeafType(EnumValue.class)
.withMutability(false)
.withProperty("enumValues")
.withValue(this.enumValues)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(false)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ListContext.Builder<Property>()
.withKind(org.jsonddl.model.Kind.LIST)
.withNestedKinds(java.util.Arrays.asList(org.jsonddl.model.Kind.DDL))
.withLeafType(Property.class)
.withMutability(false)
.withProperty("properties")
.withValue(this.properties)
.build().traverse(visitor);

return this;
}
}
Builder builder();
Builder newInstance();
}
