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
