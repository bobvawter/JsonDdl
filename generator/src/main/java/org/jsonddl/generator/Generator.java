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

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonMapVisitor;
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
    Schema s = new Schema.Builder().withModels(models).accept(new DdlTypeReplacer()).build();
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
      String builderName = simpleName + ".Builder";

      PrintWriter intf = new PrintWriter(new OutputStreamWriter(output.writeImplementation(
          packageName, simpleName)));

      intf.println("package " + packageName + ";");
      if (model.getComment() != null) {
        intf.println(model.getComment());
      }
      intf.println("@" + Generated.class.getCanonicalName() + "(value=\""
        + getClass().getCanonicalName() + "\", date=\"" + sdf.format(now) + "\")");

      if (model.getEnumValues() != null) {
        intf.println("public enum " + simpleName + " {");
        for (EnumValue enumValue : model.getEnumValues()) {
          if (enumValue.getComment() != null) {
            intf.println(enumValue.getComment());
          }
          intf.println(enumValue.getName() + ",");
        }
        intf.println("}");
        intf.close();
        continue;
      }

      intf.print("public interface " + simpleName);
      // XXX implement interfaces
      // if (typeMap.containsKey("implements")) {
      // out.print(" implements " + typeMap.remove("implements") + ", ");
      // } else {
      // out.print(" implements ");
      // }
      intf.print(" extends ");
      intf.print(JsonDdlObject.class.getCanonicalName() + "<" + simpleName + ">");
      intf.println(" {");

      StringWriter builderContents = new StringWriter();
      PrintWriter builder = new PrintWriter(builderContents);
      {
        builder.println("public static class Builder implements "
          + JsonDdlObject.Builder.class.getCanonicalName() + "<" + simpleName + ">, " + simpleName
          + " {");
        builder.println("private " + simpleName + ".Impl obj;");
        builder.println("public Builder() {this(new " + simpleName + ".Impl());}");
        builder.println("public Builder(" + simpleName + ".Impl instance) {this.obj = instance;}");
        builder.println("public " + builderName + " builder() { return this; }");
        builder.println("public Class<" + simpleName + "> getDdlObjectType() { return "
          + simpleName + ".class;}");
        builder.println("public " + builderName + " newInstance() { return new " + builderName
          + "(); }");
        builder.println("public " + Map.class.getCanonicalName()
          + "<String, Object> toJsonObject() { return obj.toJsonObject(); }");
      }
      StringWriter implContents = new StringWriter();
      PrintWriter impl = new PrintWriter(implContents);
      {
        impl.println("public static class Impl implements " + simpleName + " {");
        impl.println("protected Impl() {}");
        impl.println("public Class<" + simpleName + "> getDdlObjectType() { return "
          + simpleName + ".class;}");
      }
      StringWriter buildContents = new StringWriter();
      PrintWriter build = new PrintWriter(buildContents);
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
        impl.println("private " + qsn + " " + propName + ";");
        impl.println("public " + qsn + " get" + getterName + "() {return "
            + propName + ";}");

        if (property.getComment() != null) {
          intf.println(property.getComment());
        }
        intf.println(qsn + " get" + getterName + "();");

        if (TypeAnswers.shouldProtect(type)) {
          build.println("toReturn." + propName + " = " + Protected.class.getCanonicalName()
            + ".object(toReturn." + propName + ");");
        }

        builder.println("public " + qsn + " get" + getterName + "() { return obj." + propName
          + "; }");
        builder.print(
            "public " + builderName + " with" + getterName + "(" + qsn + " value) { ");
        builder.print("obj." + propName + " = value;");
        builder.println("return this;}");

        from.println("with" + getterName + "(from.get" + getterName + "());");

        writeTraversalForProperty(traverse, propName, getterName, type, false);
        writeTraversalForProperty(traverseMutable, propName, getterName, type, true);
      }
      builder.println("public " + builderName + " accept("
        + JsonDdlVisitor.class.getCanonicalName()
        + " visitor) {");
      builder.println("obj = new " + ContextImpl.ObjectContext.Builder.class.getCanonicalName()
        + "<" + simpleName + ">().withValue(this).withKind("
        + Kind.class.getCanonicalName()
        + "."
        + Kind.DDL.name() + ").withMutability(true).build().traverse(visitor).builder().obj;");
      builder.println("return this;");
      builder.println("}");

      builder.println("public " + simpleName + " build() {");
      builder.println(simpleName + ".Impl toReturn = obj;");
      builder.println("obj = null;");
      builder.append(buildContents.getBuffer().toString());
      builder.println("return toReturn;");
      builder.println("}");

      builder.println("public " + builderName + " from(" + simpleName + " from) {");
      builder.append(fromContents.getBuffer());
      builder.append("return this;");
      builder.println("}");

      builder.println("public " + builderName + " from(" + Map.class.getCanonicalName()
        + "<String, Object> map){");
      builder.println("accept(" + JsonMapVisitor.class.getCanonicalName()
        + ".fromJsonMap(map));");
      builder.println("return this;");
      builder.println("}");

      builder.println("public " + builderName + " traverse("
          + JsonDdlVisitor.class.getCanonicalName()
          + " visitor) {");
      builder.append(traverseMutableContents.getBuffer().toString());
      builder.println("return this;");
      builder.println("}");
      builder.println("}");
      // END BUILDER
      intf.append(builderContents.getBuffer().toString());

      impl.println("public " + simpleName + " accept(" + JsonDdlVisitor.class.getCanonicalName()
        + " visitor) {");
      impl.println("return new " + ContextImpl.ObjectContext.Builder.class.getCanonicalName() + "<"
        + simpleName + ">().withValue(this).withKind(" + Kind.class.getCanonicalName() + "."
        + Kind.DDL.name()
        + ").build().traverse(visitor);");
      impl.println("}");

      impl.println("public " + builderName + " builder() { return newInstance().from(this); }");
      impl.println("public " + builderName + " newInstance() { return new Builder(); }");

      impl.println("public " + Map.class.getCanonicalName()
        + "<String,Object> toJsonObject() { return " + JsonMapVisitor.class.getCanonicalName()
        + ".toJsonObject(this); }");

      impl.println("public " + simpleName + " traverse(" + JsonDdlVisitor.class.getCanonicalName()
        + " visitor) {");
      impl.append(traverseContents.getBuffer().toString());
      impl.println("return this;");
      impl.println("}");
      impl.println("}");
      // END IMPL
      intf.append(implContents.getBuffer().toString());

      intf.println("Builder builder();");
      intf.println("Builder newInstance();");
      intf.println("}");

      intf.close();
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
      pw.println("with" + getterName + "(");
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
    String propertyRef = (mutable ? "obj" : "this") + "." + propertyName;
    pw.println(".withValue(" + propertyRef + ")");
    pw.print(".build().traverse(visitor)");
    if (mutable) {
      pw.print(")");
    }
    pw.println(";");
  }
}
