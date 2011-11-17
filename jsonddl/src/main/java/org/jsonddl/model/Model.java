package org.jsonddl.model;
/**
     * A model represents a single kind of object within the schema. There is a
     * 1:1 correspondence between Models and Java classes or JS type closures.
     */
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-16T21:33:15")
public interface Model extends org.jsonddl.JsonDdlObject<Model> {
java.lang.String getComment();
java.util.List<EnumValue> getEnumValues();
java.lang.String getName();
java.util.List<Property> getProperties();
public static class Builder implements org.jsonddl.JsonDdlObject.Builder<Model>, Model {
private ModelImpl obj;
public Builder() {this(new ModelImpl());}
Builder(ModelImpl instance) {this.obj = instance;}
public Model.Builder builder() { return this; }
public Class<Model> getDdlObjectType() { return Model.class;}
public Model.Builder newInstance() { return new Model.Builder(); }
public java.util.Map<String, Object> toJsonObject() { return obj.toJsonObject(); }
public java.lang.String getComment() { return obj.comment; }
public void setComment(java.lang.String value) { withComment(value);}public Model.Builder withComment(java.lang.String value) { obj.comment = value;return this;}
public java.util.List<EnumValue> getEnumValues() { return obj.enumValues; }
public void setEnumValues(java.util.List<EnumValue> value) { withEnumValues(value);}public Model.Builder withEnumValues(java.util.List<EnumValue> value) { obj.enumValues = value;return this;}
public java.lang.String getName() { return obj.name; }
public void setName(java.lang.String value) { withName(value);}public Model.Builder withName(java.lang.String value) { obj.name = value;return this;}
public java.util.List<Property> getProperties() { return obj.properties; }
public void setProperties(java.util.List<Property> value) { withProperties(value);}public Model.Builder withProperties(java.util.List<Property> value) { obj.properties = value;return this;}
public Model.Builder accept(org.jsonddl.JsonDdlVisitor visitor) {
obj = new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Model>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).withMutability(true).build().traverse(visitor).builder().obj;
return this;
}
public Model build() {
ModelImpl toReturn = obj;
obj = null;
toReturn.enumValues = org.jsonddl.impl.Protected.object(toReturn.enumValues);
toReturn.properties = org.jsonddl.impl.Protected.object(toReturn.properties);
return toReturn;
}
public Model.Builder from(Model from) {
withComment(from.getComment());
withEnumValues(from.getEnumValues());
withName(from.getName());
withProperties(from.getProperties());
return this;}
public Model.Builder from(java.util.Map<String, Object> map){
accept(org.jsonddl.JsonMapVisitor.fromJsonMap(map));
return this;
}
public Model.Builder traverse(org.jsonddl.JsonDdlVisitor visitor) {
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
Builder builder();
Builder newInstance();
}
