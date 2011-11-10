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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class Context<J extends JsonDdlObject<J>> {
  public static class ImmutableContext<J extends JsonDdlObject<J>> extends Context<J> {
    private final J value;

    public ImmutableContext(String property, J value) {
      super(property);
      this.value = value;
    }

    @Override
    public J traverse(JsonDdlVisitor visitor) {
      value.traverse(visitor, this);
      return value;
    }
  }

  public static class ImmutableListContext<J extends JsonDdlObject<J>> extends Context<J> {
    protected final List<J> list;
    protected ListIterator<J> it;

    public ImmutableListContext(String property, List<J> list) {
      super(property);
      this.list = new ArrayList<J>(list);
      it = list.listIterator();
    }

    @Override
    public List<J> traverse(JsonDdlVisitor visitor) {
      it = list.listIterator();
      while (it.hasNext()) {
        it.next().traverse(visitor, this);
      }
      return list;
    }
  }

  public static class ListContext<J extends JsonDdlObject<J>> extends ImmutableListContext<J> {
    public ListContext(String property, List<J> list) {
      super(property, list);
    }

    @Override
    public void insertAfter(J next) {
      it.add(next);
    }

    @Override
    public void insertBefore(J previous) {
      int current = it.previousIndex() + 1;
      list.add(current, previous);
      it = list.listIterator(current + 1);
    }

    @Override
    public void replace(J replacement) {
      it.set(replacement);
    }
  }

  public static class SettableContext<J extends JsonDdlObject<J>> extends Context<J> {
    private J value;

    public SettableContext(String property, J value) {
      super(property);
      this.value = value;
    }

    @Override
    public void replace(J replacement) {
      value = replacement;
    }

    @Override
    public J traverse(JsonDdlVisitor visitor) {
      value.traverse(visitor, this);
      return value;
    }
  }

  private final String property;

  protected Context(String property) {
    this.property = property;
  }

  public String getProperty() {
    return property;
  }

  public void insertAfter(J next) {
    throw new UnsupportedOperationException();
  }

  public void insertBefore(J previous) {
    throw new UnsupportedOperationException();
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  public void replace(J replacement) {
    throw new UnsupportedOperationException();
  }

  public abstract Object traverse(JsonDdlVisitor visitor);
}
