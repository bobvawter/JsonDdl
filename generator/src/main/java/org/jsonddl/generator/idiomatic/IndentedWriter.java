package org.jsonddl.generator.idiomatic;

import java.io.PrintWriter;
import java.io.Writer;

public class IndentedWriter extends PrintWriter {
  private boolean shouldIndent;
  private int level;

  public IndentedWriter(Writer sink) {
    super(sink);
  }

  public void indent() {
    level++;
  }

  public void indentln(String format, Object... args) {
    indent();
    println(format, args);
    outdent();
  }

  public void outdent() {
    if (level == 0) {
      throw new IllegalStateException("Attempting to decrease indent below 0");
    }
    level--;
  }

  @Override
  public void println() {
    super.println();
    shouldIndent = true;
  }

  public void println(String format, Object... args) {
    format(format, args);
    println();
  }

  @Override
  public void write(char[] buf, int off, int len) {
    maybeIndent();
    super.write(buf, off, len);
  }

  @Override
  public void write(int c) {
    maybeIndent();
    super.write(c);
  }

  @Override
  public void write(String s) {
    maybeIndent();
    super.write(s);
  }

  @Override
  public void write(String s, int off, int len) {
    maybeIndent();
    super.write(s, off, len);
  }

  private void maybeIndent() {
    if (shouldIndent) {
      shouldIndent = false;
      for (int i = 0; i < level; i++) {
        print("  ");
      }
    }
  }
}
