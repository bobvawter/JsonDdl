package org.jsonddl.generator;

import org.jsonddl.generator.model.Kind;
import org.jsonddl.generator.model.Type;

class TypeAnswers {
  public static String getContextBuilderDeclaration(Type type) {
    StringBuilder sb = new StringBuilder();
    Class<?> contextBuilderType = getContextBuilderType(type);
    sb.append(contextBuilderType.getCanonicalName());
    sb.append("<");
    if (Kind.EXTERNAL.getContextBuilderType().equals(contextBuilderType)) {
      sb.append(getQualifiedSourceName(type));
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
          return Kind.LIST.getContextBuilderType();
        }
        return Kind.EXTERNAL.getContextBuilderType();
      case MAP:
        if (Kind.DDL.equals(type.getMapValue().getKind())) {
          return Kind.MAP.getContextBuilderType();
        }
        return Kind.EXTERNAL.getContextBuilderType();
    }
    return type.getKind().getContextBuilderType();
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
      case DDL:
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
}
