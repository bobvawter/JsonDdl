/*
 * Copyright 2011 Robert W. Vawter III <bob@vawter.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jsonddl.generator;

import java.util.List;
import java.util.Map;

import org.jsonddl.model.Type;

/**
 * A utility class for Type-related questions.
 */
public class TypeAnswers {
  public static String getParameterizedQualifiedSourceName(Type type) {
    switch (type.getKind()) {
      case LIST:
        return String.format("%s<%s>", getQualifiedSourceName(type),
            getParameterizedQualifiedSourceName(type.getListElement()));
      case MAP:
        return String.format("%s<%s, %s>", getQualifiedSourceName(type),
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

  /**
   * Returns {@code true} if the two types are equivalent.
   */
  public static boolean isSameType(Type a, Type b) {
    if (!a.getKind().equals(b.getKind())) {
      return false;
    }

    switch (a.getKind()) {
      case DDL:
      case ENUM:
      case EXTERNAL:
        return a.getName().equals(b.getName());
      case LIST:
        return isSameType(a.getListElement(), b.getListElement());
      case MAP:
        return isSameType(a.getMapKey(), b.getMapKey()) &&
          isSameType(a.getMapValue(), b.getMapValue());
    }

    return true;
  }

  public static boolean shouldProtect(Type type) {
    switch (type.getKind()) {
      case DDL:
      case LIST:
      case MAP:
        return true;
    }
    return false;
  }

  private TypeAnswers() {}
}
