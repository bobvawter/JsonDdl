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
package org.jsonddl.generator.industrial;

import static org.jsonddl.generator.industrial.TypeAnswers.getParameterizedQualifiedSourceName;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.generator.Dialect;
import org.jsonddl.generator.Options;
import org.jsonddl.impl.ContextImpl;
import org.jsonddl.impl.JsonMapVisitor;
import org.jsonddl.impl.Protected;
import org.jsonddl.impl.Traversable;
import org.jsonddl.model.EnumValue;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.Property;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;

public class IndustrialDialect implements Dialect {

  public static String generatedAnnotation(Class<? extends Dialect> clazz, Date now) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String generated = "@" + Generated.class.getCanonicalName() + "(value=\""
      + clazz.getCanonicalName() + "\", date=\"" + sdf.format(now) + "\")";
    return generated;
  }

  public static String getterName(String propName) {
    String getterName = Character.toUpperCase(propName.charAt(0))
      + (propName.length() > 1 ? propName.substring(1) : "");
    return getterName;
  }

  @Override
  public void generate(Options options, Dialect.Collector output, Schema s) throws IOException {
    Date now = new Date();
    String packageName = options.getPackageName();
    for (Model model : s.getModels().values()) {
      String simpleName = model.getName();
      String builderName = simpleName + ".Builder";
      String implName = simpleName + "Impl";

      PrintWriter intf = new PrintWriter(new OutputStreamWriter(output.writeJavaSource(
          packageName, simpleName)));

      intf.println("package " + packageName + ";");
      if (model.getComment() != null) {
        intf.println(model.getComment());
      }
      intf.println(generatedAnnotation(getClass(), now));

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
          + JsonDdlObject.Builder.class.getCanonicalName() + "<" + simpleName + ">, "
          + Traversable.class.getCanonicalName() + "<" + simpleName + ">, "
          + simpleName + " {");
        builder.println("private " + implName + " obj;");
        builder.println("public Builder() {this(new " + implName + "());}");
        builder.println("Builder(" + implName + " instance) {this.obj = instance;}");
        builder.println("public " + builderName + " builder() { return this; }");
        builder.println("public Class<" + simpleName + "> getDdlObjectType() { return "
          + simpleName + ".class;}");
        builder.println("public " + builderName + " newInstance() { return new " + builderName
          + "(); }");
        builder.println("public " + Map.class.getCanonicalName()
          + "<String, Object> toJsonObject() { return obj.toJsonObject(); }");
      }

      PrintWriter impl = new PrintWriter(new OutputStreamWriter(output.writeJavaSource(
          packageName, implName)));
      {
        impl.println("package " + packageName + ";");
        impl.println(generatedAnnotation(getClass(), now));
        impl.println("class " + implName + " implements "
          + Traversable.class.getCanonicalName() + "<" + simpleName + ">, " + simpleName + " {");
        impl.println("protected " + implName + "() {}");
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
        String getterName = getterName(propName);

        String qsn = getParameterizedQualifiedSourceName(type);
        impl.println(qsn + " " + propName + ";");
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

        if (Kind.DDL.equals(type.getKind())) {
          // public Foo.Builder getFoo() { Foo.Builder toReturn = obj.foo.builder();
          builder.println("public " + qsn + ".Builder"
            + " get" + getterName + "() {");
          builder.println(qsn + ".Builder toReturn = obj." + propName + ".builder();");
          builder.println("obj." + propName + " = toReturn;");
          builder.println("return toReturn;");
          builder.println("}");
        } else {
          builder.println("public " + qsn + " get" + getterName + "() { return obj." + propName
            + "; }");
        }
        builder.println("public void set" + getterName + "(" + qsn + " value) { with" + getterName
          + "(value);}");
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
      builder.println(implName + " toReturn = obj;");
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
      impl.println("public " + builderName + " newInstance() { return new " + builderName + "(); }");

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

      intf.println("Builder builder();");
      intf.println("Builder newInstance();");
      intf.println("}");

      intf.close();
      impl.close();
    }
  }

  @Override
  public String getName() {
    return "industrial";
  }

  private String kindReference(Kind type) {
    if (type == null) {
      return "null";
    }
    return Kind.class.getCanonicalName() + "." + type.name();
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
