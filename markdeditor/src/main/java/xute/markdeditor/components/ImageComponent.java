package xute.markdeditor.components;

import android.content.Context;
import android.graphics.Bitmap;

public class ImageComponent {
  private Context context;

  public ImageComponent(Context context) {
    this.context = context;
  }

  public ImageComponentItem getNewImageComponentItem(String filePath, ImageComponentItem.ImageRemoveListener imageRemoveListener) {
    ImageComponentItem imageComponentItem = new ImageComponentItem(context);
    imageComponentItem.setImageRemoveListener(imageRemoveListener);
    imageComponentItem.setFilePath(filePath);
    return imageComponentItem;
  }
}
