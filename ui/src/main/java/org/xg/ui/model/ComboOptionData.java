package org.xg.ui.model;

public class ComboOptionData {
  private final String name;

  public Integer getCode() {
    return code;
  }

  private final Integer code;

  public ComboOptionData(Integer code, String name) {
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
    if (obj instanceof ComboOptionData) {
      ComboOptionData ut2 = (ComboOptionData)obj;
      return ut2.code.equals(code);
    }
    return false;
  }

  @Override
  public String toString() {
    return getName();
  }
}
