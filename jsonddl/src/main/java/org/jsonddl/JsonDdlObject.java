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
package org.jsonddl;

import java.util.Map;

public interface JsonDdlObject {
  public interface Builder<J extends JsonDdlObject> {
    Builder<J> accept(JsonDdlVisitor visitor);

    J build();

    Builder<J> from(J copyFrom);

    Builder<J> from(Map<String, Object> map);

    Builder<J> traverse(JsonDdlVisitor visitor);
  }

  JsonDdlObject accept(JsonDdlVisitor visitor);

  //
  // <O extends JsonDdlObject<O>> O as(Class<O> clazz);
  //
  Builder<? extends JsonDdlObject> builder();

  Class<? extends JsonDdlObject> getDdlObjectType();

  Builder<? extends JsonDdlObject> newInstance();

  Map<String, Object> toJsonObject();

  JsonDdlObject traverse(JsonDdlVisitor visitor);

}
