/*
 * Copyright 2012 Robert W. Vawter III <bob@vawter.org>
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

import java.io.UnsupportedEncodingException;

import org.jsonddl.JsonDdlObject;
import org.jsonddl.JsonDdlVisitor;
import org.jsonddl.JsonDdlVisitor.Context;
import org.jsonddl.impl.ContextImpl.ObjectContext;

public class VisitSupport {
  public static <J extends JsonDdlObject<J>> void endVisit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {}

  public static <J extends JsonDdlObject<J>> boolean visit(JsonDdlVisitor visitor, J obj,
      Context<J> ctx) {
    return true;
  }

  @SuppressWarnings("unchecked")
  static <T> Class<? extends T> asSubclass(Class<?> base, Class<T> desired) {
    return (Class<? extends T>) base;
  }

  @SuppressWarnings("unchecked")
  static <T> T cast(Object object, Class<T> to) {
    return (T) object;
  }

  static JsonDdlObject.Builder<?> create(Class<?> leafType) {
    return null;
  }

  static byte[] getBytes(String string) {
    try {
      return string.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  static <B extends JsonDdlObject<B>> ObjectContext.Builder<B> objectContextBuilder(Class<B> toBuild) {
    return new ObjectContext.Builder<B>();
  }
}
