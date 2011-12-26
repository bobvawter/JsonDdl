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

import org.junit.Test;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayBoolean;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * A few simple sanity tests.
 */
public class GwtTestExample extends GWTTestCase {

  private static native String stringify(Object o) /*-{return JSON.stringify(o);}-*/;

  @Override
  public String getModuleName() {
    return "org.jsonddl.gwt.jso.JsonDdlGwtJsoTest";
  }

  @Test
  public void test() {
    Example e = JavaScriptObject.createObject().cast();
    e.setABoolean(true);
    e.setAnExample(JavaScriptObject.createObject().<Example> cast());
    e.setAnExampleList(JavaScriptObject.createArray().<JsArray<Example>> cast());
    e.setAnIntegralList(JavaScriptObject.createArray().<JsArrayInteger> cast());
    e.setAString("Hello world!");
    e.setDouble(42.2);
    e.setInt(42);

    MapIsh<JsArrayBoolean> mapish = JavaScriptObject.createObject().cast();
    mapish.put("Hello", JavaScriptObject.createArray().<JsArrayBoolean> cast());
    e.setAStringToListOfBooleanMap(mapish);

    assertEquals(
        "{\"aBoolean\":true,\"anExample\":{},\"anExampleList\":[],\"anIntegralList\":[],"
          + "\"aString\":\"Hello world!\",\"double\":42.2,\"int\":42,"
          + "\"aStringToListOfBooleanMap\":{\"Hello\":[]}}",
        stringify(e));
  }

  @Test
  public void testEmptyExample() {
    Example e = JsonUtils.safeEval("{}");
    assertNull(e.getAnExample());
    assertNull(e.getAnExampleList());
    assertNull(e.getAnIntegralList());
    assertNull(e.getAString());
    assertNull(e.getAStringToListOfBooleanMap());
    assertEquals(0.0, e.getDouble());
    assertEquals(0, e.getInt());
    assertFalse(e.isABoolean());
  }
}
