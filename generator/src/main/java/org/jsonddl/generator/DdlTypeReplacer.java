package org.jsonddl.generator;

import java.util.Map;

import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;
import org.jsonddl.model.Kind;
import org.jsonddl.model.Model;
import org.jsonddl.model.Schema;
import org.jsonddl.model.Type;

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