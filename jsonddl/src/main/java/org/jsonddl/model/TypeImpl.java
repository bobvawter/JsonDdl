package org.jsonddl.model;
@javax.annotation.Generated(value="org.jsonddl.generator.Generator", date="2011-11-18T23:32:13")
class TypeImpl implements org.jsonddl.impl.Traversable<Type>, Type {
protected TypeImpl() {}
public Class<Type> getDdlObjectType() { return Type.class;}
java.lang.String name;
public java.lang.String getName() {return name;}
Kind kind;
public Kind getKind() {return kind;}
Type listElement;
public Type getListElement() {return listElement;}
Type mapKey;
public Type getMapKey() {return mapKey;}
Type mapValue;
public Type getMapValue() {return mapValue;}
public Type accept(org.jsonddl.JsonDdlVisitor visitor) {
return new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>().withValue(this).withKind(org.jsonddl.model.Kind.DDL).build().traverse(visitor);
}
public Type.Builder builder() { return newInstance().from(this); }
public Type.Builder newInstance() { return new Type.Builder(); }
public java.util.Map<String,Object> toJsonObject() { return org.jsonddl.impl.JsonMapVisitor.toJsonObject(this); }
public Type traverse(org.jsonddl.JsonDdlVisitor visitor) {
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<java.lang.String>()
.withKind(org.jsonddl.model.Kind.STRING)
.withLeafType(java.lang.String.class)
.withMutability(false)
.withProperty("name")
.withValue(this.name)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ValueContext.Builder<Kind>()
.withKind(org.jsonddl.model.Kind.ENUM)
.withLeafType(Kind.class)
.withMutability(false)
.withProperty("kind")
.withValue(this.kind)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(false)
.withProperty("listElement")
.withValue(this.listElement)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(false)
.withProperty("mapKey")
.withValue(this.mapKey)
.build().traverse(visitor);
new org.jsonddl.impl.ContextImpl.ObjectContext.Builder<Type>()
.withKind(org.jsonddl.model.Kind.DDL)
.withLeafType(Type.class)
.withMutability(false)
.withProperty("mapValue")
.withValue(this.mapValue)
.build().traverse(visitor);
return this;
}
}
