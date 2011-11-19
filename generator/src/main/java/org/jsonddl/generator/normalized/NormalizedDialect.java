package org.jsonddl.generator.normalized;

import java.io.IOException;
import java.io.OutputStream;

import org.jsonddl.generator.Dialect;
import org.jsonddl.model.Schema;

import com.google.gson.Gson;

public class NormalizedDialect implements Dialect {

  @Override
  public void generate(String packageName, Collector output, Schema s) throws IOException {
    OutputStream out = output.writeResource(packageName.replace('.', '/') + "/schema.js");
    out.write(new Gson().toJson(s).getBytes("UTF8"));
    out.close();
  }

  @Override
  public String getName() {
    return "normalized";
  }
}
