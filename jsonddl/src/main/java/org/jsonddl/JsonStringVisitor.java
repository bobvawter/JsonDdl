package org.jsonddl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.jsonddl.JsonDdlVisitor.PropertyVisitor;
import org.jsonddl.model.Kind;

public class JsonStringVisitor implements PropertyVisitor {

  static class JsonDdlStringer implements Stringer {
    public static final Stringer INSTANCE = new JsonDdlStringer();

    @Override
    public String toJsonString(Object o) {
      JsonDdlObject<?> obj = (JsonDdlObject<?>) o;
      JsonStringVisitor v = new JsonStringVisitor();
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
    public String toJsonString(Object value) {
      return value.toString();
    }
  }

  interface Stringer {
    String toJsonString(Object value);
  }

  static class StringStringer implements Stringer {
    public static final Stringer INSTANCE = new StringStringer();

    @Override
    public String toJsonString(Object value) {
      return '"' + JSONValue.escape(value.toString()) + '"';
    }
  }

  public static String toJsonString(JsonDdlObject<?> obj) {
    JsonStringVisitor v = new JsonStringVisitor();
    obj.accept(v);
    return v.toString();
  }

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
