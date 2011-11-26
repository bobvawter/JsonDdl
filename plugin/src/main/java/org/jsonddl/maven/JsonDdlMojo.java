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
package org.jsonddl.maven;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jsonddl.generator.Dialect;
import org.jsonddl.generator.Generator;
import org.jsonddl.generator.Options;

/**
 * Runs the JsonDdl code generator.
 * 
 * @goal generate-sources
 * @phase generate-sources
 */
public class JsonDdlMojo extends AbstractMojo {
  /**
   * The names of the generator dialects to invoke. If left unspecified, the "industrial" and
   * "normalized" dialects will be executed.
   * 
   * @parameter
   */
  private String[] dialects = { "industrial", "normalized" };

  /**
   * Extra options to provide to the generator dialects.
   * 
   * @parameter
   */
  private Map<String, String> extraOptions;

  /**
   * Specific schema files to generate. The format is
   * {@code <com.example.packageName>path/to/scema.js</com.example.packageName>}. If not specified,
   * any {@code *.js} files in {@code src/[main,test]/jsonddl} will be compiled and the basename of
   * the file will be treated as the package name.
   * 
   * @parameter
   */
  private Map<String, String> schemas;

  /**
   * The destination directory for the generated sources.
   * 
   * @parameter default-value="${project.build.directory}/generated-sources"
   */
  private File outputDirectory;

  /**
   * @parameter default-value="${project}"
   * @readonly
   * @required
   */
  private MavenProject project;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (isTest()) {
      if (schemas == null || schemas.isEmpty()) {
        schemas = findFiles("src/test/jsonddl");
      }
      outputDirectory = new File(outputDirectory, "test-jsonddl");
      project.addTestCompileSourceRoot(outputDirectory.getAbsolutePath());
    } else {
      if (schemas == null || schemas.isEmpty()) {
        schemas = findFiles("src/main/jsonddl");
      }
      outputDirectory = new File(outputDirectory, "jsonddl");
      project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    }
    Options options = new Options.Builder()
        .withDialects(Arrays.asList(dialects))
        .withExtraOptions(extraOptions)
        .build();
    for (Map.Entry<String, String> schema : schemas.entrySet()) {
      try {
        File schemaFile = new File(project.getBasedir(), schema.getValue());
        if (!schemaFile.canRead()) {
          throw new MojoExecutionException("Cannot find schema file " + schemaFile.getPath());
        }
        boolean success = new Generator().generate(
            new FileInputStream(schemaFile),
            options.builder().withPackageName(schema.getKey()).build(),
            new Dialect.Collector() {
              @Override
              public void println(String message) {
                getLog().info(message);
              }

              @Override
              public void println(String format, Object... args) {
                println(String.format(format, args));
              }

              @Override
              public OutputStream writeJavaSource(String packageName, String simpleName)
                  throws IOException {
                File dir = new File(outputDirectory, packageName.replace('.', File.separatorChar));
                dir.mkdirs();
                File f = new File(dir, simpleName + ".java");
                return new FileOutputStream(f);
              }

              @Override
              public OutputStream writeResource(String path) throws IOException {
                File file = new File(outputDirectory, path);
                file.getParentFile().mkdirs();
                return new FileOutputStream(file);
              }

            });
        if (!success) {
          throw new MojoFailureException("Code generator did not complete normally");
        }
      } catch (IOException e) {
        throw new MojoExecutionException("Could not generate schema", e);
      }
    }
  }

  protected boolean isTest() {
    return false;
  }

  private Map<String, String> findFiles(String lookIn) {
    File[] files = new File(project.getBasedir(), lookIn).listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.getPath().endsWith(".js");
      }
    });
    Map<String, String> toReturn = new LinkedHashMap<String, String>();
    for (File f : files) {
      String localPackage = f.getName();
      localPackage = localPackage.substring(0, localPackage.length() - 3);
      toReturn.put(localPackage, f.getPath().substring(project.getBasedir().getPath().length()));
      getLog().debug("Mapped file " + f.getPath() + " to package " + localPackage);
    }
    return toReturn;
  }
}
