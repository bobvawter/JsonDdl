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

import java.util.List;

import org.jsonddl.model.Kind;

/**
 * Indicates that an object can traverse an object graph. Visitors will receive objects by
 * implementing methods with the following signatures:
 * 
 * <pre>
 * boolean visit(SomeType t, Context&lt;SomeType&gt; ctx);
 * 
 * void endVisit(SomeType t, Context&lt;SomeType&gt; ctx);
 * </pre>
 * 
 * Visitors need only implement the methods that they are interested in. The {@code visit} method
 * returns a boolean value to indicate if the object being visited should be traversed. If
 * {@code visit} is unimplemented, the default behavior is to traverse the properties of the object.
 */
public interface JsonDdlVisitor {
  /**
   * The Context interface provides a visitor with information about the object or property being
   * visited. When a visitor is traversing a Bulider, the Context can be used to mutate the object
   * in-place.
   */
  public interface Context<J> {
    /**
     * Indicates if {@link #insertAfter(Object)} and {@link #insertBefore(Object)} can be
     * successfully called.
     */
    boolean canInsert();

    /**
     * Indicates if {@link #remove()} can be successfully called.
     */
    boolean canRemove();

    /**
     * Indicates if {@link #replace(Object)} can be succesfully called.
     */
    boolean canReplace();

    /**
     * Returns the basic json data type of the property being visited.
     */
    Kind getKind();

    /**
     * Provides the declared java type of the leaf type in a map or list.
     */
    Class<?> getLeafType();

    /**
     * Provides the parameterization of list or map properties.
     */
    List<Kind> getNestedKinds();

    /**
     * Returns the name of the property being visited.
     */
    String getProperty();

    /**
     * Available while traversing a mutable list context. The inserted value will be traversed.
     */
    void insertAfter(J next);

    /**
     * Available while traversing a mutable list context. The inserted value will not be traversed.
     */
    void insertBefore(J previous);

    /**
     * Available in all mutable contexts to delete the current value.
     */
    void remove();

    /**
     * Available in all mutable contexts to replace the current value.
     */
    void replace(J replacement);

  }

  /**
   * Optional capability interface for visitors that wish to consume object properties dynamically.
   * The {@link #visitProperty(Object, Context)} method will be called before visiting the value of
   * the property, and any side-effects of {@code visitProperty} will be visible to a type-specific
   * {@code visit} method. Similarly, a type-specific {@code endVisit} will be called before
   * {@link #endVisitProperty(Object, Context)}.
   */
  public interface PropertyVisitor extends JsonDdlVisitor {
    <T> void endVisitProperty(T value, Context<T> ctx);

    <T> boolean visitProperty(T value, Context<T> ctx);
  }
}
