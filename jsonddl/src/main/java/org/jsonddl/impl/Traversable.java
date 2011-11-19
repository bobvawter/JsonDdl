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
package org.jsonddl.impl;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;

/**
 * Internal interface used to traverse the properties of an object. Users should call
 * {@link JsonDdlObject#accept(JsonDdlVisitor)} instead.
 */
public interface Traversable<J extends JsonDdlObject<J>> extends JsonDdlObject<J> {
  J traverse(JsonDdlVisitor visitor);
}
