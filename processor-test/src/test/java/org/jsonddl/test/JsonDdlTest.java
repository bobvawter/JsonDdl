package org.jsonddl.test;

import org.jsonddl.JsonDdlObject.Builder;
import org.junit.Test;

/**
 * This just ensures that the annotation processor actually ran.
 */
public class JsonDdlTest {
  @Test
  public void test() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Class<?> clazz = Class.forName("org.jsonddl.test.Example$Builder");
    Builder<?> b = Builder.class.cast(clazz.newInstance());
    b.build();
  }
}
