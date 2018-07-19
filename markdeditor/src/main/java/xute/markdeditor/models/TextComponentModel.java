package xute.markdeditor.models;

public class TextComponentModel extends BaseComponentModel {
  private int headingStyle = -1;

  public int getHeadingStyle() {
    return headingStyle;
  }

  public void setHeadingStyle(int headingStyle) {
    this.headingStyle = headingStyle;
  }

  @Override
  public String toString() {
    return "TextComponentModel{" +
     "headingStyle=" + headingStyle +
     '}';
  }
}
