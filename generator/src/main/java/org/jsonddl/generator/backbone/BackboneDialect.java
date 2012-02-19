/*
 * Copyright 2012 Robert W. Vawter III <bob@vawter.org>
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
package org.jsonddl.generator.backbone;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.jsonddl.generator.Options;
import org.jsonddl.generator.TemplateDialect;
import org.jsonddl.model.Model;
import org.jsonddl.model.Schema;
import org.stringtemplate.v4.ST;

public class BackboneDialect extends TemplateDialect {

  @Override
  public String getName() {
    return "backbone";
  }

  @Override
  protected void doGenerate(Options options, Collector output, Schema s) throws IOException {
    ST enumTemplate = getTemplate("enumType", options);
    ST modelTemplate = getTemplate("modelObject", options);
    for (Model model : s.getModels().values()) {
      ST template = model.getEnumValues() == null ? modelTemplate : enumTemplate;
      OutputStream js = output.writeResource(options.getPackageName().replace('.', '/') + "/"
        + model.getName() + ".js");
      renderTemplate(forModel(template, model), new OutputStreamWriter(js));
    }
  }
}
