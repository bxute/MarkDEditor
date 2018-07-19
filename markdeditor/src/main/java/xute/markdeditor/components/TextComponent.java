package xute.markdeditor.components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.models.TextComponentModel;
import xute.markdeditor.utilities.FontSize;

public class TextComponent {

  private final Context mContext;
  private TextComponentCallback _textComponentCallback;
  private Resources r;

  public TextComponent(Context context, TextComponentCallback textComponentCallback) {
    this.mContext = context;
    r = mContext.getResources();
    _textComponentCallback = textComponentCallback;
  }

  public EditText newEditTextComponent() {
    final EditText et = new EditText(mContext);
    et.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
          return true;
        if (keyCode == KeyEvent.KEYCODE_DEL) {
          if (_textComponentCallback != null) {
            _textComponentCallback.onRemoveTextComponent(((ComponentTag) view.getTag()).getComponentIndex());
          }
        }
        return false;
      }
    });

    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean inFocus) {
        if (inFocus) {
          if (_textComponentCallback != null) {
            _textComponentCallback.onFocusGained(view);
          }
        }
      }
    });

    et.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (before < count) {
          char ch = charSequence.charAt(charSequence.length() - 1);
          if (ch == '\n') {
            et.setText(charSequence.subSequence(0, charSequence.length() - 1));
            if (_textComponentCallback != null) {
              _textComponentCallback.onInsertTextComponent(((ComponentTag) et.getTag()).getComponentIndex());
            }
          }
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
    return et;
  }

  public void updateComponent(View view) {
    ComponentTag componentTag = (ComponentTag) view.getTag();
    //check heading
    int heading = ((TextComponentModel) componentTag.getComponent()).getHeadingStyle();
    ((EditText) view).setTextSize(FontSize.getFontSize(heading));
    if (heading != -1) {
      ((EditText) view).setTypeface(null, Typeface.BOLD);
    } else {
      ((EditText) view).setTypeface(null, Typeface.NORMAL);
    }
  }

  public interface TextComponentCallback {
    void onInsertTextComponent(int selfIndex);

    void onFocusGained(View view);

    void onRemoveTextComponent(int selfIndex);
  }
}
