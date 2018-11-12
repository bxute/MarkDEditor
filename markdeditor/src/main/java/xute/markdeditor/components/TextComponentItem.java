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

  public static final String UL_BULLET = "\u2022";
  TextView indicatorTv;
  EditText editText;
  private int mEditorMode;
  private String indicatorText;

  public TextComponentItem(@NonNull Context context) {
    super(context);
    init(context, MODE_PLAIN);
  }

  private void init(Context context, int mode) {
    View view = LayoutInflater.from(context).inflate(R.layout.text_component_item, this);
    indicatorTv = view.findViewById(R.id.indicator);
    editText = view.findViewById(R.id.text);
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

  public void setHintText(String hint) {
    editText.setHint(hint);
  }

  public void setText(String content) {
    editText.setText(content);
  }

  public EditText getInputBox() {
    return editText;
  }

  public int getMode() {
    return mEditorMode;
  }

  public void setMode(int mode) {
    this.mEditorMode = mode;
    if (mode == MODE_PLAIN) {
      indicatorTv.setVisibility(GONE);
      editText.setBackgroundResource(R.drawable.text_input_bg);
    } else if (mode == MODE_UL) {
      indicatorTv.setText(UL_BULLET);
      indicatorTv.setVisibility(VISIBLE);
      editText.setBackgroundResource(R.drawable.text_input_bg);
    } else if (mode == MODE_OL) {
      indicatorTv.setVisibility(VISIBLE);
      editText.setBackgroundResource(R.drawable.text_input_bg);
    }
  }

  public int getTextHeadingStyle() {
    ComponentTag componentTag = (ComponentTag) getTag();
    //check heading
    return ((TextComponentModel) componentTag.getComponent()).getHeadingStyle();
  }

  public void setIndicator(String bullet) {
    indicatorTv.setText(bullet);
    this.indicatorText = bullet;
  }

  public String getIndicatorText() {
    return indicatorText;
  }

  public String getContent() {
    return editText.getText().toString();
  }
}
