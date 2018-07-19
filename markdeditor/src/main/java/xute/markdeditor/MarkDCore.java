package xute.markdeditor;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MarkDCore extends LinearLayout {
  public MarkDCore(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    this.setOrientation(VERTICAL);
  }
}
