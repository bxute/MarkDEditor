package xute.markdeditor;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import xute.markdeditor.components.TextComponent;
import xute.markdeditor.components.TextComponentItem;
import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.models.TextComponentModel;
import xute.markdeditor.utilities.ComponentMetadataHelper;

import static xute.markdeditor.Styles.RowType.BLOCKQUOTE;
import static xute.markdeditor.Styles.RowType.NORMAL;
import static xute.markdeditor.components.TextComponentItem.MODE_PLAIN;
import static xute.markdeditor.components.TextComponentItem.MODE_UL;

public class MarkDEditor extends MarkDCore implements TextComponent.TextComponentCallback {
  public static final String TAG = MarkDEditor.class.getSimpleName();
  private View _activeView;
  // index of activeView
  private int controlIndex;
  private Context mContext;
  private TextComponent __textComponent;
  private int currentInputMode;

  public MarkDEditor(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    currentInputMode = MODE_PLAIN;
    __textComponent = new TextComponent(context, this);
    addTextComponent(0);
  }

  /**
   * adds new TextComponent.
   *
   * @param insertIndex at which addition of new textcomponent take place.
   */
  private void addTextComponent(int insertIndex) {
    TextComponentItem textComponentItem = __textComponent.newTextComponent(currentInputMode);
    //prepare tag
    TextComponentModel textComponentModel = new TextComponentModel();
    ComponentTag componentTag = ComponentMetadataHelper.getNewComponentTag(insertIndex);
    componentTag.setComponent(textComponentModel);
    textComponentItem.setTag(componentTag);
    addView(textComponentItem, insertIndex);
    setFocus(textComponentItem);
  }

  /**
   * @param view to be focused on.
   */
  private void setFocus(View view) {
    controlIndex = ((ComponentTag) view.getTag()).getComponentIndex();
    _activeView = view;
    currentInputMode = ((TextComponentItem) _activeView).getMode();
    ((TextComponentItem) view).getInputBox().requestFocus();
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
    String content = ((TextComponentItem) viewToBeRemoved).getInputBox().getText().toString();
    removeViewAt(selfIndex);
    reComputeTagsAfter(selfIndex);
    View previousView = getChildAt(selfIndex - 1);
    int contentLen = ((TextComponentItem) previousView).getInputBox().getText().toString().length();
    ((TextComponentItem) previousView).getInputBox().append(String.format(" %s", content));
    setFocus(previousView, contentLen);
  }

  /**
   * @param view to be focused on.
   */
  private void setFocus(View view, int cursorPos) {
    controlIndex = ((ComponentTag) view.getTag()).getComponentIndex();
    _activeView = view;
    view.requestFocus();
    if (view instanceof TextComponentItem) {
      InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
      mgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
      //move cursor
      ((TextComponentItem) view).getInputBox().setSelection(cursorPos);
    }
  }

  /**
   * sets heading to text component
   *
   * @param heading number to be set
   */
  public void setHeading(int heading) {
    currentInputMode = MODE_PLAIN;
    if (_activeView instanceof TextComponentItem) {
      ((TextComponentItem) _activeView).setMode(currentInputMode);
      ComponentTag componentTag = (ComponentTag) _activeView.getTag();
      ((TextComponentModel) componentTag.getComponent()).setHeadingStyle(heading);
      __textComponent.updateComponent(_activeView);
    }
  }

  /**
   * changes the current text into blockquote.
   */
  public void changeToBlockquote() {
    currentInputMode = MODE_PLAIN;
    if (_activeView instanceof TextComponentItem) {
      ((TextComponentItem) _activeView).setMode(currentInputMode);
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
    if (_activeView instanceof TextComponentItem) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder
       .append(" <a href=\"")
       .append(url)
       .append("\">")
       .append(text)
       .append("</a> ");
      ((TextComponentItem) _activeView).getInputBox().append(stringBuilder.toString());
    }
  }

  public void changeToOLMode() {

  }

  public void changeToULMode() {
    currentInputMode = MODE_UL;
    if (_activeView instanceof TextComponentItem) {
      ((TextComponentItem) _activeView).setMode(currentInputMode);
      ComponentTag componentTag = (ComponentTag) _activeView.getTag();
      ((TextComponentModel) componentTag.getComponent()).setHeadingStyle(NORMAL);
      __textComponent.updateComponent(_activeView);
    }
  }
}
