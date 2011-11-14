package org.jsonddl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsonddl.JsonDdlVisitor.PropertyVisitor;

public class JsonParseVisitor implements PropertyVisitor, ContentHandler {

  private InputStream in;

  private Class<? extends JsonDdlObject<?>> clazz;

  @Override
  public boolean endArray() throws ParseException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void endJSON() throws ParseException, IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean endObject() throws ParseException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean endObjectEntry() throws ParseException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public <T> void endVisitProperty(T value, Context<T> ctx) {
    // TODO Auto-generated method stub

  }

  public void parse(InputStream in) throws IOException {
    JSONParser parser = new JSONParser();
    try {
      parser.parse(new InputStreamReader(in), this);
    } catch (ParseException e) {
      throw new IOException(e.getMessage());
    }
  }

  @Override
  public boolean primitive(Object value) throws ParseException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean startArray() throws ParseException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void startJSON() throws ParseException, IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean startObject() throws ParseException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean startObjectEntry(String key) throws ParseException, IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public <T> boolean visitProperty(T value, Context<T> ctx) {
    // TODO Auto-generated method stub
    return false;
  }

}
