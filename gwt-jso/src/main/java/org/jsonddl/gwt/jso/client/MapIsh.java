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

package org.jsonddl.gwt.jso.client;

import com.google.gwt.core.client.JavaScriptObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A MapIsh is provides a {@link Map}-like interface without implementing {@link Map} directly.
 */
public class MapIsh<T> extends JavaScriptObject {
  protected MapIsh() {}

  public final native T get(String key) /*-{
    return this[key];
  }-*/;

  public final List<String> keys() {
    List<String> toReturn = new ArrayList<String>();
    accumulateKeys(toReturn);
    return toReturn;
  }

  public final native void put(String key, T value) /*-{
    this[key] = value;
  }-*/;

  private native void accumulateKeys(List<String> list) /*-{
    for (key in this) {
      if (this.hasOwnProperty(key)) {
        list.@java.util.List::add(Ljava/lang/Object;)(key);
      }
    }
  }-*/;
}
