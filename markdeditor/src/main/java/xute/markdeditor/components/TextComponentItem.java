package xute.markdeditor.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import xute.markdeditor.R;

public class TextComponentItem extends FrameLayout {
  public static final int MODE_PLAIN = 0;
  public static final int MODE_UL = 1;
  public static final int MODE_OL = 2;
  TextView indicator;
  EditText text;
  private int mEditorMode;

  public TextComponentItem(@NonNull Context context) {
    super(context);
    init(context, MODE_PLAIN);
  }

  private void init(Context context, int mode) {
    View view = LayoutInflater.from(context).inflate(R.layout.text_component_item, this);
    indicator = view.findViewById(R.id.indicator);
    text = view.findViewById(R.id.text);
    this.mEditorMode = mode;
    if (mode == MODE_PLAIN) {
      indicator.setVisibility(GONE);
    } else {
      indicator.setVisibility(VISIBLE);
    }
  }

  public TextComponentItem(@NonNull Context context, int mode) {
    super(context);
    init(context, mode);
  }

  public TextComponentItem(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, MODE_PLAIN);
  }

  public void setIndicator(String bullet) {
    indicator.setText(bullet);
  }

  public void setText(String content) {
    text.setText(content);
  }

  public EditText getInputBox() {
    return text;
  }

  public int getMode() {
    return mEditorMode;
  }

  public void setMode(int mode) {
    this.mEditorMode = mode;
    if (mode == MODE_PLAIN) {
      indicator.setVisibility(GONE);
    } else {
      indicator.setVisibility(VISIBLE);
    }
  }
}
