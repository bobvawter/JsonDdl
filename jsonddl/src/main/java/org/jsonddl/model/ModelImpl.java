package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-18T23:32:13")
class ModelImpl implements org.jsonddl.impl.Traversable<Model>, Model {
protected ModelImpl() {}
public Class<Model> getDdlObjectType() { return Model.class;}
java.lang.String comment;
public java.lang.String getComment() {return comment;}
java.util.List<EnumValue> enumValues;
public java.util.List<EnumValue> getEnumValues() {return enumValues;}
java.lang.String name;
public java.lang.String getName() {return name;}
java.util.List<Property> properties;
public java.util.List<Property> getProperties() {return properties;}
public Model accept(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Model>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Model.Builder builder() { return newInstance().from(this); }
public Model.Builder newInstance() { return new Model.Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.impl.JsonMapVisitor.toJsonObject(this); }
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
