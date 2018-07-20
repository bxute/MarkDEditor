package xute.markdeditor.models;

public class BulletGroupModel {
  private int orderType;
  private int startIndex;
  private int endIndex;

  public int getOrderType() {
    return orderType;
  }

  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }

  public int getEndIndex() {
    return endIndex;
  }

  public void setEndIndex(int endIndex) {
    this.endIndex = endIndex;
  }

  @Override
  public String toString() {
    return "BulletGroupModel{" +
     "orderType=" + orderType +
     ", startIndex=" + startIndex +
     ", endIndex=" + endIndex +
     '}';
  }
}
