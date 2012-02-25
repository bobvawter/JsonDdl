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
var tests = {
  /**
   * Verify that a simple stringify works correctly.
   */
  testStringify : function() {
    var schema = new Schema();
    schema.setComment("Hello world");
    "Hello world" === schema.getComment() || die();
    '{"comment":"Hello world"}' === JSON.stringify(schema) || die();
  },
  /**
   * Ensure that wrapping a raw object causes Model and Collection conversions
   * to happen.
   */
  testWrap : function() {
    var schema = new Schema(testData);
    schema.getComment() || die();

    var schemaModel = schema.getModels().get('Schema');
    schemaModel || die();
    schemaModel.getProperties().length === 2 || die();

    var commentProperty = schemaModel.getProperties().at(0);
    commentProperty || die();
    commentProperty.getType().getKind() === Kind.STRING || die();
  }
}

/**
 * Runs each test defined in the tests array above. Returns an array consisting
 * of {@code OK} or a failure message for each test.
 * 
 * @returns {Array}
 */
function runTests() {
  var results = [];
  for ( var key in tests) {
    if (!tests.hasOwnProperty(key)) {
      continue;
    }
    try {
      tests[key]();
      results.push("OK");
    } catch (e) {
      results.push(key + ": " + e.toString());
    }
  }
  return results;
}

/**
 * Utility method for test framework to inject location data.
 */
function die(message) {
  if (message) {
    throw message;
  } else {
    throw "Failed";
  }
}

var testData = {
  "comment" : "/**\n * This file is a schema that defines the jsonddl schema.\n * \u003cp\u003e\n * The var declaration at the top isn\u0027t actually important to the parser, but it\n * does make the schema a valid JavaScript program.\n */",
  "models" : {
    "EnumValue" : {
      "name" : "EnumValue",
      "properties" : [ {
        "name" : "comment",
        "type" : {
          "kind" : "STRING"
        }
      }, {
        "name" : "name",
        "type" : {
          "kind" : "STRING"
        }
      } ]
    },
    "Kind" : {
      "comment" : "/**\n   * This is a simplified type system, representing only the types of data that\n   * can be directly expressed in JSON.\n   */",
      "enumValues" : [ {
        "name" : "BOOLEAN"
      }, {
        "name" : "DDL"
      }, {
        "name" : "DOUBLE"
      }, {
        "name" : "ENUM"
      }, {
        "name" : "EXTERNAL"
      }, {
        "name" : "INTEGER"
      }, {
        "name" : "LIST"
      }, {
        "name" : "MAP"
      }, {
        "name" : "STRING"
      } ],
      "name" : "Kind"
    },
    "Model" : {
      "comment" : "/**\n   * A model represents a single kind of object within the schema. There is a\n   * 1:1 correspondence between Models and Java classes or JS type closures.\n   */",
      "name" : "Model",
      "properties" : [ {
        "name" : "comment",
        "type" : {
          "kind" : "STRING"
        }
      }, {
        "name" : "enumValues",
        "type" : {
          "kind" : "LIST",
          "listElement" : {
            "name" : "EnumValue",
            "kind" : "DDL"
          }
        }
      }, {
        "name" : "name",
        "type" : {
          "kind" : "STRING"
        }
      }, {
        "name" : "properties",
        "type" : {
          "kind" : "LIST",
          "listElement" : {
            "name" : "Property",
            "kind" : "DDL"
          }
        }
      } ]
    },
    "Property" : {
      "comment" : "/**\n   * A property is a pair of a name and a type.\n   */",
      "name" : "Property",
      "properties" : [
          {
            "comment" : "/**\n     * This comment will be stored in this property.\n     */",
            "name" : "comment",
            "type" : {
              "kind" : "STRING"
            }
          }, {
            "name" : "name",
            "type" : {
              "kind" : "STRING"
            }
          }, {
            "name" : "type",
            "type" : {
              "name" : "Type",
              "kind" : "DDL"
            }
          } ]
    },
    "Schema" : {
      "comment" : "/**\n   * A Schema is the top-level object that encapsulates the normalized form of a\n   * json-ddl schema.\n   */",
      "name" : "Schema",
      "properties" : [
          {
            "comment" : "/**\n     * General information about the schema itself.\n     */",
            "name" : "comment",
            "type" : {
              "kind" : "STRING"
            }
          },
          {
            "comment" : "/**\n     * A map of object models by simple name.\n     */",
            "name" : "models",
            "type" : {
              "kind" : "MAP",
              "mapKey" : {
                "kind" : "STRING"
              },
              "mapValue" : {
                "name" : "Model",
                "kind" : "DDL"
              }
            }
          } ]
    },
    "Type" : {
      "comment" : "/**\n   * A specific Kind and parameterization thereof.\n   */",
      "name" : "Type",
      "properties" : [ {
        "name" : "name",
        "type" : {
          "kind" : "STRING"
        }
      }, {
        "name" : "kind",
        "type" : {
          "name" : "Kind",
          "kind" : "ENUM"
        }
      }, {
        "name" : "listElement",
        "type" : {
          "name" : "Type",
          "kind" : "DDL"
        }
      }, {
        "name" : "mapKey",
        "type" : {
          "name" : "Type",
          "kind" : "DDL"
        }
      }, {
        "name" : "mapValue",
        "type" : {
          "name" : "Type",
          "kind" : "DDL"
        }
      } ]
    }
  }
};