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
/**
 * Defines extra datatypes used by the code generator, but that are not specific
 * to specifying a schema.
 */
var schema = {
  /**
   * Controls behaviors in the code generator.
   */
  Options : {
    /**
     * The list of dialects to execute. If unset, the generator will execute all
     * dialects that are found by examining the
     * {@code META-INF/services/org.jsonddl.generator.Dialect} resources.
     */
    dialects : [ "" ],
    /**
     * A hook point for providing options to dialects.
     */
    extraOptions : {
      "" : ""
    },
    /**
     * Indicates that the generator should expect normalized input and not the
     * usual idiomatic form.
     */
    normalizedInput : false,
    /**
     * The namespace to use when generating output files.
     */
    packageName : ""
  }
};
