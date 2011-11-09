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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Generated;

import org.jsonddl.Context;
import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
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
        File f = new File("/tmp/foo");
        f = new File(f, packageName.replace('.', File.separatorChar));
        f.mkdirs();
        f = new File(f, simpleName + ".java");
        return new FileOutputStream(f);
      }
    });
  }

  public boolean generate(InputStream schema, String packageName, Collector output)
      throws IOException {
    Parser parser = new Parser();
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

    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    for (ObjectProperty prop : obj.getElements()) {
      String simpleName = extractName(prop);
      ObjectLiteral typeDeclaration = castOrNull(ObjectLiteral.class, prop.getRight());
      Map<String, String> typeMap = typeMap(typeDeclaration);

      PrintWriter out = new PrintWriter(new OutputStreamWriter(output.writeImplementation(
          packageName, simpleName)));

      out.println("package " + packageName + ";");
      out.println("@" + Generated.class.getCanonicalName() + "(value=\""
        + getClass().getCanonicalName() + "\", date=\"" + sdf.format(now) + "\")");
      out.print("public class " + simpleName);
      if (typeMap.containsKey("implements")) {
        out.print(" implements " + typeMap.remove("implements") + ", ");
      } else {
        out.print(" implements ");
      }
      out.print(JsonDdlObject.class.getCanonicalName() + "<" + simpleName + ">");
      out.println(" {");

      StringWriter stringWriter = new StringWriter();
      PrintWriter builder = new PrintWriter(stringWriter);
      builder.println("public static class Builder implements "
        + JsonDdlObject.Builder.class.getCanonicalName() + "<" + simpleName + "> {");
      builder.println("private " + simpleName + " obj = new " + simpleName + "();");
      builder.println("public " + simpleName + " build() {");
      builder.println(simpleName + " toReturn = obj;");
      builder.println("obj = null;");
      builder.println("return toReturn;");
      builder.println("}");
      for (Map.Entry<String, String> entry : typeMap.entrySet()) {
        String type = entry.getValue();
        String propName = entry.getKey();
        String getterName = Character.toUpperCase(propName.charAt(0))
          + (propName.length() > 1 ? propName.substring(1) : "");

        out.println("private " + type + " " + propName + ";");
        out.println("public " + type + " get" + getterName + "() {return " + propName + ";}");
        builder.println("public Builder with" + getterName + "(" + type + " value) { obj."
          + propName + " = value; return this;}");
      }
      builder.append("}");
      out.println(stringWriter.getBuffer().toString());

      out.println("public void traverse(" + JsonDdlVisitor.class.getCanonicalName() + " visitor, "
        + Context.class.getCanonicalName() + "<" + simpleName + "> ctx) {}");
      out.println("}");

      out.close();

      output.println(simpleName + ":" + typeDeclaration.toSource());
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

  private Map<String, String> typeMap(ObjectLiteral lit) {
    Map<String, String> toReturn = new TreeMap<String, String>();

    for (ObjectProperty prop : lit.getElements()) {
      String propertyName = extractName(prop);
      String typeName = typeName(prop.getRight(), false);
      toReturn.put(propertyName, typeName);
    }

    return toReturn;
  }

  /**
   * Convert an AST node to a Java type reference.
   */
  private String typeName(AstNode node, boolean forceBoxed) {
    StringLiteral string = castOrNull(StringLiteral.class, node);
    if (string != null) {
      String value = string.getValue();
      if (value.isEmpty()) {
        return "String";
      }
      return value;
    }
    Name name = castOrNull(Name.class, node);
    if (name != null) {
      return name.getIdentifier();
    }

    ArrayLiteral array = castOrNull(ArrayLiteral.class, node);
    if (array != null) {
      if (array.getSize() != 1) {
        throw new UnexpectedNodeException(array, "Expecting exactly one entry");
      }
      return List.class.getName() + "<" + typeName(array.getElement(0), true) + ">";
    }

    KeywordLiteral keyword = castOrNull(KeywordLiteral.class, node);
    if (keyword != null && keyword.isBooleanLiteral()) {
      return forceBoxed ? "Boolean" : "boolean";
    }

    NumberLiteral num = castOrNull(NumberLiteral.class, node);
    if (num != null) {
      double d = num.getNumber();
      if (Math.round(d) == d) {
        return forceBoxed ? "Integer" : "int";
      } else {
        return forceBoxed ? "Double" : "double";
      }
    }
    ObjectLiteral obj = castOrNull(ObjectLiteral.class, node);
    if (obj != null) {
      if (obj.getElements().size() != 1) {
        throw new UnexpectedNodeException(obj, "Expecting exactly one property");
      }
      ObjectProperty prop = obj.getElements().get(0);
      return Map.class.getName() + "<" + typeName(prop.getLeft(), true) + ", "
        + typeName(prop.getRight(), true) + ">";
    }

    throw new UnexpectedNodeException(node);
  }
}
