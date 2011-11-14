package org.jsonddl.generator;

import java.util.EnumMap;
import java.util.List;
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
    if (contextTypes.get(Kind.EXTERNAL).equals(contextBuilderType)) {
      sb.append(getParameterizedQualifiedSourceName(type));
    } else {
      sb.append(getContextParameterization(type));
    }
    sb.append(">");
    return sb.toString();
  }

  public static Class<?> getContextBuilderType(Type type) {
    switch (type.getKind()) {
      case LIST:
        if (Kind.DDL.equals(type.getListElement().getKind())) {
          return contextTypes.get(Kind.LIST);
        }
        return contextTypes.get(Kind.EXTERNAL);
      case MAP:
        if (Kind.DDL.equals(type.getMapValue().getKind())) {
          return contextTypes.get(Kind.MAP);
        }
        return contextTypes.get(Kind.EXTERNAL);
    }
    return contextTypes.get(type.getKind());
  }

  public static String getContextParameterization(Type type) {
    switch (type.getKind()) {
      case LIST:
        return type.getListElement().getName();
      case MAP:
        return type.getMapValue().getName();
    }
    return getParameterizedQualifiedSourceName(type);
  }

  public static String getParameterizedQualifiedSourceName(Type type) {
    switch (type.getKind()) {
      case LIST:
        return String.format("%s<%s>", getQualifiedSourceName(type),
            getParameterizedQualifiedSourceName(type.getListElement()));
      case MAP:
        return String.format("%s<%s,%s>", getQualifiedSourceName(type),
            getParameterizedQualifiedSourceName(type.getMapKey()),
            getParameterizedQualifiedSourceName(type.getMapValue()));
    }
    return getQualifiedSourceName(type);
  }

  public static String getQualifiedSourceName(Type type) {
    switch (type.getKind()) {
      case BOOLEAN:
        return Boolean.class.getCanonicalName();
      case DOUBLE:
        return Double.class.getCanonicalName();
      case INTEGER:
        return Integer.class.getCanonicalName();
      case STRING:
        return String.class.getCanonicalName();
      case DDL:
      case ENUM:
      case EXTERNAL:
        return type.getName();
      case LIST:
        return List.class.getCanonicalName();
      case MAP:
        return Map.class.getCanonicalName();
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
