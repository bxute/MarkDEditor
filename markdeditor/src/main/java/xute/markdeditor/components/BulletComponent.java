package xute.markdeditor.components;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class BulletComponent {

  public static final String DOT = "\\u2022";
  private final Context mContext;
  private ArrayList<String> strings;

  public BulletComponent(Context context) {
    this.mContext = context;
    strings = new ArrayList<>();
  }

  public TextComponentItem getNewBulletComponent(String text) {
    TextComponentItem bulletItemView = new TextComponentItem(mContext);
    bulletItemView.setIndicator(DOT);
    bulletItemView.setText(text);
    strings.add(text);
    //set listeners
    final EditText et = bulletItemView.getInputBox();
    et.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
          return true;
        if (keyCode == KeyEvent.KEYCODE_DEL) {
//          if (_textComponentCallback != null) {
//            _textComponentCallback.onRemoveTextComponent(((ComponentTag) view.getTag()).getComponentIndex());
//          }
        }
        return false;
      }
    });

    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean inFocus) {
        if (inFocus) {
//          if (_textComponentCallback != null) {
//            _textComponentCallback.onFocusGained(view);
//          }
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
//            if (_textComponentCallback != null) {
//              _textComponentCallback.onInsertTextComponent(((ComponentTag) et.getTag()).getComponentIndex());
//            }
          } else {
            et.setText(Html.fromHtml(charSequence.toString()));
          }
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
    return bulletItemView;
  }

}
