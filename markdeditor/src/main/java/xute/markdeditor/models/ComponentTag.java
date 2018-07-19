package xute.markdeditor.models;

public class ComponentTag {
  private int componentIndex;
  private BaseComponentModel baseComponent;

  public BaseComponentModel getComponent() {
    return baseComponent;
  }

  public void setComponent(BaseComponentModel baseComponent) {
    this.baseComponent = baseComponent;
  }

  public int getComponentIndex() {
    return componentIndex;
  }

  public void setComponentIndex(int componentIndex) {
    this.componentIndex = componentIndex;
  }

  @Override
  public String toString() {
    return "ComponentTag{" +
     "componentIndex=" + componentIndex +
     ", baseComponent=" + baseComponent +
     '}';
  }
}
