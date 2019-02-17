package org.xg;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

  private final static ObjectMapper ObjMapper = new ObjectMapper();

  public static <T> T readObj(String json, Class<T> clz) {
    try {
      return ObjMapper.readValue(json, clz);
    }
    catch (Exception ex) {
      throw new RuntimeException("Error deserializing json", ex);
    }

  }
}
