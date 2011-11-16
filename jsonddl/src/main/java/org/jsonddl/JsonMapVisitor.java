package org.jsonddl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsonddl.JsonDdlVisitor.PropertyVisitor;
import org.jsonddl.impl.Protected;
import org.jsonddl.model.Kind;

public class JsonMapVisitor {
  public static class Builder {
    JsonMapVisitor v = new JsonMapVisitor();

    public JsonMapVisitor build() {
      JsonMapVisitor toReturn = v;
      v = null;
      return toReturn;
    }

    public Builder withAddNulls(boolean addNulls) {
      v.includeNullProperties = addNulls;
      return this;
    }
  }

  class FromMapVisitor implements PropertyVisitor {
    private final Map<String, Object> map;

    public FromMapVisitor(Map<String, Object> map) {
      this.map = map;
    }

    @Override
    public <T> void endVisitProperty(T value, Context<T> ctx) {}

    @Override
    public <T> boolean visitProperty(T value, Context<T> ctx) {
      Mapper s = makeStringer(ctx.getKind(), ctx.getNestedKinds());
      Object property = map.get(ctx.getProperty());
      if (property != null) {
        @SuppressWarnings("unchecked")
        T newValue = (T) s.fromJson(property, ctx.getLeafType());
        ctx.replace(newValue);
      }
      return false;
    }
  }

  class JsonDdlMapper implements Mapper {
    @Override
    public JsonDdlObject fromJson(Object value, Class<?> leafType) {
      assert JsonDdlObject.class.isAssignableFrom(leafType);

      JsonDdlObject.Builder<?> builder = null;
      for (Class<?> clazz : leafType.getDeclaredClasses()) {
        if (JsonDdlObject.Builder.class.isAssignableFrom(clazz)) {
          try {
            builder = clazz.asSubclass(JsonDdlObject.Builder.class).newInstance();
            break;
          } catch (InstantiationException e) {
            // Should never happen since these are code-gen types
            throw new RuntimeException(e);
          } catch (IllegalAccessException e) {
            // Should never happen since these are code-gen types
            throw new RuntimeException(e);
          }
        }
      }
      @SuppressWarnings("unchecked")
      Map<String, Object> map = (Map<String, Object>) value;
      builder.from(map);
      return builder.build();
    }

    @Override
    public Map<String, Object> toJsonObject(Object o) {
      return JsonMapVisitor.toJsonObject((JsonDdlObject) o);
    }
  }

  class ListMapper implements Mapper {
    private final Mapper next;

    public ListMapper(Mapper next) {
      this.next = next;
    }

    @Override
    public List<?> fromJson(Object value, Class<?> leafType) {
      List<?> list = (List<?>) value;
      List<Object> toReturn = new ArrayList<Object>(list.size());

      for (Object o : list) {
        toReturn.add(next.fromJson(o, leafType));
      }

      return Protected.object(toReturn);
    }

    @Override
    public Object toJsonObject(Object value) {
      List<?> source = (List<?>) value;
      List<Object> toReturn = new ArrayList<Object>();
      for (Object o : source) {
        toReturn.add(next.toJsonObject(o));
      }
      return toReturn;
    }
  }

  class MapMapper implements Mapper {
    private final Mapper key;
    private final Mapper value;

    public MapMapper(Mapper key, Mapper value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public Map<?, ?> fromJson(Object input, Class<?> leafType) {
      Map<?, ?> map = (Map<?, ?>) input;
      Map<Object, Object> toReturn = new LinkedHashMap<Object, Object>(map.size());

      for (Map.Entry<?, ?> entry : map.entrySet()) {
        toReturn.put(key.fromJson(entry.getKey(), String.class),
            value.fromJson(entry.getValue(), leafType));
      }

      return Protected.object(toReturn);
    }

    @Override
    public Object toJsonObject(Object incoming) {
      Map<?, ?> map = (Map<?, ?>) incoming;
      Map<String, Object> toReturn = new LinkedHashMap<String, Object>();
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        toReturn.put(key.toJsonObject(entry.getKey()).toString(),
            value.toJsonObject(entry.getValue()));
      }
      return toReturn;
    }
  }

  interface Mapper {
    Object fromJson(Object value, Class<?> leafType);

    Object toJsonObject(Object value);
  }

  static class PrimitiveMapper implements Mapper {
    static final PrimitiveMapper INSTANCE = new PrimitiveMapper();

    @Override
    public Object fromJson(Object value, Class<?> leafType) {
      if (boolean.class.equals(leafType) || Boolean.class.equals(leafType)) {
        return value;
      } else if (double.class.equals(leafType) || Double.class.equals(leafType)) {
        return value;
      } else if (int.class.equals(leafType) || Integer.class.equals(leafType)) {
        return value;
      }
      throw new UnsupportedOperationException("Unexpected input of type "
        + value.getClass().getName() + " for leaf type " + leafType.getName());
    }

    @Override
    public Object toJsonObject(Object value) {
      return value;
    }
  }

  static class StringMapper implements Mapper {
    public static final Mapper INSTANCE = new StringMapper();

    @Override
    public Object fromJson(Object value, Class<?> leafType) {
      if (String.class.equals(leafType)) {
        return value.toString();
      }

      if (leafType.isEnum()) {
        @SuppressWarnings("unchecked")
        Enum<?> e = Enum.valueOf(leafType.asSubclass(Enum.class), value.toString());
        return e;
      }

      throw new UnsupportedOperationException("Unable to deserialize arbitrary object of type "
        + leafType.getName());
    }

    @Override
    public Object toJsonObject(Object value) {
      return value.toString();
    }
  }

  class ToMapVisitor implements PropertyVisitor {
    private Map<String, Object> map = new LinkedHashMap<String, Object>();

    @Override
    public <T> void endVisitProperty(T value, Context<T> ctx) {}

    public Map<String, Object> getMap() {
      return map;
    }

    @Override
    public <T> boolean visitProperty(T value, Context<T> ctx) {
      if (value == null) {
        if (includeNullProperties) {
          map.put(ctx.getProperty(), null);
        }
      } else {
        Mapper mapper = makeStringer(ctx.getKind(), ctx.getNestedKinds());
        map.put(ctx.getProperty(), mapper.toJsonObject(value));
      }
      return false;
    }
  }

  public static JsonDdlVisitor fromJsonMap(Map<String, Object> map) {
    return new JsonMapVisitor().new FromMapVisitor(map);
  }

  public static Map<String, Object> toJsonObject(JsonDdlObject obj) {
    JsonMapVisitor.ToMapVisitor v = new JsonMapVisitor().new ToMapVisitor();
    obj.accept(v);
    return v.getMap();
  }

  private boolean includeNullProperties;

  private JsonMapVisitor() {}

  private Mapper makeStringer(Kind base, List<Kind> nested) {
    List<Kind> list = new ArrayList<Kind>(nested.size() + 1);
    list.add(base);
    list.addAll(nested);
    return makeStringer(list);
  }

  private Mapper makeStringer(List<Kind> list) {
    Kind kind = list.remove(0);
    switch (kind) {
      case BOOLEAN:
      case DOUBLE:
      case INTEGER:
        return PrimitiveMapper.INSTANCE;
      case DDL:
        return new JsonDdlMapper();
      case ENUM:
      case EXTERNAL:
      case STRING:
        return StringMapper.INSTANCE;
      case LIST:
        return new ListMapper(makeStringer(list));
      case MAP:
        return new MapMapper(makeStringer(list), makeStringer(list));
    }
    throw new UnsupportedOperationException(kind.toString());
  }
}
