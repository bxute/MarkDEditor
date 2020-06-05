package xute.markdeditor.utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class ImageHelper {
  public static void load(Context context,final ImageView imageView, String _uri) {
    try {
      RequestOptions options = new RequestOptions()
       .fitCenter()
       .diskCacheStrategy(DiskCacheStrategy.NONE)
       .priority(Priority.HIGH);
      Glide.with(context)
       .load(_uri)
       .apply(options)
       .listener(new RequestListener<Drawable>() {
         @Override
         public boolean onLoadFailed(@Nullable GlideException e,
                                     Object model,
                                     Target<Drawable> target,
                                     boolean isFirstResource) {
           return false;
         }

         @Override
         public boolean onResourceReady(Drawable resource,
                                        Object model,
                                        Target<Drawable> target,
                                        DataSource dataSource,
                                        boolean isFirstResource) {
           int width = imageView.getMeasuredWidth();
           int targetHeight = width * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
           if (imageView.getLayoutParams().height != targetHeight) {
             imageView.getLayoutParams().height = targetHeight;
             imageView.requestLayout();
           }
           imageView.setImageDrawable(resource);
           return true;
         }
       })
       .into(imageView);
    }
    catch (Exception e) {

    }
  }
}
