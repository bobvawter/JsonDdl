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
package org.jsonddl.generator.backbone;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.jsonddl.generator.Dialect.Collector;
import org.jsonddl.generator.Generator;
import org.jsonddl.generator.Options;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class BackboneDialectTest {

  /**
   * Vends instances of {@link JsOutputStream} to the {@link Generator}.
   */
  private class JsCollector implements Collector {
    @Override
    public void println(String message) {}

    @Override
    public void println(String format, Object... args) {}

    @Override
    public Writer writeJavaSource(String packageName, String simpleName) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public OutputStream writeResource(String path) throws IOException {
      return new JsOutputStream(path);
    }
  }

  /**
   * Buffers a JS file and injects it into {@link BackboneDialectTest#cx}.
   */
  private class JsOutputStream extends ByteArrayOutputStream {
    private final String path;

    public JsOutputStream(String path) {
      this.path = path;
    }

    @Override
    public void close() throws IOException {
      super.close();
      String source = new String(toByteArray());
      System.out.println(path);
      System.out.println(source);
      cx.evaluateString(scope, source, path, 1, null);
    }
  }

  private static final Charset UTF8 = Charset.forName("UTF-8");
  private Context cx;
  private ScriptableObject scope;

  @After
  public void after() {
    Context.exit();
  }

  @Before
  public void before() throws IOException {
    cx = Context.enter();
    scope = cx.initStandardObjects();

    for (String resource : Arrays.asList("underscore-min.js", "backbone-min.js")) {
      evaluateResource(resource);
    }
  }

  /**
   * Smoke test for script injection in setup.
   */
  @Test
  public void test() {
    Object obj = cx.evaluateString(scope, "Backbone", "test", 0, null);
    assertNotNull(obj);
  }

  @Test
  public void testDialectParse() throws IOException {
    InputStream in = getClass().getResourceAsStream("../test-schema.js");
    Collector output = new JsCollector();
    Options options = new Options.Builder()
        .addDialects("backbone")
        .withNormalizedInput(true)
        .build();

    Generator gen = new Generator();
    assertTrue(gen.generate(in, options, output));

    // Verify that the Schema type was correctly defined
    Object schema = cx.evaluateString(scope, "Schema", "test", 0, null);
    assertNotNull(schema);

    // Inject test code
    evaluateResource("test.js");

    Object results = cx.evaluateString(scope, "runTests()", "test", 0, null);
    Scriptable array = Context.toObject(results, scope);
    int length = (int) Context.toNumber(array.get("length", array));
    assertEquals("Unexpected number of tests run", 2, length);
    if (length > 0) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < length; i++) {
        String string = Context.toString(array.get(i, array));
        if (!"OK".equals(string)) {
          sb.append(string).append("\n");
        }
      }
      if (sb.length() != 0) {
        fail(sb.toString());
      }
    }
  }

  private Object evaluateResource(String resource) throws IOException {
    Reader in = new InputStreamReader(getClass().getResourceAsStream(resource), UTF8);
    try {
      return cx.evaluateReader(scope, in, resource, 1, null);
    } finally {
      in.close();
    }
  }
}
