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
package org.jsonddl.generator;

import org.mozilla.javascript.ast.AstNode;

public class UnexpectedNodeException extends RuntimeException {
  private static final long serialVersionUID = 796217485028764302L;

  public UnexpectedNodeException(AstNode node) {
    super("Unexpected node " + node.getClass().getSimpleName() + " at line " + node.getLineno());
  }

  public UnexpectedNodeException(AstNode node, String message) {
    super(message + " at line " + node.getLineno());
  }
}