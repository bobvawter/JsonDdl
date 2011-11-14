package org.jsonddl;

import org.jsonddl.JsonDdlObject.Builder;

public interface BuilderFactory {
  <J extends JsonDdlObject<J>> Builder<J> builder(Class<J> clazz);
}
