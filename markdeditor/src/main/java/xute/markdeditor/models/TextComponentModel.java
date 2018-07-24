package xute.markdeditor.models;

import static xute.markdeditor.Styles.TextComponentStyle.NORMAL;

public class TextComponentModel extends BaseComponentModel {
  private int headingStyle = NORMAL;

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
