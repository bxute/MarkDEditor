package xute.markdeditor.models;

public class ImageComponentModel extends BaseComponentModel {
  private String url;
  private String caption;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  @Override
  public String toString() {
    return "ImageComponentModel{" +
     "url='" + url + '\'' +
     ", caption='" + caption + '\'' +
     '}';
  }
}
