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
import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.models.TextComponentModel;

public class TextComponentItem extends FrameLayout {
  public static final int MODE_PLAIN = 0;
  public static final int MODE_UL = 1;
  public static final int MODE_OL = 2;

  public static final String UL_BULLET = "\u25CF";
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
    setMode(mode);
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

  public int getTextHeadingStyle() {
    ComponentTag componentTag = (ComponentTag) getTag();
    //check heading
    return ((TextComponentModel) componentTag.getComponent()).getHeadingStyle();
  }

  public void setMode(int mode) {
    this.mEditorMode = mode;
    if (mode == MODE_PLAIN) {
      indicator.setVisibility(GONE);
      text.setBackgroundResource(R.drawable.text_input_bg);
    } else if (mode == MODE_UL) {
      indicator.setText(UL_BULLET);
      indicator.setVisibility(VISIBLE);
      text.setBackgroundResource(R.drawable.text_input_bg);
    } else if (mode == MODE_OL) {
      indicator.setVisibility(VISIBLE);
      text.setBackgroundResource(R.drawable.text_input_bg);
    }
  }
}
