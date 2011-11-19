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

import java.util.Map;

import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;

/**
 * Used to re-link type references after all model types have been pulled in.
 */
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