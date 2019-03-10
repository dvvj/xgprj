package org.xg.ui.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UserType {
  private final String name;

  public Integer getCode() {
    return code;
  }

  private final Integer code;

  public UserType(Integer code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return code;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof UserType) {
      UserType ut2 = (UserType)obj;
      return ut2.code.equals(code);
    }
    return false;
  }

  @Override
  public String toString() {
    return getName();
  }
}
