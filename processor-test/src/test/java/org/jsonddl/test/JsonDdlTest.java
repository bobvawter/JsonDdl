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

  @Test
  public void testPojo() throws ClassNotFoundException, InstantiationException,
      IllegalAccessException {
    Class<?> clazz = Class.forName("org.jsonddl.test.pojo.Example");
    clazz.newInstance();
  }
}
