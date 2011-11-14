package org.jsonddl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.json.simple.JSONValue;
import org.jsonddl.JsonDdlVisitor.PropertyVisitor;
import org.jsonddl.impl.Protected;
import org.jsonddl.model.Kind;

public class JsonStringVisitor {

  class Decoder implements PropertyVisitor {
    private final Stack<Map<String, Object>> maps = new Stack<Map<String, Object>>();

    public Decoder(Map<String, Object> map) {
      maps.push(map);
    }

    @Override
    public <T> void endVisitProperty(T value, Context<T> ctx) {
      if (Kind.DDL.equals(ctx.getKind())) {
        maps.pop();
      }
    }

    @Override
    public <T> boolean visitProperty(T value, Context<T> ctx) {
      Stringer s = makeStringer(ctx.getKind(), ctx.getNestedKinds());
      Map<String, Object> topMap = maps.peek();
      Object property = topMap.get(ctx.getProperty());
      if (property != null) {
        @SuppressWarnings("unchecked")
        T newValue = (T) s.fromJson(property, ctx.getLeafType());
        ctx.replace(newValue);
      }
      if (Kind.DDL.equals(ctx.getKind())) {
        @SuppressWarnings("unchecked")
        Map<String, Object> subMap = (Map<String, Object>) property;
        maps.push(subMap);
      }
      return false;
    }
  }

  class Encoder implements PropertyVisitor {
    private boolean needsComma;
    private boolean printNulls;
    private StringBuilder sb = new StringBuilder();

    public void endVisit(JsonDdlObject<?> obj, Context<?> ctx) {
      sb.append("}");
    }

    @Override
    public <T> void endVisitProperty(T value, Context<T> ctx) {}

    @Override
    public String toString() {
      return sb.toString();
    }

    public boolean visit(JsonDdlObject<?> obj, Context<?> ctx) {
      sb.append("{");
      return true;
    }

    @Override
    public <T> boolean visitProperty(T value, Context<T> ctx) {
      if (value == null && !printNulls) {
        return false;
      }
      if (needsComma) {
        sb.append(",");
      }
      needsComma = true;
      sb.append('"').append(JSONValue.escape(ctx.getProperty())).append("\":");
      if (value == null) {
        sb.append("null");
      } else {
        Stringer stringer = makeStringer(ctx.getKind(), ctx.getNestedKinds());
        sb.append(stringer.toJsonString(value));
      }
      return false;
    }
  }

  static class JsonDdlStringer implements Stringer {
    public static final Stringer INSTANCE = new JsonDdlStringer();

    @Override
    public JsonDdlObject<?> fromJson(Object value, Class<?> leafType) {
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
    public String toJsonString(Object o) {
      JsonDdlObject<?> obj = (JsonDdlObject<?>) o;
      Encoder v = new JsonStringVisitor().new Encoder();
      obj.accept(v);
      return v.toString();
    }
  }

  static class ListStringer implements Stringer {
    private final Stringer next;

    public ListStringer(Stringer next) {
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
    public String toJsonString(Object value) {
      List<?> list = (List<?>) value;
      StringBuilder sb = new StringBuilder("[");
      boolean needsComma = false;
      for (Object o : list) {
        if (needsComma) {
          sb.append(",");
        }
        needsComma = true;
        sb.append(next.toJsonString(o));
      }
      sb.append("]");
      return sb.toString();
    }
  }

  static class MapStringer implements Stringer {
    private final Stringer key;
    private final Stringer value;

    public MapStringer(Stringer key, Stringer value) {
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
    public String toJsonString(Object value) {
      Map<?, ?> map = (Map<?, ?>) value;
      StringBuilder sb = new StringBuilder("{");
      boolean needsComma = false;
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        if (needsComma) {
          sb.append(",");
        }
        needsComma = true;
        sb.append(key.toJsonString(entry.getKey()))
            .append(":")
            .append(this.value.toJsonString(entry.getValue()));
      }
      sb.append("}");
      return sb.toString();
    }
  }

  static class PrimitiveStringer implements Stringer {
    public static final Stringer INSTANCE = new PrimitiveStringer();

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
    public String toJsonString(Object value) {
      return value.toString();
    }
  }

  interface Stringer {
    Object fromJson(Object value, Class<?> leafType);

    String toJsonString(Object value);
  }

  static class StringStringer implements Stringer {
    public static final Stringer INSTANCE = new StringStringer();

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

      // TODO: Allow arbitrary JSON deserializer to be plugged in
      throw new UnsupportedOperationException("Unable to deserialize arbitrary object of type "
        + leafType.getName());
    }

    @Override
    public String toJsonString(Object value) {
      return '"' + JSONValue.escape(value.toString()) + '"';
    }
  }

  public static JsonDdlVisitor fromJsonMap(Map<String, Object> map) {
    return new JsonStringVisitor().new Decoder(map);
  }

  public static String toJsonString(JsonDdlObject<?> obj) {
    JsonStringVisitor.Encoder v = new JsonStringVisitor().new Encoder();
    obj.accept(v);
    return v.toString();
  }

  private Stringer makeStringer(Kind base, List<Kind> nested) {
    List<Kind> list = new ArrayList<Kind>(nested.size() + 1);
    list.add(base);
    list.addAll(nested);
    return makeStringer(list);
  }

  private Stringer makeStringer(List<Kind> list) {
    Kind kind = list.remove(0);
    switch (kind) {
      case BOOLEAN:
      case DOUBLE:
      case INTEGER:
        return PrimitiveStringer.INSTANCE;
      case DDL:
        return JsonDdlStringer.INSTANCE;
      case ENUM:
      case EXTERNAL:
      case STRING:
        return StringStringer.INSTANCE;
      case LIST:
        return new ListStringer(makeStringer(list));
      case MAP:
        return new MapStringer(makeStringer(list), makeStringer(list));
    }
    throw new UnsupportedOperationException(kind.toString());
  }
}
