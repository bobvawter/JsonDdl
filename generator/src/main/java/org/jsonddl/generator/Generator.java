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

import static org.jsonddl.generator.TypeAnswers.canTraverse;
import static org.jsonddl.generator.TypeAnswers.getContextParameterization;
import static org.jsonddl.generator.TypeAnswers.getQualifiedSourceName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

import org.jsonddl.Context;
import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.VisitSupport;
import org.jsonddl.generator.model.Kind;
import org.jsonddl.generator.model.Model;
import org.jsonddl.generator.model.Property;
import org.jsonddl.generator.model.Schema;
import org.jsonddl.generator.model.Type;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.VariableDeclaration;

public class Generator {
  public interface Collector {
    void println(String message);

    void println(String format, Object... args);

    OutputStream writeImplementation(String packageName, String simpleName) throws IOException;
  }

  public static void main(String[] args) throws IOException {
    final File outputRoot = new File(args[2]);
    new Generator().generate(new FileInputStream(new File(args[0])), args[1], new Collector() {
      @Override
      public void println(String message) {
        System.out.println(message);
      }

      @Override
      public void println(String format, Object... args) {
        System.out.println(String.format(format, args));
      }

      @Override
      public OutputStream writeImplementation(String packageName, String simpleName)
            throws IOException {
        File f = new File(outputRoot, packageName.replace('.', File.separatorChar));
        f.mkdirs();
        f = new File(f, simpleName + ".java");
        return new FileOutputStream(f);
      }
    });
  }

  public boolean generate(InputStream schema, String packageName, Collector output)
      throws IOException {
    CompilerEnvirons env = new CompilerEnvirons();
    env.setRecordingComments(true);
    env.setRecordingLocalJsDocComments(true);
    Parser parser = new Parser(env);
    AstRoot root;
    try {
      root = parser.parse(new InputStreamReader(schema), packageName, 0);
    } catch (RhinoException e) {
      output.println("Could not parse input file: %s", e.getMessage());
      return false;
    }
    Node first = root.getFirstChild();
    if (first instanceof VariableDeclaration) {
      first = ((VariableDeclaration) first).getVariables().get(0).getInitializer();
    }
    ObjectLiteral obj;
    if (first instanceof ObjectLiteral) {
      obj = (ObjectLiteral) first;
    } else {
      output.println("Expecting an object literal or variable initializer as the first node");
      return false;
    }

    Map<String, Model> models = new TreeMap<String, Model>();
    for (ObjectProperty prop : obj.getElements()) {
      List<Property> properties = new ArrayList<Property>();
      ObjectLiteral propertyDeclarations = castOrNull(ObjectLiteral.class, prop.getRight());
      for (ObjectProperty propertyDeclaration : propertyDeclarations.getElements()) {
        properties.add(extractProperty(propertyDeclaration));
      }
      models.put(extractName(prop), new Model.Builder()
          .withComment(prop.getLeft().getJsDoc())
          .withName(extractName(prop))
          .withProperties(properties)
          .build());
    }
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Schema s = new Schema.Builder().withModels(models).build()
        .acceptMutable(new JsonDdlVisitor() {
          private Set<String> modelTypes;

          public void endVisit(Type t, Context<Type> ctx) {
            if (Kind.EXTERNAL.equals(t.getKind()) && modelTypes.contains(t.getName())) {
              ctx.replace(t.builder().withKind(Kind.DDL).build());
            }
          }

          public boolean visit(Schema s, Context<Schema> ctx) {
            modelTypes = s.getModels().keySet();
            return true;
          }
        });
    for (Model model : s.getModels().values()) {
      String simpleName = model.getName();

      PrintWriter out = new PrintWriter(new OutputStreamWriter(output.writeImplementation(
          packageName, simpleName)));

      out.println("package " + packageName + ";");
      if (model.getComment() != null) {
        out.println(model.getComment());
      }
      out.println("@" + Generated.class.getCanonicalName() + "(value=\""
        + getClass().getCanonicalName() + "\", date=\"" + sdf.format(now) + "\")");
      out.print("public class " + simpleName);
      // XXX implement interfaces
      // if (typeMap.containsKey("implements")) {
      // out.print(" implements " + typeMap.remove("implements") + ", ");
      // } else {
      // out.print(" implements ");
      // }
      out.print(" implements ");
      out.print(JsonDdlObject.class.getCanonicalName() + "<" + simpleName + ">");
      out.println(" {");

      StringWriter builderContents = new StringWriter();
      PrintWriter builder = new PrintWriter(builderContents);
      {
        builder.println("public static class Builder implements "
          + JsonDdlObject.Builder.class.getCanonicalName() + "<" + simpleName + "> {");
        builder.println("private " + simpleName + " obj = new " + simpleName + "();");
        builder.println("public " + simpleName + " build() {");
        builder.println(simpleName + " toReturn = obj;");
        builder.println("obj = null;");
        builder.println("return toReturn;");
        builder.println("}");
      }
      StringWriter fromContents = new StringWriter();
      PrintWriter from = new PrintWriter(fromContents);
      StringWriter traverseContents = new StringWriter();
      PrintWriter traverse = new PrintWriter(traverseContents);
      StringWriter traverseMutableContents = new StringWriter();
      PrintWriter traverseMutable = new PrintWriter(traverseMutableContents);
      for (Property property : model.getProperties()) {
        String propName = property.getName();
        Type type = property.getType();
        String getterName = Character.toUpperCase(propName.charAt(0))
          + (propName.length() > 1 ? propName.substring(1) : "");

        String qsn = getQualifiedSourceName(type);
        out.println("private " + qsn + " " + propName + ";");
        if (property.getComment() != null) {
          out.println(property.getComment());
        }
        out.println("public " + qsn + " get" + getterName + "() {return "
          + propName + ";}");
        builder.println("public Builder with" + getterName + "(" + qsn
          + " value) { obj."
          + propName + " = value; return this;}");

        from.println("with" + getterName + "(from.get" + getterName + "());");

        if (canTraverse(type)) {
          String contextArgs = "<" + getContextParameterization(type) + ">(\"" + propName
            + "\", this."
            + propName + ").traverse(visitor)";
          String traverseCall = "new "
            + type.getKind().getImmutableContextType().getCanonicalName() + contextArgs;
          traverse.println(traverseCall + ";");
          traverseCall = "new " + type.getKind().getMutableContextType().getCanonicalName()
            + contextArgs;
          traverseMutable.println("builder.with" + getterName + "(" + traverseCall + ");");
        }
      }
      builder.println("public Builder from(" + simpleName + " from) {");
      builder.append(fromContents.getBuffer());
      builder.append("return this;");
      builder.println("}");
      builder.println("}");
      out.append(builderContents.getBuffer().toString());

      out.println("private " + simpleName + "(){}");

      out.println("public void accept(" + JsonDdlVisitor.class.getCanonicalName() + " visitor) {");
      out.println("new " + Context.ImmutableContext.class.getCanonicalName() + "<" + simpleName
        + ">(null,this).traverse(visitor);");
      out.println("}");

      out.println("public " + simpleName + " acceptMutable("
        + JsonDdlVisitor.class.getCanonicalName()
        + " visitor) {");
      out.println("return new " + Context.SettableContext.class.getCanonicalName() + "<"
        + simpleName
        + ">(null,this).traverse(visitor);");
      out.println("}");

      out.println("public Builder builder() { return newInstance().from(this); }");
      out.println("public Builder newInstance() { return new Builder(); }");

      out.println("public void traverse(" + JsonDdlVisitor.class.getCanonicalName()
        + " visitor, "
        + Context.class.getCanonicalName() + "<" + simpleName + "> ctx) {");
      out.println("if (" + VisitSupport.class.getCanonicalName() + ".visit(visitor, this,ctx)) {");
      out.println(traverseContents.getBuffer());
      out.println("}");
      out.println(VisitSupport.class.getCanonicalName() + ".endVisit(visitor, this, ctx);");
      out.println("}");

      out.println("public " + simpleName + " traverseMutable("
        + JsonDdlVisitor.class.getCanonicalName()
        + " visitor, " + Context.class.getCanonicalName() + "<" + simpleName + "> ctx) {");
      out.println("Builder builder = builder();");
      out.println("if (" + VisitSupport.class.getCanonicalName() + ".visit(visitor, this,ctx)) {");
      out.println(traverseMutableContents.getBuffer());
      out.println("}");
      out.println(VisitSupport.class.getCanonicalName() + ".endVisit(visitor, this, ctx);");
      out.println("return builder.build();");
      out.println("}");

      out.println("}");

      out.close();
    }
    return true;
  }

  private <T extends AstNode> T castOrNull(Class<T> clazz, AstNode node) {
    if (clazz.isInstance(node)) {
      return clazz.cast(node);
    }
    return null;
  }

  /**
   * Extracts the name of the given object property.
   */
  private String extractName(ObjectProperty prop) {
    String typeName;
    StringLiteral typeNameAsLit = castOrNull(StringLiteral.class, prop.getLeft());
    Name typeNameAsName = castOrNull(Name.class, prop.getLeft());
    if (typeNameAsLit != null) {
      typeName = typeNameAsLit.getValue();
    } else if (typeNameAsName != null) {
      typeName = typeNameAsName.getIdentifier();
    } else {
      throw new RuntimeException("Unexpected node type "
        + prop.getLeft().getClass().getSimpleName());
    }
    return typeName;
  }

  private Property extractProperty(ObjectProperty prop) {
    return new Property.Builder()
        .withComment(prop.getLeft().getJsDoc())
        .withName(extractName(prop))
        .withType(typeName(prop.getRight(), false))
        .build();
  }

  /**
   * Convert an AST node to a Java type reference.
   */
  private Type typeName(AstNode node, boolean forceBoxed) {
    StringLiteral string = castOrNull(StringLiteral.class, node);
    if (string != null) {
      String value = string.getValue();
      if (value.isEmpty()) {
        return new Type.Builder()
            .withKind(Kind.EXTERNAL)
            .withName("String")
            .build();
      } else if (false /* ddlNames.contains(value) */) {
        return new Type.Builder()
            .withKind(Kind.DDL)
            .withName(value)
            .build();
      }
      return new Type.Builder().withKind(Kind.EXTERNAL).withName(value).build();
    }
    Name name = castOrNull(Name.class, node);
    if (name != null) {
      String id = name.getIdentifier();
      return new Type.Builder()
          .withKind(false /* ddlNames.contains(id) */? Kind.DDL : Kind.EXTERNAL)
          .withName(id)
          .build();
    }

    ArrayLiteral array = castOrNull(ArrayLiteral.class, node);
    if (array != null) {
      if (array.getSize() != 1) {
        throw new UnexpectedNodeException(array, "Expecting exactly one entry");
      }
      return new Type.Builder()
          .withKind(Kind.LIST)
          .withListElement(typeName(array.getElement(0), true))
          .build();
    }

    KeywordLiteral keyword = castOrNull(KeywordLiteral.class, node);
    if (keyword != null && keyword.isBooleanLiteral()) {
      return new Type.Builder()
          .withKind(Kind.PRIMITIVE)
          .withName(forceBoxed ? "Boolean" : "boolean")
          .build();
    }

    NumberLiteral num = castOrNull(NumberLiteral.class, node);
    if (num != null) {
      double d = num.getNumber();
      if (Math.round(d) == d) {
        return new Type.Builder()
            .withKind(Kind.PRIMITIVE)
            .withName(forceBoxed ? "Integer" : "int")
            .build();
      } else {
        return new Type.Builder()
            .withKind(Kind.PRIMITIVE)
            .withName(forceBoxed ? "Double" : "double")
            .build();
      }
    }
    ObjectLiteral obj = castOrNull(ObjectLiteral.class, node);
    if (obj != null) {
      if (obj.getElements().size() != 1) {
        throw new UnexpectedNodeException(obj, "Expecting exactly one property");
      }
      ObjectProperty prop = obj.getElements().get(0);
      return new Type.Builder()
          .withKind(Kind.MAP)
          .withMapKey(typeName(prop.getLeft(), true))
          .withMapValue(typeName(prop.getRight(), true))
          .build();
    }

    throw new UnexpectedNodeException(node);
  }
}
