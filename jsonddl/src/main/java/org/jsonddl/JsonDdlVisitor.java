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

/**
 * Indicates that an object conforms to the following protocol.
 */
public interface JsonDdlVisitor {
  /**
   * Optional capability interface for visitors that wish to consume object properties dynamically.
   */
  public interface PropertyVisitor extends JsonDdlVisitor {
    <T> void endVisitProperty(T value, Context<T> ctx);

    <T> boolean visitProperty(T value, Context<T> ctx);
  }
}
