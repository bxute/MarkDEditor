package xute.markdeditor.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class FileUploadReponse {

  @Expose
  @SerializedName("error")
  public String error;
  @Expose
  @SerializedName("url")
  public String url;

  public FileUploadReponse(String error, String url) {
    this.error = error;
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
