package org.jsonddl.generator;

import java.util.EnumMap;
import java.util.Map;

import org.jsonddl.impl.ContextImpl;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Type;

class TypeAnswers {
  private static final Map<Kind, Class<?>> contextTypes = new EnumMap<Kind, Class<?>>(
      Kind.class);
  static {
    contextTypes.put(Kind.BOOLEAN, ContextImpl.ValueContext.Builder.class);
    contextTypes.put(Kind.DDL, ContextImpl.ObjectContext.Builder.class);
    contextTypes.put(Kind.DOUBLE, ContextImpl.ValueContext.Builder.class);
    contextTypes.put(Kind.ENUM, ContextImpl.ValueContext.Builder.class);
    contextTypes.put(Kind.EXTERNAL, ContextImpl.ValueContext.Builder.class);
    contextTypes.put(Kind.INTEGER, ContextImpl.ValueContext.Builder.class);
    contextTypes.put(Kind.LIST, ContextImpl.ListContext.Builder.class);
    contextTypes.put(Kind.MAP, ContextImpl.MapContext.Builder.class);
    contextTypes.put(Kind.STRING, ContextImpl.ValueContext.Builder.class);
  }

  public static String getContextBuilderDeclaration(Type type) {
    StringBuilder sb = new StringBuilder();
    Class<?> contextBuilderType = getContextBuilderType(type);
    sb.append(contextBuilderType.getCanonicalName());
    sb.append("<");
    if (getContextBuilderType(Kind.EXTERNAL).equals(contextBuilderType)) {
      sb.append(getQualifiedSourceName(type));
    } else {
      sb.append(getContextParameterization(type));
    }
    sb.append(">");
    return sb.toString();
  }

  public static Class<?> getContextBuilderType(Kind kind) {
    return contextTypes.get(kind);
  }

  public static Class<?> getContextBuilderType(Type type) {
    switch (type.getKind()) {
      case LIST:
        if (Kind.DDL.equals(type.getListElement().getKind())) {
          return getContextBuilderType(Kind.LIST);
        }
        return getContextBuilderType(Kind.EXTERNAL);
      case MAP:
        if (Kind.DDL.equals(type.getMapValue().getKind())) {
          return getContextBuilderType(Kind.MAP);
        }
        return getContextBuilderType(Kind.EXTERNAL);
    }
    return getContextBuilderType(type.getKind());
  }

  public static String getContextParameterization(Type type) {
    switch (type.getKind()) {
      case LIST:
        return type.getListElement().getName();
      case MAP:
        return type.getMapValue().getName();
    }
    return getQualifiedSourceName(type);
  }

  public static String getQualifiedSourceName(Type type) {
    switch (type.getKind()) {
      case BOOLEAN:
        return "Boolean";
      case DOUBLE:
        return "Double";
      case INTEGER:
        return "Integer";
      case STRING:
        return "String";
      case DDL:
      case ENUM:
      case EXTERNAL:
        return type.getName();
      case LIST:
        return String.format("java.util.List<%s>", getQualifiedSourceName(type.getListElement()));
      case MAP:
        return String.format("java.util.Map<%s,%s>", getQualifiedSourceName(type.getMapKey()),
            getQualifiedSourceName(type.getMapValue()));
    }
    throw new UnsupportedOperationException(type.toString());
  }

  public static boolean shouldProtect(Type type) {
    switch (type.getKind()) {
      case LIST:
      case MAP:
        return true;
    }
    return false;
  }
}
