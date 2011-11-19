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
package org.jsonddl.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsonddl.JsonDdlObject;

public class Protected {
  private static final Class<?> unmodifiableListClass;
  private static final Class<?> unmodifiableMapClass;
  static {
    unmodifiableListClass = Collections.unmodifiableList(Arrays.asList(1, 2, 3)).getClass();
    Map<Object, Object> testMap = new HashMap<Object, Object>();
    testMap.put(new Object(), new Object());
    testMap.put(new Object(), new Object());
    unmodifiableMapClass = Collections.unmodifiableMap(testMap).getClass();
  }

  public static <J extends JsonDdlObject<J>> J object(J value) {
    if (value instanceof JsonDdlObject.Builder) {
      return ((JsonDdlObject.Builder<J>) value).build();
    }
    return value;
  }

  public static <T> List<T> object(List<T> values) {
    if (values == null) {
      return null;
    }
    switch (values.size()) {
      case 0:
        return Collections.emptyList();
      case 1:
        return Collections.singletonList(object(values.get(0)));
      default:
        if (unmodifiableListClass.equals(values.getClass())) {
          return values;
        }
        ArrayList<T> toReturn = new ArrayList<T>(values.size());
        for (T value : values) {
          toReturn.add(object(value));
        }
        return Collections.unmodifiableList(toReturn);
    }
  }

  public static <K, V> Map<K, V> object(Map<K, V> values) {
    if (values == null) {
      return null;
    }
    switch (values.size()) {
      case 0:
        return Collections.emptyMap();
      case 1: {
        Map.Entry<K, V> entry = values.entrySet().iterator().next();
        return Collections.singletonMap(object(entry.getKey()), object(entry.getValue()));
      }
      default: {
        if (unmodifiableMapClass.equals(values.getClass())) {
          return values;
        }
        Map<K, V> toReturn = new HashMap<K, V>();
        for (Map.Entry<K, V> entry : values.entrySet()) {
          toReturn.put(object(entry.getKey()), object(entry.getValue()));
        }
        return Collections.unmodifiableMap(toReturn);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T object(T value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Boolean) {
      return value;
    }
    if (value instanceof Number) {
      return value;
    }
    if (value instanceof String) {
      return value;
    }
    if (value instanceof Enum<?>) {
      return value;
    }
    if (value instanceof JsonDdlObject.Builder) {
      return (T) ((JsonDdlObject.Builder<?>) value).build();
    }
    if (value instanceof JsonDdlObject) {
      return value;
    }
    if (value instanceof List) {
      return (T) object((List<?>) value);
    }
    if (value instanceof Map) {
      return (T) object((Map<?, ?>) value);
    }

    throw new IllegalArgumentException("Cannot protect type of " + value.getClass().getName());
  }
}
