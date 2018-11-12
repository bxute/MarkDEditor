package xute.markdeditor.datatype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DraftDataItemModel {
  @Expose
  @SerializedName("itemType")
  private int itemType;
  @Expose
  @SerializedName("mode")
  private int mode;
  @Expose
  @SerializedName("style")
  private int style;
  @Expose
  @SerializedName("content")
  private String content;
  @Expose
  @SerializedName("downloadUrl")
  private String downloadUrl;
  @Expose
  @SerializedName("caption")
  private String caption;

  public DraftDataItemModel() {
  }

  public int getItemType() {
    return itemType;
  }

  public void setItemType(int itemType) {
    this.itemType = itemType;
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public int getStyle() {
    return style;
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  @Override
  public String toString() {
    return "DraftDataItemModel{" +
     "itemType='" + itemType + '\'' +
     ", mode=" + mode +
     ", style=" + style +
     ", content='" + content + '\'' +
     ", downloadUrl='" + downloadUrl + '\'' +
     ", caption='" + caption + '\'' +
     '}';
  }
}
