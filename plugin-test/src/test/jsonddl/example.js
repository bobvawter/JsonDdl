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
/*
 * The DDL parser is a loose superset of JSON, ignoring comments and any leading var declaration.
 */
var a = {
    // Top-level map keys are taken as simple type names
    "Example" : {
	// boolean fields are declared with either true or false as the value
	"aBoolean" : true,
	// Any integral value results in an int field
	"anInt" : 0,
	// Non-integral numbers result in double fields
	"aDouble" : 0.1,
	// String fields may be declared using an empty string
	"aString" : "",
	// Object references are by local name
	"anExample" : "Example",
	// Lists are declared with their constituent type as the single element
	"anExampleList" : [ "Example" ],
	"anIntegralList" : [ 0 ],
	// Maps are declared as key : value
	"aStringToListOfBooleanMap" : {
	    "" : [ true ]
	}
    },
    /*
     * It's possible to declare types that use arbitrary java types in their
     * properties. These Java-specific types aren't guaranteed to be portable
     * across runtimes.
     */
    "UsesJavaTypes" : {
	// Java types can use types in the implicit namespace
	"anInteger" : "Integer",
	// Or they can use fully-qualified type names
	"aUUID" : "java.util.UUID",
	// Primitive types are declared using the type's boxed form
	"aLong" : "Long"
    }
}