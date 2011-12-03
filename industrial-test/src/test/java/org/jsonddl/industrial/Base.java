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
package org.jsonddl.industrial;

/**
 * Demonstrates how behaviors can be added to generated industrial dialect objects.
 * <p>
 * <ul>
 * <li>Declare an interface with the additional methods that should be visible to callers.
 * <li>Declare an abstract class that implements the <em>generated interface</em> in order to have
 * access to the generated property / traversal methods.
 * <li>Add two dialect properties to the DDL declaration: {@code industrial:extends} with the name
 * of the abstract class and {@code industrial:implements} with the name of the interface.
 */
public interface Base {
  boolean isStringSet();

  void setRandomString();

  public abstract static class Impl implements Extended {
    @Override
    public boolean isStringSet() {
      return getString() != null;
    }

    @Override
    public void setRandomString() {
      if (!(this instanceof Extended.Builder)) {
        throw new IllegalStateException();
      }
      this.builder().setString("Hello World!");
    }
  }
}
