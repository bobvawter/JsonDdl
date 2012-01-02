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

import java.io.IOException;
import java.io.Writer;

import org.jsonddl.generator.Options;
import org.jsonddl.generator.TemplateDialect;
import org.jsonddl.model.Model;
import org.jsonddl.model.Schema;
import org.stringtemplate.v4.ST;

/**
 * Creates plain-old Java objects.
 */
public class PojoDialect extends TemplateDialect {

  @Override
  public String getName() {
    return "pojo";
  }

  @Override
  protected void doGenerate(Options options, Collector output, Schema s) throws IOException {
    ST enumTemplate = getTemplate("enumType", options);
    ST modelTemplate = getTemplate("modelObject", options);
    for (Model model : s.getModels().values()) {
      ST template = model.getEnumValues() == null ? modelTemplate : enumTemplate;
      Writer intf = output.writeJavaSource(options.getPackageName(), model.getName());
      renderTemplate(forModel(template, model), intf);
    }
  }
}
