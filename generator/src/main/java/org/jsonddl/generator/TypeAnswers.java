package org.jsonddl.generator;

import org.jsonddl.generator.model.Kind;
import org.jsonddl.generator.model.Type;

class TypeAnswers {
  public static boolean canTraverse(Type type) {
    switch (type.getKind()) {
      case DDL:
        return true;
      case LIST:
        return Kind.DDL.equals(type.getListElement().getKind());
    }
    return false;
  }

  public static String getContextParameterization(Type type) {
    switch (type.getKind()) {
      case DDL:
        return type.getName();
      case LIST:
        return type.getListElement().getName();
    }
    throw new IllegalStateException();
  }

  public static String getQualifiedSourceName(Type type) {
    switch (type.getKind()) {
      case DDL:
      case EXTERNAL:
      case PRIMITIVE:
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
