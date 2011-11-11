package org.jsonddl.test;

import org.junit.Test;

/**
 * This just ensures that the maven processor runs at the right time.
 */
public class JsonDdlTest {
  @Test
  public void test() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    new Example.Builder().build();
  }
}
