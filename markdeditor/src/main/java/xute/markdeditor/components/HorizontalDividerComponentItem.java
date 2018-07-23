package xute.markdeditor.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import xute.markdeditor.R;

public class HorizontalDividerComponentItem extends FrameLayout {
  public HorizontalDividerComponentItem(@NonNull Context context) {
    super(context);
    init(context);
  }

  public HorizontalDividerComponentItem(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context){
    LayoutInflater.from(context).inflate(R.layout.horizontal_divider_item_view,this);
  }
}
