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

package org.jsonddl.generator.gwt;

import java.util.Arrays;
import java.util.List;

import org.jsonddl.generator.pojo.PojoDialect;

/**
 * Creates simple JSO-based accessors for GWT.
 */
public class GwtJsoDialect extends PojoDialect {
  private static final String ARRAY_BOOLEAN_NAME = "com.google.gwt.core.client.JsArrayBoolean";
  private static final String ARRAY_INTEGER_NAME = "com.google.gwt.core.client.JsArrayInteger";
  private static final String ARRAY_JSO_NAME = "com.google.gwt.core.client.JsArray";
  private static final String ARRAY_NUMBER_NAME = "com.google.gwt.core.client.JsArrayNumber";
  private static final String ARRAY_STRING_NAME = "com.google.gwt.core.client.JsArrayString";
  private static final String JSO_NAME = "com.google.gwt.core.client.JavaScriptObject";
  private static final String MAPISH_NAME = "org.jsonddl.gwt.jso.client.MapIsh";

  private static final List<Class<?>> WELL_KNOWN_CLASSES = Arrays.<Class<?>> asList(
      boolean.class, double.class, int.class, Enum.class, String.class);
  private static final List<String> WELL_KNOWN_NAMES = Arrays.asList(
      ARRAY_BOOLEAN_NAME, ARRAY_INTEGER_NAME, ARRAY_JSO_NAME, ARRAY_NUMBER_NAME, ARRAY_STRING_NAME,
      JSO_NAME, MAPISH_NAME);

  @Override
  public String getName() {
    return "gwt-jso";
  }

  @Override
  protected List<Class<?>> getTemplateClasses() {
    return WELL_KNOWN_CLASSES;
  }

  @Override
  protected List<String> getTemplateClassNames() {
    return WELL_KNOWN_NAMES;
  }
}
