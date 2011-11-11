package org.jsonddl.maven;

/**
 * Runs the JsonDdl code generator for test output code.
 * 
 * @goal generate-test-sources
 * @phase generate-test-sources
 */
public class JsonDdlTestMojo extends JsonDdlMojo {

  @Override
  protected boolean isTest() {
    return true;
  }

}
