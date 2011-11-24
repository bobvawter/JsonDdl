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
package org.jsonddl.generator.pojo;

import static org.jsonddl.generator.industrial.IndustrialDialect.generatedAnnotation;
import static org.jsonddl.generator.industrial.IndustrialDialect.getterName;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.generator.Dialect;
import org.jsonddl.generator.IndentedWriter;
import org.jsonddl.generator.Options;
import org.jsonddl.model.Model;
import org.jsonddl.model.Property;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;

public class PojoDialect implements Dialect {

  static class Visitor implements JsonDdlVisitor {
    private final Date now = new Date();
    private final Options options;
    private IndentedWriter out;
    private final Collector output;
    private final String packageName;

    public Visitor(Options options, Collector output) {
      this.options = options;
      this.output = output;
      this.packageName = options.getPackageName();
    }

    public void endVisit(Model m) {
      out.outdent();
      out.println("}");
      out.close();
    }

    public boolean visit(Model m) throws IOException {
      out = new IndentedWriter(new OutputStreamWriter(
          output.writeJavaSource(packageName, m.getName())));
      out.println("package %s;", packageName);
      if (m.getComment() != null) {
        out.println(m.getComment());
      }
      out.println(generatedAnnotation(PojoDialect.class, now));
      out.println("public class %s {", m.getName());
      out.indent();
      return true;
    }

    public boolean visit(Property p) {
      String getterName = getterName(p.getName());
      // Field
      out.print("private ");
      p.getType().accept(this);
      out.println(" %s;", getterName);

      // Getter
      if (p.getComment() != null) {
        out.println(p.getComment());
      }
      out.print("public ");
      p.getType().accept(this);
      out.println(" get%s() { return this.%s; }", getterName, getterName);

      // Setter
      out.print("public void set%s(", getterName);
      p.getType().accept(this);
      out.println(" value) { this.%s = value; }", getterName);
      return false;
    }

    public boolean visit(Type t) {
      switch (t.getKind()) {
        case BOOLEAN:
          out.print(Boolean.class.getCanonicalName());
          break;
        case DOUBLE:
          out.print(Double.class.getCanonicalName());
          break;
        case INTEGER:
          out.print(Integer.class.getCanonicalName());
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
          out.print(List.class.getCanonicalName() + "<");
          t.getListElement().accept(this);
          out.print(">");
          break;
        case MAP:
          out.print(Map.class.getCanonicalName() + "<");
          t.getMapKey().accept(this);
          out.print(", ");
          t.getMapValue().accept(this);
          out.print(">");
          break;
        default:
          throw new UnsupportedOperationException(t.getKind().name());
      }
      return false;
    }
  }

  @Override
  public void generate(Options options, Collector output, Schema s) throws IOException {
    s.accept(new Visitor(options, output));
  }

  @Override
  public String getName() {
    return "pojo";
  }
}
