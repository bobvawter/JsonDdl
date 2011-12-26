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

package org.jsonddl.generator.gwt;

import static org.jsonddl.generator.industrial.IndustrialDialect.generatedAnnotation;
import static org.jsonddl.generator.industrial.IndustrialDialect.getterName;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.jsonddl.generator.Dialect;
import org.jsonddl.generator.IndentedWriter;
import org.jsonddl.generator.Options;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.ModelVisitor;
import org.jsonddl.model.Property;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;

/**
 * Creates simple JSO-based accessors for GWT.
 */
public class GwtJsoDialect implements Dialect {
  /**
   * Generates the JSO overlay type declaration.
   */
  static class Visitor extends ModelVisitor {
    protected final Date now = new Date();
    protected final Options options;
    protected IndentedWriter out;
    protected final Collector output;
    protected final String packageName;

    public Visitor(Options options, Collector output) {
      this.options = options;
      this.output = output;
      this.packageName = options.getPackageName();
    }

    @Override
    public void endVisit(Model m, Context<Model> ctx) {
      out.outdent();
      out.println("}");
      out.close();
    }

    @Override
    public boolean visit(Model m, Context<Model> ctx) throws IOException {
      out = new IndentedWriter(new OutputStreamWriter(
          output.writeJavaSource(packageName, m.getName())));
      out.println("package %s;", packageName);
      if (m.getComment() != null) {
        out.println(m.getComment());
      }
      out.println(generatedAnnotation(GwtJsoDialect.class, now));
      out.println("public class %s extends %s {", m.getName(), JSO_NAME);
      out.indent();
      out.println("protected %s (){}", m.getName());
      return true;
    }

    @Override
    public boolean visit(Property p, Context<Property> ctx) {
      String getterName = getterName(p.getName());

      // Getter
      if (p.getComment() != null) {
        out.println(p.getComment());
      }
      out.print("public final native ");
      p.getType().accept(this);
      out.print(" %s%s() /*-{ return ",
          Kind.BOOLEAN.equals(p.getType().getKind()) ? "is" : "get", getterName);
      writeNativeGetExpression(p);
      out.println("; }-*/;");

      // Setter
      out.print("public final native void set%s(", getterName);
      p.getType().accept(this);
      out.print(" value) /*-{ this['%s'] = ", p.getName());
      writeNativeSetExpression(p);
      out.println("; }-*/;");
      return false;
    }

    @Override
    public boolean visit(Type t, Context<Type> ctx) {
      switch (t.getKind()) {
        case BOOLEAN:
          out.print(boolean.class.getCanonicalName());
          break;
        case DOUBLE:
          out.print(double.class.getCanonicalName());
          break;
        case INTEGER:
          out.print(int.class.getCanonicalName());
          break;
        case STRING:
          out.print(String.class.getCanonicalName());
          break;
        case DDL:
        case EXTERNAL:
        case ENUM:
          out.print(t.getName());
          break;
        case LIST:
          switch (t.getListElement().getKind()) {
            case BOOLEAN:
              out.print(ARRAY_BOOLEAN_NAME);
              break;
            case DOUBLE:
              out.print(ARRAY_NUMBER_NAME);
              break;
            case INTEGER:
              out.print(ARRAY_INTEGER_NAME);
              break;
            case STRING:
              out.print(ARRAY_STRING_NAME);
              break;
            default:
              out.print(ARRAY_JSO_NAME + "<");
              t.getListElement().accept(this);
              out.print(">");
              break;
          }
          break;
        case MAP:
          out.print(MAPISH_NAME + "<");
          t.getMapValue().accept(this);
          out.print(">");
          break;
        default:
          throw new UnsupportedOperationException(t.getKind().name());
      }
      return false;
    }

    private void writeNativeGetExpression(Property p) {
      switch (p.getType().getKind()) {
        case BOOLEAN:
          out.print("!!this['%s']", p.getName());
          break;
        case DOUBLE:
        case INTEGER:
          out.print("this.hasOwnProperty('%s') ? Number(this['%1$s']) : 0", p.getName());
          break;
        case ENUM:
          // MyEnum.valueOf("foo");
          out.print("this['%s'] && @%s::valueOf(Ljava/lang/String;)(this['%1$s'])",
              p.getName(), p.getType().getName());
          break;
        default:
          out.print("this['%s']", p.getName());
      }
    }

    private void writeNativeSetExpression(Property p) {
      switch (p.getType().getKind()) {
        case ENUM:
          out.print("value.@java.lang.Enum::name()");
          break;
        default:
          out.print("value");
      }
    }
  }

  private static final String ARRAY_BOOLEAN_NAME = "com.google.gwt.core.client.JsArrayBoolean";
  private static final String ARRAY_INTEGER_NAME = "com.google.gwt.core.client.JsArrayInteger";
  private static final String ARRAY_JSO_NAME = "com.google.gwt.core.client.JsArray";
  private static final String ARRAY_NUMBER_NAME = "com.google.gwt.core.client.JsArrayNumber";
  private static final String ARRAY_STRING_NAME = "com.google.gwt.core.client.JsArrayString";
  private static final String JSO_NAME = "com.google.gwt.core.client.JavaScriptObject";
  private static final String MAPISH_NAME = "org.jsonddl.gwt.jso.client.MapIsh";

  @Override
  public void generate(Options options, Collector output, Schema s) throws IOException {
    s.accept(new Visitor(options, output));
  }

  @Override
  public String getName() {
    return "gwt-jso";
  }
}
