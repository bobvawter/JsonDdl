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
 * This file is a schema that defines the jsonddl schema.
 * <p>
 * The var declaration at the top isn't actually important to the parser, but it
 * does make the schema a valid JavaScript program.
 */
var schema = {
  /**
   * A Schema is the top-level object that encapsulates the normalized form of a
   * json-ddl schema.
   */
  Schema : {
    /**
     * A map of object models by simple name.
     */
    models : {
      "" : "Model"
    }
  },
  /**
   * A model represents a single kind of object within the schema. There is a
   * 1:1 correspondence between Models and Java classes or JS type closures.
   */
  Model : {
    comment : "",
    enumValues : [ "EnumValue" ],
    name : "",
    properties : [ "Property" ]
  },
  /**
   * A property is a pair of a name and a type.
   */
  Property : {
    /**
     * This comment will be stored in this property.
     */
    comment : "",
    name : "",
    type : "Type"
  },
  EnumValue : {
    comment : "",
    name : ""
  },
  /**
   * This is a simplified type system, representing only the types of data that
   * can be directly expressed in JSON.
   */
  Kind : [ "BOOLEAN", "DDL", "DOUBLE", "ENUM",
  /**
   * Indicates that a type named by a property declaration could not be resolved
   * to a model type declared within the schema. In this case, the
   * interpretation of the type name will be left to the code generator.
   */
  "EXTERNAL", "INTEGER", "LIST",
  /**
   * The only map key type that makes sense for json is a String.
   */
  "MAP", "STRING" ],
  Type : {
    name : "",
    kind : "Kind",
    listElement : "Type",
    mapKey : "Type",
    mapValue : "Type"
  }
};