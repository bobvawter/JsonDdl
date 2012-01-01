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

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;
import org.jsonddl.generator.Dialect;
import org.jsonddl.generator.Options;
import org.jsonddl.generator.TemplateDialect;
import org.jsonddl.impl.ContextImpl.ListContext;
import org.jsonddl.impl.ContextImpl.MapContext;
import org.jsonddl.impl.ContextImpl.ObjectContext;
import org.jsonddl.impl.ContextImpl.ValueContext;
import org.jsonddl.impl.DigestVisitor;
import org.jsonddl.impl.Digested;
import org.jsonddl.impl.JsonMapVisitor;
import org.jsonddl.impl.Protected;
import org.jsonddl.impl.Traversable;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.Schema;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class IndustrialDialect extends TemplateDialect {

  /**
   * The classes that are referenced from the templates via the {@code names} dictionary.
   */
  private static List<Class<?>> WELL_KNOWN_CLASSES = Arrays.<Class<?>> asList(ArrayList.class,
      Arrays.class, JsonDdlObject.Builder.class, Context.class, Digested.class,
      DigestVisitor.class, Generated.class, IndustrialDialect.class, Kind.class,
      JsonDdlObject.class, JsonDdlVisitor.class, JsonMapVisitor.class, LinkedHashMap.class,
      List.class, ListContext.class, Map.class, MapContext.class, ObjectContext.class,
      Protected.class, Traversable.class, UnsupportedOperationException.class, ValueContext.class);

  public static String generatedAnnotation(Class<? extends Dialect> clazz, Date now) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String generated = "@" + Generated.class.getCanonicalName() + "(value=\""
      + clazz.getCanonicalName() + "\", date=\"" + sdf.format(now) + "\")";
    return generated;
  }

  public static String getterName(String propertyName) {
    return Character.toUpperCase(propertyName.charAt(0))
      + (propertyName.length() > 1 ? propertyName.substring(1) : "");
  }

  @Override
  public String getName() {
    return "industrial";
  }

  @Override
  protected void doGenerate(Options options, Dialect.Collector output, Schema s) throws IOException {
    ST intfTemplate = getTemplate("modelInterface", options);
    ST implTemplate = getTemplate("implementation", options);
    ST enumTemplate = getTemplate("enumType", options);
    for (Model model : s.getModels().values()) {
      if (model.getEnumValues() != null) {
        Writer impl = output.writeJavaSource(options.getPackageName(), model.getName());
        renderTemplate(forModel(enumTemplate, model), impl);
        continue;
      }

      Writer intf = output.writeJavaSource(options.getPackageName(), model.getName());
      renderTemplate(forModel(intfTemplate, model), intf);

      Writer impl = output.writeJavaSource(options.getPackageName(), model.getName() + "Impl");
      renderTemplate(forModel(implTemplate, model), impl);
    }

    writePackageVisitor(options, s, output);
  }

  @Override
  protected List<Class<?>> getTemplateClasses() {
    return WELL_KNOWN_CLASSES;
  }

  @Override
  protected STGroup loadTemplates() {
    return new STGroupFile(IndustrialDialect.class.getResource("industrial.stg"),
        "UTF8", '<', '>');
  }

  /**
   * Create a convenience base type that pre-defines all method signatures that a visitor for models
   * in the package would want to define.
   */
  private void writePackageVisitor(Options options, Schema schema, Collector collector)
      throws IOException {
    final String packageName = options.getPackageName();
    StringBuilder visitorName = new StringBuilder(packageName.substring(packageName
        .lastIndexOf('.') + 1)).append("Visitor");
    visitorName.setCharAt(0, Character.toUpperCase(visitorName.charAt(0)));

    ST template = getTemplate("packageVisitor", options);
    template.add("schema", schema);
    template.add("visitorName", visitorName);

    Writer out = collector.writeJavaSource(packageName, visitorName.toString());
    renderTemplate(template, out);
  }
}
