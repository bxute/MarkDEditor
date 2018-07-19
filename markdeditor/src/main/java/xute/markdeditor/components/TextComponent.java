package xute.markdeditor.components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import xute.markdeditor.R;
import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.models.TextComponentModel;
import xute.markdeditor.utilities.FontSize;

import static xute.markdeditor.Styles.TextStyle.BLOCKQUOTE;
import static xute.markdeditor.Styles.TextStyle.H1;
import static xute.markdeditor.Styles.TextStyle.H5;
import static xute.markdeditor.Styles.TextStyle.NO_HEADING;

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
    et.setBackgroundResource(R.drawable.text_input_bg);
    et.setPadding(
     getPxFromSp(8),
     getPxFromSp(4),
     getPxFromSp(8),
     getPxFromSp(4)
    );
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
          } else {
            et.setText(Html.fromHtml(charSequence.toString()));
          }
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
    return et;
  }

  private int getPxFromSp(int sp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
  }

  public void updateComponent(View view) {
    ComponentTag componentTag = (ComponentTag) view.getTag();
    //check heading
    int style = ((TextComponentModel) componentTag.getComponent()).getHeadingStyle();
    ((EditText) view).setTextSize(FontSize.getFontSize(style));
    if (style >= H1 && style <= H5) {
      ((EditText) view).setTypeface(null, Typeface.BOLD);
      (view).setBackgroundResource(R.drawable.text_input_bg);
    }

    if (style == NO_HEADING) {
      ((EditText) view).setTypeface(null, Typeface.NORMAL);
      (view).setBackgroundResource(R.drawable.text_input_bg);
    }

    if (style == BLOCKQUOTE) {
      ((EditText) view).setTypeface(null, Typeface.NORMAL);
      (view).setBackgroundResource(R.drawable.blockquote_component_bg);
    }
  }

  public interface TextComponentCallback {
    void onInsertTextComponent(int selfIndex);

    void onFocusGained(View view);

    void onRemoveTextComponent(int selfIndex);
  }
}
