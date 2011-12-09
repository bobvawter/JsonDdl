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
package org.jsonddl.generator.idiomatic;

import org.jsonddl.generator.Dialect;
import org.jsonddl.generator.IndentedWriter;
import org.jsonddl.generator.Options;
import org.jsonddl.model.EnumValue;
import org.jsonddl.model.Model;
import org.jsonddl.model.ModelVisitor;
import org.jsonddl.model.Property;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * Produces an idiomatic representation of a schema. It won't be character-accurate to the original
 * idiomatic input, but should re-parse to the same normalized version.
 */
public class IdiomaticDialect implements Dialect {

  static class Visitor extends ModelVisitor {
    private final StringWriter contents = new StringWriter();
    private final IndentedWriter out = new IndentedWriter(contents);
    private boolean needsCommaAfterEnum;
    private boolean needsCommaAfterModel;
    private boolean needsCommaAfterProperty;

    @Override
    public void endVisit(EnumValue v, Context<EnumValue> ctx) {
      if (needsCommaAfterEnum) {
        out.println(",");
      } else {
        needsCommaAfterEnum = true;
        out.println();
      }
      if (v.getComment() != null) {
        out.println(v.getComment());
      }
      out.format("\"%s\"", v.getName());
    }

    @Override
    public void endVisit(Model m, Context<Model> ctx) {
      out.println();
      out.outdent();
      if (m.getEnumValues() != null) {
        out.print("]");
      } else {
        out.print("}");
      }
    }

    @Override
    public void endVisit(Schema s, Context<Schema> ctx) {
      out.println();
      out.outdent();
      out.println("};");
    }

    @Override
    public String toString() {
      return contents.toString();
    }

    @Override
    public boolean visit(Model m, Context<Model> ctx) {
      if (needsCommaAfterModel) {
        out.println(",");
      } else {
        out.println();
        needsCommaAfterModel = true;
      }
      needsCommaAfterProperty = false;
      needsCommaAfterEnum = false;
      if (m.getComment() != null) {
        out.println(m.getComment());
      }
      if (m.getEnumValues() != null) {
        out.format("%s : [", m.getName());
        out.indent();
      } else {
        out.format("%s : {", m.getName());
        out.indent();
      }
      return true;
    }

    @Override
    public boolean visit(Property p, Context<Property> ctx) {
      if (needsCommaAfterProperty) {
        out.println(",");
      } else {
        needsCommaAfterProperty = true;
        out.println();
      }
      if (p.getComment() != null) {
        out.println(p.getComment());
      }
      out.format("%s : ", p.getName());
      return true;
    }

    @Override
    public boolean visit(Schema s, Context<Schema> ctx) {
      if (s.getComment() != null) {
        out.println(s.getComment());
      }
      out.print("var schema = {");
      out.indent();
      return true;
    }

    @Override
    public boolean visit(Type t, Context<Type> ctx) {
      switch (t.getKind()) {
        case BOOLEAN:
          out.print("false");
          break;
        case DDL:
        case ENUM:
        case EXTERNAL:
          out.format("\"%s\"", t.getName());
          break;
        case DOUBLE:
          out.print("0.0");
          break;
        case INTEGER:
          out.print("0");
          break;
        case LIST:
          out.print("[");
          t.getListElement().accept(this);
          out.print("]");
          break;
        case MAP:
          out.print("{");
          t.getMapKey().accept(this);
          out.print(" : ");
          t.getMapValue().accept(this);
          out.print("}");
          break;
        case STRING:
          out.print("\"\"");
          break;
        default:
          throw new UnsupportedOperationException("Unknown kind " + t.getKind());
      }
      return false;
    }
  }

  @Override
  public void generate(Options options, Collector output, Schema s) throws IOException {
    Visitor v = new Visitor();
    s.accept(v);

    OutputStream out = output.writeResource(options.getPackageName().replace('.', '/')
      + "/idiomatic.js");
    out.write(v.toString().getBytes("UTF8"));
    out.close();
  }

  @Override
  public String getName() {
    return "idiomatic";
  }

}
