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
var schema = {
  Example : {
    aBoolean : true,
    'int' : 0,
    'double' : 0.1,
    aString : "",
    anExample : "Example",
    anExampleList : [ "Example" ],
    anIntegralList : [ 0 ],
    aStringToListOfBooleanMap : {
      "" : [ true ]
    }
  },
  Extended : {
    string : "",
    'industrial:extends' : 'Base.Impl',
    'industrial:implements' : 'Base'
  }
};