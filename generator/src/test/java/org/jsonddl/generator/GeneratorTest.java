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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import org.jsonddl.model.Schema;
import org.junit.Test;

/**
 * Integration tests for the generator frontend.
 */
public class GeneratorTest {
  static class NullCollector implements Dialect.Collector {
    final Map<String, ByteArrayOutputStream> resources = new TreeMap<String, ByteArrayOutputStream>();
    final Map<String, ByteArrayOutputStream> sources = new TreeMap<String, ByteArrayOutputStream>();

    @Override
    public void println(String message) {}

    @Override
    public void println(String format, Object... args) {}

    @Override
    public Writer writeJavaSource(String packageName, String simpleName) throws IOException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      sources.put(packageName + "::" + simpleName, out);
      return new OutputStreamWriter(out, SOURCE_CHARSET);
    }

    @Override
    public OutputStream writeResource(String path) throws IOException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      resources.put(path, out);
      return out;
    }
  }

  @Test
  public void testReparseNormalized() throws IOException {
    InputStream res = getClass().getResourceAsStream("test-schema.js");
    Schema s = new Generator().parseNormalized(res);
    assertEquals(6, s.getModels().size());
  }
}
