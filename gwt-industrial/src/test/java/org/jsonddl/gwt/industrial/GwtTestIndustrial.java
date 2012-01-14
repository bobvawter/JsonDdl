package org.jsonddl.gwt.industrial;

import org.jsonddl.industrial.IndustrialTest;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestIndustrial extends GWTTestCase {

  private IndustrialTest realTest;

  @Override
  public String getModuleName() {
    return "org.jsonddl.JsonDdlTest";
  }

  @Override
  public void gwtSetUp() {
    realTest = new IndustrialTest();
  }

  public void testCollections() {
    realTest.testCollections();
  }

  public void testExtension() {
    realTest.testExtension();
  }

  public void testObjectMethods() {
    realTest.testObjectMethods();
  }

  public void testToJsonObject() {
    realTest.testToJsonObject();
  }
}
