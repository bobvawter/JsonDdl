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

import org.jsonddl.JsonDdlVisitor.Context;

/**
 * A base type implemented by all generated objects.
 */
public interface JsonDdlObject<J extends JsonDdlObject<J>> {
  /**
   * Builders are used to construct instances of {@link JsonDdlObject}.
   */
  public interface Builder<J extends JsonDdlObject<J>> extends JsonDdlObject<J> {
    /**
     * Finish constructing the object and return in. Any mutable collections passed into property
     * setters will be copied into immutable variants and the {@link #build()} method called on any
     * builders passed in.
     */
    J build();

    /**
     * Initialize the builder from an existing object.
     */
    Builder<J> from(J copyFrom);

    /**
     * Initialize the builder from a map of properties.
     */
    Builder<J> from(Map<String, Object> map);
  }

  /**
   * Traverse a visitor over an object graph. If the current object is a builder, the visitor will
   * be able to modify the properties and structure of the object in place. If the visitor calls
   * {@link Context#replace(Object)} on the top-level object being visited, the replacement will be
   * returned from this method.
   * 
   * @return the current value of the object
   */
  J accept(JsonDdlVisitor visitor);

  /**
   * Returns a new Builder initialized with the current state of the object. If called on a Builder,
   * returns the some object.
   */
  Builder<J> builder();

  /**
   * Returns {@code J} parameterization.
   */
  Class<J> getDdlObjectType();

  /**
   * Create an uninitialized instance of a builder for the type of the current object.
   */
  Builder<J> newInstance();

  /**
   * Construct a mutable map containing the properties of the object.
   */
  Map<String, Object> toJsonObject();
}
