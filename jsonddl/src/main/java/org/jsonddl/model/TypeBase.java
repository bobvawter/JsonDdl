package org.jsonddl.model;

public abstract class TypeBase implements Type {

  public boolean isBroken() {
    return getKind() == null;
  }
}
