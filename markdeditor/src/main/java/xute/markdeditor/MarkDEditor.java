package xute.markdeditor;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import xute.markdeditor.components.TextComponent;
import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.models.TextComponentModel;
import xute.markdeditor.utilities.ComponentMetadataHelper;

import static xute.markdeditor.Styles.TextStyle.BLOCKQUOTE;

public class MarkDEditor extends MarkDCore implements TextComponent.TextComponentCallback {
  public static final String TAG = MarkDEditor.class.getSimpleName();
  private View _activeView;
  // index of activeView
  private int controlIndex;
  private Context mContext;
  private TextComponent __textComponent;

  public MarkDEditor(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    __textComponent = new TextComponent(context, this);
    addTextComponent(0);
  }

  /**
   * adds new TextComponent.
   *
   * @param insertIndex at which addition of new textcomponent take place.
   */
  private void addTextComponent(int insertIndex) {
    EditText editText = __textComponent.newEditTextComponent();
    //prepare tag
    TextComponentModel textComponentModel = new TextComponentModel();
    ComponentTag componentTag = ComponentMetadataHelper.getNewComponentTag(insertIndex);
    componentTag.setComponent(textComponentModel);
    editText.setTag(componentTag);
    Log.d(TAG, "Tag " + componentTag.toString());
    addView(editText, insertIndex);
    setFocus(editText);
  }

  /**
   * @param view to be focused on.
   */
  private void setFocus(View view) {
    controlIndex = ((ComponentTag) view.getTag()).getComponentIndex();
    _activeView = view;
    view.requestFocus();
  }

  @Override
  public void onInsertTextComponent(int selfIndex) {
    addTextComponent(selfIndex + 1);
    reComputeTagsAfter(selfIndex);
  }

  /**
   * re-compute the indexes of view after a view is inserted.
   *
   * @param startIndex index after which re-computation will be done.
   */
  private void reComputeTagsAfter(int startIndex) {
    View _child;
    for (int i = startIndex; i < getChildCount(); i++) {
      _child = getChildAt(i);
      ComponentTag componentTag = (ComponentTag) _child.getTag();
      componentTag.setComponentIndex(i);
      _child.setTag(componentTag);
    }
  }

  @Override
  public void onFocusGained(View view) {
    setFocus(view);
  }

  @Override
  public void onRemoveTextComponent(int selfIndex) {
    if (selfIndex == 0)
      return;
    View viewToBeRemoved = getChildAt(selfIndex);
    String content = ((EditText) viewToBeRemoved).getText().toString();
    removeViewAt(selfIndex);
    reComputeTagsAfter(selfIndex);
    View previousView = getChildAt(selfIndex - 1);
    int contentLen = ((EditText) previousView).getText().toString().length();
    ((EditText) previousView).append(content);
    setFocus(previousView, contentLen);
  }

  /**
   * @param view to be focused on.
   */
  private void setFocus(View view, int cursorPos) {
    controlIndex = ((ComponentTag) view.getTag()).getComponentIndex();
    _activeView = view;
    view.requestFocus();
    if (view instanceof EditText) {
      InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
      mgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
      //move cursor
      ((EditText) view).setSelection(cursorPos);
    }
  }

  /**
   * sets heading to text component
   *
   * @param heading number to be set
   */
  public void setHeading(int heading) {
    if (_activeView instanceof EditText) {
      ComponentTag componentTag = (ComponentTag) _activeView.getTag();
      ((TextComponentModel) componentTag.getComponent()).setHeadingStyle(heading);
      __textComponent.updateComponent(_activeView);
    }
  }

  /**
   * changes the current text into blockquote.
   */
  public void changeToBlockquote() {
    if (_activeView instanceof EditText) {
      ComponentTag componentTag = (ComponentTag) _activeView.getTag();
      ((TextComponentModel) componentTag.getComponent()).setHeadingStyle(BLOCKQUOTE);
      __textComponent.updateComponent(_activeView);
    }
  }

  /**
   * adds link.
   *
   * @param text link text
   * @param url  linking url.
   */
  public void addLink(String text, String url) {
    if (_activeView instanceof EditText) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder
       .append(" <a href=\"")
       .append(url)
       .append("\">")
       .append(text)
       .append("</a> ");
      ((EditText) _activeView).append(stringBuilder.toString());
    }
  }
}
