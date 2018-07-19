package xute.markdeditor.models;

public class BaseComponentModel {
  private String componentType;
  private int componentIndex;

  public String getComponentType() {
    return componentType;
  }

  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }

  @Override
  public String toString() {
    return "BaseComponentModel{" +
     "componentType='" + componentType + '\'' +
     ", componentIndex=" + componentIndex +
     '}';
  }
}
