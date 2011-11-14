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

import static org.jsonddl.generator.TypeAnswers.getParameterizedQualifiedSourceName;

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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Generated;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonStringVisitor;
import org.jsonddl.impl.ContextImpl;
import org.jsonddl.impl.Protected;
import org.jsonddl.model.EnumValue;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.Property;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;
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

  class DdlTypeReplacer implements JsonDdlVisitor {
    private Map<String, Model> models;

    public void endVisit(Type t, Context<Type> ctx) {
      if (Kind.EXTERNAL.equals(t.getKind()) && models.containsKey(t.getName())) {
        Kind kind = models.get(t.getName()).getEnumValues() == null ? Kind.DDL : Kind.ENUM;
        ctx.replace(t.builder().withKind(kind).build());
      }
    }

    public boolean visit(Schema s, Context<Schema> ctx) {
      models = s.getModels();
      return true;
    }
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
      InputStreamReader sourceReader = new InputStreamReader(schema);
      root = parser.parse(sourceReader, packageName, 0);
      sourceReader.close();
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
      Model.Builder builder = new Model.Builder()
          .withComment(prop.getLeft().getJsDoc())
          .withName(extractName(prop));

      ArrayLiteral enumDeclarations = castOrNull(ArrayLiteral.class, prop.getRight());
      ObjectLiteral propertyDeclarations = castOrNull(ObjectLiteral.class, prop.getRight());

      if (enumDeclarations != null) {
        List<EnumValue> enumValues = new ArrayList<EnumValue>();
        for (AstNode node : enumDeclarations.getElements()) {
          StringLiteral string = castOrNull(StringLiteral.class, node);
          if (string == null) {
            throw new UnexpectedNodeException(node, "Expecting a string");
          }
          enumValues.add(new EnumValue.Builder()
              .withComment(node.getJsDoc())
              .withName(string.getValue())
              .build());
        }
        builder.withEnumValues(enumValues);
      } else if (propertyDeclarations != null) {
        List<Property> properties = new ArrayList<Property>();
        for (ObjectProperty propertyDeclaration : propertyDeclarations.getElements()) {
          properties.add(extractProperty(propertyDeclaration));
        }
        builder.withProperties(properties);
      } else {
        throw new UnexpectedNodeException(prop.getRight(),
            "Expecting property declaration object or enum declaration array");
      }
      models.put(extractName(prop), builder.build());
    }
    Schema s = new Schema.Builder().withModels(models).build().acceptMutable(new DdlTypeReplacer());
    String json = s.toJson();
    System.out.println(json);
    try {
      Object o = new JSONParser().parse(json);
      Schema s2 = new Schema.Builder().from((Map<String, Object>) o).build();
      System.out.println(s2.toJson());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    generateIndustrialObjects(packageName, output, s);
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

  private void generateIndustrialObjects(String packageName, Collector output, Schema s)
      throws IOException {
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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

      if (model.getEnumValues() != null) {
        out.println("public enum " + simpleName + " {");
        for (EnumValue enumValue : model.getEnumValues()) {
          if (enumValue.getComment() != null) {
            out.println(enumValue.getComment());
          }
          out.println(enumValue.getName() + ",");
        }
        out.println("}");
        out.close();
        continue;
      }

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
        builder.println("private " + simpleName + " obj;");
        builder.println("public Builder() {this(new " + simpleName + "());}");
        builder.println("public Builder(" + simpleName + " instance) {this.obj = instance;}");
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

        String qsn = getParameterizedQualifiedSourceName(type);
        out.println("private " + qsn + " " + propName + ";");
        if (property.getComment() != null) {
          out.println(property.getComment());
        }
        out.println("public " + qsn + " get" + getterName + "() {return "
          + propName + ";}");
        builder.print(
            "public Builder with" + getterName + "(" + qsn + " value) { ");
        if (TypeAnswers.shouldProtect(type)) {
          builder.print("obj." + propName + " = " + Protected.class.getCanonicalName()
            + ".object(value);");
        } else {
          builder.print("obj." + propName + " = value;");
        }
        builder.println("return this;}");

        from.println("with" + getterName + "(from.get" + getterName + "());");

        writeTraversalForProperty(traverse, propName, getterName, type, false);
        writeTraversalForProperty(traverseMutable, propName, getterName, type, true);
      }
      builder.println("public Builder from(" + simpleName + " from) {");
      builder.append(fromContents.getBuffer());
      builder.append("return this;");
      builder.println("}");

      builder.println("public Builder from(" + Map.class.getCanonicalName()
        + "<String, Object> map){");
      builder.println("obj = obj.acceptMutable(" + JsonStringVisitor.class.getCanonicalName()
        + ".fromJsonMap(map));");
      builder.println("return this;");
      builder.println("}");

      builder.println("}");
      out.append(builderContents.getBuffer().toString());

      out.println("private " + simpleName + "(){}");

      out.println("public void accept(" + JsonDdlVisitor.class.getCanonicalName() + " visitor) {");
      out.println("new " + ContextImpl.ObjectContext.Builder.class.getCanonicalName() + "<"
        + simpleName
        + ">().withValue(this).withKind(" + Kind.class.getCanonicalName() + "." + Kind.DDL.name()
        + ").build().traverse(visitor);");
      out.println("}");

      out.println("public " + simpleName + " acceptMutable("
        + JsonDdlVisitor.class.getCanonicalName()
        + " visitor) {");
      out.println("return new " + ContextImpl.ObjectContext.Builder.class.getCanonicalName() + "<"
        + simpleName
          + ">().withValue(this).withKind(" + Kind.class.getCanonicalName() + "." + Kind.DDL.name()
        + ").withMutability(true).build().traverse(visitor);");
      out.println("}");

      out.println("public Builder builder() { return newInstance().from(this); }");
      out.println("public Builder newInstance() { return new Builder(); }");

      out.println("public String toJson() { return " + JsonStringVisitor.class.getCanonicalName()
        + ".toJsonString(this); }");

      out.println("public void traverse(" + JsonDdlVisitor.class.getCanonicalName()
        + " visitor) {");
      out.println(traverseContents.getBuffer());
      out.println("}");

      out.println("public " + simpleName + " traverseMutable("
        + JsonDdlVisitor.class.getCanonicalName()
        + " visitor) {");
      out.println("Builder builder = newInstance();");
      out.println(traverseMutableContents.getBuffer());
      out.println("return builder.build();");
      out.println("}");

      out.println("}");

      out.close();
    }
  }

  private String kindReference(Kind type) {
    if (type == null) {
      return "null";
    }
    return Kind.class.getCanonicalName() + "." + type.name();
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
            .withKind(Kind.STRING)
            .build();
      }
      return new Type.Builder().withKind(Kind.EXTERNAL).withName(value).build();
    }
    Name name = castOrNull(Name.class, node);
    if (name != null) {
      String id = name.getIdentifier();
      return new Type.Builder()
          .withKind(Kind.EXTERNAL)
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
          .withKind(Kind.BOOLEAN)
          .build();
    }

    NumberLiteral num = castOrNull(NumberLiteral.class, node);
    if (num != null) {
      double d = num.getNumber();
      if (Math.round(d) == d) {
        return new Type.Builder()
            .withKind(Kind.INTEGER)
            .build();
      } else {
        return new Type.Builder()
            .withKind(Kind.DOUBLE)
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

  private void writeTraversalForProperty(PrintWriter pw, String propertyName, String getterName,
      Type type, boolean mutable) {
    if (mutable) {
      pw.println("builder.with" + getterName + "(");
    }

    final List<Kind> kindReferencs = new ArrayList<Kind>();
    type.accept(new JsonDdlVisitor() {
      @SuppressWarnings("unused")
      public boolean visit(Type t, Context<Type> ctx) {
        kindReferencs.add(t.getKind());
        return true;
      }
    });

    pw.println("new " + TypeAnswers.getContextBuilderDeclaration(type) + "()");
    pw.println(".withKind(" + kindReference(kindReferencs.remove(0)) + ")");
    if (!kindReferencs.isEmpty()) {
      pw.print(".withNestedKinds(" + Arrays.class.getCanonicalName() + ".asList(");
      boolean needsComma = false;
      for (Kind kind : kindReferencs) {
        if (needsComma) {
          pw.print(", ");
        }
        needsComma = true;
        pw.print(kindReference(kind));
      }
      pw.println("))");
    }
    pw.println(".withLeafType(" + TypeAnswers.getQualifiedLeafTypeName(type) + ".class)");
    pw.println(".withMutability(" + mutable + ")");
    pw.println(".withProperty(\"" + propertyName + "\")");
    pw.println(".withValue(this." + propertyName + ")");
    pw.print(".build().traverse(visitor)");
    if (mutable) {
      pw.print(")");
    }
    pw.println(";");
  }
}
