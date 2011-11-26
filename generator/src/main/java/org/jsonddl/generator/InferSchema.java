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

import static org.jsonddl.generator.industrial.IndustrialDialect.getterName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jsonddl.generator.idiomatic.IdiomaticDialect;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.Property;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Examines a JSON payload and constructs a schema to access it. The output from this tool is an
 * idiomatic representation of the schema. The generated schema is very literal; there may be many
 * opportunities to simplify the schema.
 */
public class InferSchema {

  public static void main(String[] args) throws IOException {
    System.exit(new InferSchema().exec(args) ? 0 : 1);
  }

  /**
   * TODO: Implement collection accumulator methods on industrial types.
   */
  private Map<String, Model> allModels = new TreeMap<String, Model>();

  /**
   * Read a json object literal and attempt to infer a schema from it.
   * 
   * @param in the InputStream will be closed by this method
   * @param rootTypeName the name of the type of object contained in the payload
   * @return a Schema describing the payload
   */
  public Schema inferFrom(InputStream in, String rootTypeName)
      throws IOException {
    JsonElement root;
    InputStreamReader sourceReader = new InputStreamReader(in);
    root = new JsonParser().parse(sourceReader);
    sourceReader.close();

    if (!root.isJsonObject()) {
      throw new IllegalArgumentException("The input must be a JSON object literal");
    }
    inferFromObject(rootTypeName, root.getAsJsonObject());
    return new Schema.Builder().withModels(allModels).build();
  }

  private boolean exec(String[] args) throws IOException {
    if (args.length != 3) {
      System.err.println(String.format("%s inputFile.js outputFile.js RootTypeName", getClass()
          .getCanonicalName()));
      return false;
    }
    File input = new File(args[0]);
    final File output = new File(args[1]);
    String rootTypeName = args[2];

    Schema s = inferFrom(new FileInputStream(input), rootTypeName);
    Dialect d = new IdiomaticDialect();
    d.generate(new Options.Builder().withPackageName("inferred").build(), new Dialect.Collector() {
      @Override
      public void println(String message) {
        System.out.println(message);
      }

      @Override
      public void println(String format, Object... args) {
        println(String.format(format, args));
      }

      @Override
      public OutputStream writeJavaSource(String packageName, String simpleName) throws IOException {
        throw new UnsupportedOperationException();
      }

      @Override
      public OutputStream writeResource(String path) throws IOException {
        return new FileOutputStream(output);
      }
    }, s);
    return true;
  }

  private Type inferFromArray(String propertyName, JsonArray array) {
    String subName = propertyName + "Item";
    List<Type> subTypes = new ArrayList<Type>();
    for (JsonElement elt : array) {
      subTypes.add(inferType(subName, elt));
    }

    boolean homogeneous = true;
    Type firstType = null;
    for (Type t : subTypes) {
      if (firstType == null) {
        firstType = t;
      } else {
        homogeneous = TypeAnswers.isSameType(firstType, t);
        if (!homogeneous) {
          break;
        }
      }
    }

    if (homogeneous) {
      return new Type.Builder().withKind(Kind.LIST)
          .withListElement(firstType).build();
    } else {
      throw new UnsupportedOperationException("Cannot support heterogeneous collections");
    }
  }

  private Type inferFromObject(String propertyName, JsonObject object) {
    String typeName = getterName(propertyName);
    Model model = allModels.get(typeName);
    if (model == null) {
      model = new Model.Builder()
          .withName(typeName)
          .withProperties(new ArrayList<Property>());
      allModels.put(typeName, model);
    }
    List<Property> properties = model.getProperties();
    Set<String> knownProperties = new HashSet<String>();
    for (Property p : properties) {
      knownProperties.add(p.getName());
    }
    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      Type type = inferType(entry.getKey(), entry.getValue());
      if (!knownProperties.contains(entry.getKey())) {
        Property prop = new Property.Builder()
            .withName(entry.getKey())
            .withType(type)
            .build();
        properties.add(prop);
      }
    }
    return new Type.Builder()
        .withKind(Kind.DDL)
        .withName(model.getName())
        .build();
  }

  private Type inferType(String propertyName, JsonElement node) {
    if (node.isJsonPrimitive()) {
      JsonPrimitive p = node.getAsJsonPrimitive();
      if (p.isBoolean()) {
        return new Type.Builder().withKind(Kind.BOOLEAN).build();
      }
      if (p.isNumber()) {
        return new Type.Builder().withKind(Kind.DOUBLE).build();
      }
      if (p.isString()) {
        return new Type.Builder().withKind(Kind.STRING).build();
      }
      throw new RuntimeException("Unhandled primitive type " + p.toString());
    }

    if (node.isJsonObject()) {
      JsonObject object = node.getAsJsonObject();
      return inferFromObject(propertyName, object);
    }

    if (node.isJsonArray()) {
      return inferFromArray(propertyName, node.getAsJsonArray());
    }

    throw new RuntimeException("Could not infer type from node " + node.toString());

  }
}
