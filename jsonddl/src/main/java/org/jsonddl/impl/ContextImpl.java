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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;
import org.jsonddl.JsonDdlVisitor.PropertyVisitor;
import org.jsonddl.model.Kind;

public abstract class ContextImpl<J> implements Context<J> {
  public static class Builder<C extends PropertyContext<?, V>, V> {
    protected C ctx;

    Builder(C ctx) {
      this.ctx = ctx;
    }

    public C build() {
      C toReturn = ctx;
      ctx = null;
      return toReturn;
    }

    public Builder<C, V> withKind(Kind kind) {
      ctx.kind = kind;
      return this;
    }

    public Builder<C, V> withLeafType(Class<?> leafType) {
      ctx.leafType = leafType;
      return this;
    }

    public Builder<C, V> withMutability(boolean isMutable) {
      ctx.mutable = isMutable;
      return this;
    }

    public Builder<C, V> withNestedKinds(List<Kind> kinds) {
      ctx.nestedKinds = Protected.object(kinds);
      return this;
    }

    public Builder<C, V> withProperty(String property) {
      ctx.property = property;
      return this;
    }

    public Builder<C, V> withValue(V value) {
      ctx.setValue(value);
      return this;
    }
  }

  /**
   * Allows traversal of a {@link List} of {@link JsonDdlObject} instances.
   */
  public static class ListContext<J extends JsonDdlObject<J>> extends PropertyContext<J, List<J>> {
    public static class Builder<J extends JsonDdlObject<J>> extends
        ContextImpl.Builder<ListContext<J>, List<J>> {
      public Builder() {
        super(new ListContext<J>());
      }
    }

    private boolean didReplace;
    private ListIterator<J> it;

    ListContext() {}

    @Override
    public boolean canInsert() {
      return isMutable();
    }

    @Override
    public boolean canRemove() {
      return isMutable();
    }

    @Override
    public boolean canReplace() {
      return isMutable();
    }

    @Override
    public void insertAfter(J next) {
      if (isMutable()) {
        it.add(next);
      } else {
        super.insertAfter(next);
      }
    }

    @Override
    public void insertBefore(J previous) {
      if (isMutable()) {
        int current = it.previousIndex() + 1;
        value.add(current, previous);
        it = value.listIterator(current + 1);
      } else {
        super.insertBefore(previous);
      }
    }

    @Override
    public void remove() {
      if (isMutable()) {
        it.remove();
      } else {
        super.remove();
      }
    }

    @Override
    public void replace(J replacement) {
      if (isMutable()) {
        didReplace = true;
        it.set(replacement);
      } else {
        super.replace(replacement);
      }
    }

    @Override
    public void setValue(List<J> value) {
      if (value != null && isMutable()) {
        value = new ArrayList<J>(value);
      }
      super.setValue(value);
    }

    @Override
    protected void doTraverse(JsonDdlVisitor visitor) {
      it = value.listIterator();
      while (it.hasNext()) {
        didReplace = false;
        J temp = configureNestedBuilderKinds(new ObjectContext.Builder<J>())
            .withValue(it.next())
            .build().traverse(visitor);
        if (isMutable() && !didReplace) {
          it.set(temp);
        }
      }
    }
  }

  public static class MapContext<J extends JsonDdlObject<J>> extends
      PropertyContext<J, Map<String, J>> {
    public static class Builder<J extends JsonDdlObject<J>> extends
        ContextImpl.Builder<MapContext<J>, Map<String, J>> {
      public Builder() {
        super(new MapContext<J>());
      }
    }

    private Map.Entry<String, J> currentEntry;
    private boolean didReplace;
    private Iterator<Map.Entry<String, J>> it;

    MapContext() {}

    @Override
    public boolean canRemove() {
      return isMutable();
    }

    @Override
    public boolean canReplace() {
      return isMutable();
    }

    @Override
    public void remove() {
      if (isMutable()) {
        it.remove();
      } else {
        super.remove();
      }
    }

    @Override
    public void replace(J replacement) {
      if (isMutable()) {
        didReplace = true;
        currentEntry.setValue(replacement);
      } else {
        super.replace(replacement);
      }
    }

    @Override
    public void setValue(Map<String, J> value) {
      if (value != null && isMutable()) {
        value = new LinkedHashMap<String, J>(value);
      }
      super.setValue(value);
    }

    @Override
    protected void doTraverse(JsonDdlVisitor visitor) {
      it = value.entrySet().iterator();
      while (it.hasNext()) {
        currentEntry = it.next();
        J value = configureNestedBuilderKinds(new ObjectContext.Builder<J>())
              .withValue(currentEntry.getValue())
              .build().traverse(visitor);
        if (isMutable() && !didReplace) {
          currentEntry.setValue(value);
        }
      }
    }
  }

  /**
   * Allows the traversal of a single {@link JsonDdlObject}.
   */
  public static class ObjectContext<J extends JsonDdlObject<J>> extends
      ValueContext<J> {
    public static class Builder<J extends JsonDdlObject<J>> extends
        ValueContext.Builder<J> {
      public Builder() {
        super(new ObjectContext<J>());
      }
    }

    ObjectContext() {}

    @Override
    protected void doTraverse(JsonDdlVisitor visitor) {
      if (VisitSupport.visit(visitor, value, this)) {
        if (isMutable()) {
          @SuppressWarnings("unchecked")
          J temp = ((Traversable<J>) value.builder()).traverse(visitor);
          if (!didChange) {
            value = temp;
          }
        } else {
          ((Traversable<J>) value).traverse(visitor);
        }
      }
      VisitSupport.endVisit(visitor, value, this);
    }
  }

  /**
   * Contains a single value that may be replaced, but does not provide any traversal.
   */
  public static class ValueContext<T> extends PropertyContext<T, T> {
    public static class Builder<T> extends ContextImpl.Builder<ValueContext<T>, T> {
      public Builder() {
        this(new ValueContext<T>());
      }

      public Builder(ValueContext<T> ctx) {
        super(ctx);
      }
    }

    protected boolean didChange;

    ValueContext() {}

    @Override
    public boolean canReplace() {
      return isMutable();
    }

    public T getValue() {
      return value;
    }

    @Override
    public void replace(T replacement) {
      if (isMutable()) {
        didChange = true;
        value = replacement;
      } else {
        super.replace(replacement);
      }
    }

    @Override
    protected void doTraverse(JsonDdlVisitor visitor) {}
  }

  /**
   * Allows the traversal of a single property. This method will dispatch to the optional
   * {@link PropertyVisitor} interface methods.
   */
  static abstract class PropertyContext<J, T> extends ContextImpl<J> {
    protected T value;

    public void setValue(T value) {
      this.value = value;
    }

    /**
     * Returns the value of the context after traversal is complete.
     */
    public final T traverse(JsonDdlVisitor visitor) {
      // Avoid treating a root object as though it were embedded in a property
      if (property != null && visitor instanceof PropertyVisitor) {
        // Create a ValueContext to hold the replacement
        org.jsonddl.impl.ContextImpl.ValueContext<T> ctx =
            configureNestedBuilderKinds(new ValueContext.Builder<T>())
                .withValue(value)
                .build();

        // Allow the PropertyVisitor to replace the value, then visit the (replaced) value
        if (((PropertyVisitor) visitor).visitProperty(value, ctx)) {
          value = ctx.getValue();
          if (value != null) {
            doTraverse(visitor);
          }
        }
        ((PropertyVisitor) visitor).endVisitProperty(value, ctx);
        value = ctx.getValue();
      } else if (value != null) {
        doTraverse(visitor);
      }
      return value;
    }

    protected abstract void doTraverse(JsonDdlVisitor visitor);

  }

  Kind kind;
  Class<?> leafType;
  List<Kind> nestedKinds = Collections.emptyList();
  boolean mutable;
  String property;

  ContextImpl() {}

  @Override
  public boolean canInsert() {
    return false;
  }

  @Override
  public boolean canRemove() {
    return false;
  }

  @Override
  public boolean canReplace() {
    return false;
  }

  @Override
  public Kind getKind() {
    return kind;
  }

  @Override
  public Class<?> getLeafType() {
    return leafType;
  }

  @Override
  public List<Kind> getNestedKinds() {
    return nestedKinds;
  }

  @Override
  public String getProperty() {
    return property;
  }

  @Override
  public void insertAfter(J next) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void insertBefore(J previous) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void replace(J replacement) {
    throw new UnsupportedOperationException();
  }

  protected boolean isMutable() {
    return mutable;
  }

  <B extends Builder<?, ?>> B configureNestedBuilderKinds(B builder) {
    builder
        .withKind(getKind())
        .withNestedKinds(getNestedKinds())
        .withLeafType(getLeafType())
        .withMutability(isMutable())
        .withProperty(getProperty());
    return builder;
  }
}
