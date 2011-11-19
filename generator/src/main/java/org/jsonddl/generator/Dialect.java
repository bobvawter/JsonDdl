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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ServiceLoader;

import org.jsonddl.model.Schema;

/**
 * Additional dialects can be added to the generator by providing implementations of this type that
 * conform to the {@link ServiceLoader} spec.
 */
public interface Dialect {
  /**
   * Instances of a Collector are provided to the Dialect implementation.
   */
  public interface Collector {
    /**
     * Write an informational message to be displayed in the runtime environment.
     */
    void println(String message);

    /**
     * Write a printf-style informational message to be displayed in the runtime environment.
     */
    void println(String format, Object... args);

    /**
     * Create an OutputStream that collects source code to be compiled.
     * 
     * @param packageName the dotted package name (e.g. {@code org.jsonddl}) of the type being
     *          created
     * @param simpleName the simple name (e.g. {@code Foo}) of the top-level type being created
     * @return an OutputStream that collects the contents for the new type. The caller must call
     *         {@link OutputStream#close()} to ensure that the contents of the file are committed.
     */
    OutputStream writeJavaSource(String packageName, String simpleName) throws IOException;

    /**
     * Create an OutputStream that collects the contents of additional resources.
     * 
     * @param path the location at which the the contents of the resource should be made available.
     * @return an OutputStream that collects the contents of the resource. The caller must call
     *         {@link OutputStream#close()} to ensure that the contents of the file are committed.
     */
    OutputStream writeResource(String path) throws IOException;
  }

  void generate(String packageName, Dialect.Collector output, Schema s) throws IOException;

  String getName();
}
