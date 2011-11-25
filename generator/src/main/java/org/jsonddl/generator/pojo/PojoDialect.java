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
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.Property;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;

/**
 * Creates plain-old Java objects.
 */
public class PojoDialect implements Dialect {
  /**
   * Performs the actual code-generation. Decoration of the generated code with additional
   * annotations can be accomplished by overriding any of the {@code beforeX} methods.
   */
  protected static class Visitor implements JsonDdlVisitor {
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
      beforeModel(m);
      out.println(generatedAnnotation(PojoDialect.class, now));
      out.println("public class %s {", m.getName());
      out.indent();
      return true;
    }

    public boolean visit(Property p) {
      String getterName = getterName(p.getName());
      // Field
      beforeField(p);
      out.print("private ");
      p.getType().accept(this);
      out.println(" %s;", getterName);

      // Getter
      if (p.getComment() != null) {
        out.println(p.getComment());
      }
      beforeGetter(p);
      out.print("public ");
      p.getType().accept(this);
      out.println(" %s%s() { return this.%s; }",
          Kind.BOOLEAN.equals(p.getType().getKind()) ? "is" : "get", getterName, getterName);

      // Setter
      beforeSetter(p);
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

    /**
     * Called before a field declaration is written.
     */
    protected void beforeField(Property p) {}

    /**
     * Called before a getter declaration is written.
     */
    protected void beforeGetter(Property p) {}

    /**
     * Called before a type declaration is written.
     */
    protected void beforeModel(Model m) {}

    /**
     * Called before a setter decleration is written.
     */
    protected void beforeSetter(Property p) {}
  }

  @Override
  public void generate(Options options, Collector output, Schema s) throws IOException {
    s.accept(createVisitor(options, output));
  }

  @Override
  public String getName() {
    return "pojo";
  }

  /**
   * Subclasses can override this method to provide an alternate
   */
  protected Visitor createVisitor(Options options, Collector output) {
    return new Visitor(options, output);
  }
}
