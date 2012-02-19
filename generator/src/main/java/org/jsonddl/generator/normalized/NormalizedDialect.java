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
package org.jsonddl.generator.normalized;

import java.io.IOException;
import java.io.OutputStream;

import org.jsonddl.generator.Dialect;
import org.jsonddl.generator.Options;
import org.jsonddl.model.Schema;

import com.google.gson.Gson;

/**
 * This dialect simply converts a {@link Schema} to its normalized JSON format.
 */
public class NormalizedDialect implements Dialect {

  @Override
  public void generate(Options options, Collector output, Schema s) throws IOException {
    OutputStream out = output.writeResource(
        options.getPackageName().replace('.', '/') + "/schema.js");
    out.write(new Gson().toJson(s.toJsonObject()).getBytes(Collector.SOURCE_CHARSET));
    out.close();
  }

  @Override
  public String getName() {
    return "normalized";
  }
}
