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
import java.util.Locale;
import java.util.Map;

import javax.annotation.Generated;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;
import org.jsonddl.generator.Dialect;
import org.jsonddl.generator.Options;
import org.jsonddl.generator.TypeAnswers;
import org.jsonddl.impl.ContextImpl.ObjectContext;
import org.jsonddl.impl.DigestVisitor;
import org.jsonddl.impl.Digested;
import org.jsonddl.impl.JsonMapVisitor;
import org.jsonddl.impl.Protected;
import org.jsonddl.impl.Traversable;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.ModelVisitor;
import org.jsonddl.model.Property;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.misc.Aggregate;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class IndustrialDialect implements Dialect {
  private static final String GENERATED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

  /**
   * The classes that are referenced from the templates via the {@code names} dictionary.
   */
  private static List<Class<?>> WELL_KNOWN_CLASSES = Arrays.<Class<?>> asList(ArrayList.class,
      Arrays.class, JsonDdlObject.Builder.class, Context.class, Digested.class,
      DigestVisitor.class, IndustrialDialect.class, Kind.class, Generated.class,
      JsonDdlObject.class, JsonDdlVisitor.class, JsonMapVisitor.class, LinkedHashMap.class,
      List.class, Map.class, ObjectContext.class, Protected.class, Traversable.class);

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

  private final STGroupFile templates;

  public IndustrialDialect() {
    templates = new STGroupFile(IndustrialDialect.class.getResource("industrial.stg"),
        "UTF8", '<', '>');
    // Convert a Type to its parameterized, qualified source name
    templates.registerRenderer(Type.class, new AttributeRenderer() {
      @Override
      public String toString(Object o, String formatString, Locale locale) {
        return TypeAnswers.getParameterizedQualifiedSourceName((Type) o);
      }
    });
    // Add a magic "getterName" property to Property objects for use by the templates
    templates.registerModelAdaptor(Property.class, new ObjectModelAdaptor() {
      @Override
      public Object getProperty(Interpreter interp, ST self, Object o, Object property,
          String propertyName) throws STNoSuchPropertyException {
        if ("getterName".equals(propertyName)) {
          return getterName(((Property) o).getName());
        }
        return super.getProperty(interp, self, o, property, propertyName);
      }
    });
    // Map TypeAnswers methods onto Type objects
    templates.registerModelAdaptor(Type.class, new ObjectModelAdaptor() {
      @Override
      public Object getProperty(Interpreter interp, ST self, Object o, Object property,
          String propertyName) throws STNoSuchPropertyException {
        if ("contextBuilderDeclaration".equals(propertyName)) {
          return TypeAnswers.getContextBuilderDeclaration((Type) o);
        }
        if ("nestedKinds".equals(propertyName)) {
          // A list of the inner kind parameterizations
          final List<Kind> kindReferences = new ArrayList<Kind>();
          ((Type) o).accept(new ModelVisitor() {
            @Override
            public boolean visit(Type t, Context<Type> ctx) {
              kindReferences.add(t.getKind());
              return true;
            }
          });
          kindReferences.remove(0);
          return kindReferences;
        }
        if ("shouldProtect".equals(propertyName)) {
          return TypeAnswers.shouldProtect((Type) o);
        }
        if (propertyName.startsWith("isKind")) {
          String kindName = propertyName.substring("isKind".length());
          Kind kind = Kind.valueOf(kindName.toUpperCase());
          return kind.equals(((Type) o).getKind());
        }
        return super.getProperty(interp, self, o, property, propertyName);
      }
    });
  }

  @Override
  public void generate(Options options, Dialect.Collector output, Schema s) throws IOException {
    Date now = new Date();
    ST intfTemplate = getTemplate("modelInterface", options, now);
    ST implTemplate = getTemplate("implementation", options, now);
    ST enumTemplate = getTemplate("enumType", options, now);
    for (Model model : s.getModels().values()) {
      for (ST template : Arrays.asList(intfTemplate, implTemplate, enumTemplate)) {
        template.remove("dialectProperties");
        Map<String, Map<String, String>> dialectProperties = model.getDialectProperties();
        if (dialectProperties != null) {
          template.add("dialectProperties", dialectProperties.get(getName()));
        }

        template.remove("model");
        template.add("model", model);
      }
      if (model.getEnumValues() != null) {
        Writer impl = output.writeJavaSource(options.getPackageName(), model.getName());
        enumTemplate.write(new AutoIndentWriter(impl));
        impl.close();
        continue;
      }

      Writer intf = output.writeJavaSource(options.getPackageName(), model.getName());
      renderTemplate(intfTemplate, intf);

      Writer impl = output.writeJavaSource(options.getPackageName(), model.getName() + "Impl");
      renderTemplate(implTemplate, impl);
    }

    writePackageVisitor(options, s, output, now);
  }

  @Override
  public String getName() {
    return "industrial";
  }

  private ST getTemplate(String name, Options options, Date now) {
    Aggregate agg = new Aggregate();
    for (Class<?> clazz : WELL_KNOWN_CLASSES) {
      agg.properties.put(clazz.getSimpleName(), clazz.getCanonicalName());
    }
    return templates.getInstanceOf(name)
        .add("names", agg)
        .add("now", new SimpleDateFormat(GENERATED_DATE_FORMAT).format(now))
        .add("options", options);
  }

  /**
   * Render the fully-initialized template into the given Writer. The Writer will be closed by this
   * method.
   */
  private void renderTemplate(ST template, Writer out) throws IOException {
    AutoIndentWriter intfWriter = new AutoIndentWriter(out);
    intfWriter.setLineWidth(80);
    template.write(intfWriter);
    out.close();
  }

  /**
   * Create a convenience base type that pre-defines all method signatures that a visitor for models
   * in the package would want to define.
   */
  private void writePackageVisitor(Options options, Schema schema, Collector collector,
      final Date now) throws IOException {
    final String packageName = options.getPackageName();
    StringBuilder visitorName = new StringBuilder(packageName.substring(packageName
        .lastIndexOf('.') + 1)).append("Visitor");
    visitorName.setCharAt(0, Character.toUpperCase(visitorName.charAt(0)));

    ST template = getTemplate("packageVisitor", options, now);
    template.add("schema", schema);
    template.add("visitorName", visitorName);

    Writer out = collector.writeJavaSource(packageName, visitorName.toString());
    template.write(new AutoIndentWriter(out));
    out.close();
  }
}
