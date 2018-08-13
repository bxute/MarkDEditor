package xute.markdeditor;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import xute.markdeditor.components.HorizontalDividerComponent;
import xute.markdeditor.components.HorizontalDividerComponentItem;
import xute.markdeditor.components.ImageComponent;
import xute.markdeditor.components.ImageComponentItem;
import xute.markdeditor.components.TextComponent;
import xute.markdeditor.components.TextComponentItem;
import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.models.ImageComponentModel;
import xute.markdeditor.models.TextComponentModel;
import xute.markdeditor.utilities.ComponentMetadataHelper;
import xute.markdeditor.utilities.MarkDownConverter;

import static xute.markdeditor.Styles.TextComponentStyle.BLOCKQUOTE;
import static xute.markdeditor.Styles.TextComponentStyle.NORMAL;
import static xute.markdeditor.components.TextComponentItem.MODE_OL;
import static xute.markdeditor.components.TextComponentItem.MODE_PLAIN;
import static xute.markdeditor.components.TextComponentItem.MODE_UL;

public class MarkDEditor extends MarkDCore implements TextComponent.TextComponentCallback, ImageComponentItem.ImageRemoveListener {
  public static final String TAG = MarkDEditor.class.getSimpleName();
  private View _activeView;
  // index of activeView
  private int controlIndex;
  private Context mContext;
  private TextComponent __textComponent;
  private ImageComponent __imageComponent;
  private HorizontalDividerComponent __horizontalComponent;
  private int currentInputMode;
  private MarkDownConverter markDownConverter;
  private String serverToken;
  private static String serverUrl;

  public MarkDEditor(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    bulletGroupModels = new ArrayList<>();
    markDownConverter = new MarkDownConverter();
    currentInputMode = MODE_PLAIN;
    __textComponent = new TextComponent(context, this);
    __imageComponent = new ImageComponent(context);
    __horizontalComponent = new HorizontalDividerComponent(context);
    addTextComponent(0);
    setHeading(NORMAL);
  }

  public void setServerInfo(String serverUrl, String serverToken) {
    this.serverToken = serverToken;
    this.serverUrl = serverUrl;
  }

  public static String getServerUrl(){
    return serverUrl;
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
    __textComponent.updateComponent(textComponentItem);
    setFocus(textComponentItem);
    reComputeTagsAfter(insertIndex);
    refreshViewOrder();
  }

  private EditorFocusReporter editorFocusReporter;

  @Override
  public void onInsertTextComponent(int selfIndex) {
    addTextComponent(selfIndex + 1);
  }

  @Override
  public void onFocusGained(View view) {
    setFocus(view);
  }

  /**
   * This callback method removes view at given index.
   * It checks if there is a horizontal line just before it, it removes the line too.
   * Else it removes the current view only.
   * @param selfIndex index of view to remove.
   */
  @Override
  public void onRemoveTextComponent(int selfIndex) {
    if (selfIndex == 0)
      return;
    //know the type of previous view
    View viewToBeRemoved = getChildAt(selfIndex);
    View previousView = getChildAt(selfIndex - 1);
    String content = ((TextComponentItem) viewToBeRemoved).getInputBox().getText().toString();
    removeViewAt(selfIndex);
    reComputeTagsAfter(selfIndex);
    if (previousView instanceof HorizontalDividerComponentItem) {
      //remove previous view.
      removeViewAt(selfIndex - 1);
      reComputeTagsAfter(selfIndex - 1);
      //focus on latest text component
      int lastTextComponent = getLatestTextComponentIndexBefore(selfIndex - 1);
      setFocus(getChildAt(lastTextComponent));
    } else if (previousView instanceof TextComponentItem) {
      int contentLen = ((TextComponentItem) previousView).getInputBox().getText().toString().length();
      ((TextComponentItem) previousView).getInputBox().append(String.format("%s", content));
      setFocus(previousView, contentLen);
    }
    refreshViewOrder();
  }

  /**
   * This method searches whithin view group for a TextComponent which was
   * inserted prior to startIndex.
   * @param starIndex index from which search starts.
   * @return index of LatestTextComponent before startIndex.
   */
  private int getLatestTextComponentIndexBefore(int starIndex) {
    View view = null;
    for (int i = starIndex; i >= 0; i--) {
      view = getChildAt(i);
      if (view instanceof TextComponentItem)
        return i;
    }
    return 0;
  }

  /**
   * re-compute the indexes of view after a view is inserted/deleted.
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

  /**
   * @param view to be focused on.
   */
  private void setFocus(View view) {
    controlIndex = ((ComponentTag) view.getTag()).getComponentIndex();
    _activeView = view;
    currentInputMode = ((TextComponentItem) _activeView).getMode();
    ((TextComponentItem) view).getInputBox().requestFocus();
    reportStylesOfFocusedView((TextComponentItem) view);
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

  /**
   * sets heading to text component
   *
   * @param heading number to be set
   */
  public void setHeading(int heading) {
    Log.d("Editor", "setting heading " + heading);
    currentInputMode = MODE_PLAIN;
    if (_activeView instanceof TextComponentItem) {
      ((TextComponentItem) _activeView).setMode(currentInputMode);
      ComponentTag componentTag = (ComponentTag) _activeView.getTag();
      ((TextComponentModel) componentTag.getComponent()).setHeadingStyle(heading);
      __textComponent.updateComponent(_activeView);
    }
    refreshViewOrder();
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
    refreshViewOrder();
  }

  /**
   * change the current insert mode to Ordered List Mode.
   * Increasing numbers are used to denote each item.
   */
  public void changeToOLMode() {
    currentInputMode = MODE_OL;
    if (_activeView instanceof TextComponentItem) {
      ((TextComponentItem) _activeView).setMode(currentInputMode);
      ComponentTag componentTag = (ComponentTag) _activeView.getTag();
      ((TextComponentModel) componentTag.getComponent()).setHeadingStyle(NORMAL);
      __textComponent.updateComponent(_activeView);
    }
    refreshViewOrder();
  }

  /**
   * change the current insert mode to UnOrdered List Mode.
   * Circular filled bullets are used to denote each item.
   */
  public void changeToULMode() {
    currentInputMode = MODE_UL;
    if (_activeView instanceof TextComponentItem) {
      ((TextComponentItem) _activeView).setMode(currentInputMode);
      ComponentTag componentTag = (ComponentTag) _activeView.getTag();
      ((TextComponentModel) componentTag.getComponent()).setHeadingStyle(NORMAL);
      __textComponent.updateComponent(_activeView);
    }
    refreshViewOrder();
  }

  /**
   * This method gets the suitable insert index using
   * `checkInvalidateAndCalculateInsertIndex()` method.
   * Prepares the ImageComponent and inserts it.
   * Since the user might need to type further, it inserts new TextComponent below
   * it.
   *
   * @param filePath uri of image to be inserted.
   */
  public void insertImage(String filePath) {
    int insertIndex = checkInvalidateAndCalculateInsertIndex();
    ImageComponentItem imageComponentItem = __imageComponent.getNewImageComponentItem(filePath, serverToken, this);
    //prepare tag
    ImageComponentModel imageComponentModel = new ImageComponentModel();
    ComponentTag imageComponentTag = ComponentMetadataHelper.getNewComponentTag(insertIndex);
    imageComponentTag.setComponent(imageComponentModel);
    imageComponentItem.setTag(imageComponentTag);
    addView(imageComponentItem, insertIndex);
    reComputeTagsAfter(insertIndex);
    refreshViewOrder();
    //add another text component below image
    insertIndex++;
    currentInputMode = MODE_PLAIN;
    addTextComponent(insertIndex);
  }

  /**
   * This method checks the current active/focussed view.
   * If there is some text in it, then next insertion will take place below this
   * view.
   * Else the current focussed view will be removed and new view will inserted
   * at its position.
   *
   * @return index of next insert.
   */
  private int checkInvalidateAndCalculateInsertIndex() {
    ComponentTag tag = (ComponentTag) _activeView.getTag();
    int activeIndex = tag.getComponentIndex();
    View view = getChildAt(activeIndex);
    //check for TextComponentItem
    if (view instanceof TextComponentItem) {
      //if active text component has some texts.
      if (((TextComponentItem) view).getInputBox().getText().length() > 0) {
        //insert below it
        return activeIndex + 1;
      } else {
        //remove current view
        removeViewAt(activeIndex);
        reComputeTagsAfter(activeIndex);
        refreshViewOrder();
        //insert at the current position.
        return activeIndex;
      }
    }
    return activeIndex + 1;
  }

  /**
   * inserts new horizontal ruler.
   */
  public void insertHorizontalDivider() {
    int insertIndex = getNextIndex();
    HorizontalDividerComponentItem horizontalDividerComponentItem = __horizontalComponent.getNewHorizontalComponentItem();
    ComponentTag _hrTag = ComponentMetadataHelper.getNewComponentTag(insertIndex);
    horizontalDividerComponentItem.setTag(_hrTag);
    addView(horizontalDividerComponentItem, insertIndex);
    reComputeTagsAfter(insertIndex);
    //add another text component below image
    insertIndex++;
    currentInputMode = MODE_PLAIN;
    addTextComponent(insertIndex);
    refreshViewOrder();
  }

  /**
   * @return index next to focussed view.
   */
  private int getNextIndex() {
    ComponentTag tag = (ComponentTag) _activeView.getTag();
    return tag.getComponentIndex() + 1;
  }

  @Override
  public void onImageRemove(int removeIndex) {
    if (removeIndex == 0) {
      //insert 1 text component
      removeViewAt(0);
      addTextComponent(0);
    } else {
      removeViewAt(removeIndex);
    }
    reComputeTagsAfter(removeIndex);
    refreshViewOrder();
  }

  /**
   * overloaded method for focusing view, it puts the cursor at specified position.
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
      reportStylesOfFocusedView((TextComponentItem) view);
    }
  }

  /**
   * method to send callback for focussed view back to subscriber(if any).
   *
   * @param view newly focus view.
   */
  private void reportStylesOfFocusedView(TextComponentItem view) {
    if (editorFocusReporter != null) {
      editorFocusReporter.onFocusedViewHas(view.getMode(), view.getTextHeadingStyle());
    }
  }

  /**
   * @return markdown format of editor content.
   */
  public String getMarkdownContent() {
    if (markDownConverter.isDataProcessed()) {
      return markDownConverter.getMarkDown();
    } else {
      return markDownConverter.processData(this).getMarkDown();
    }
  }

  /**
   * @return list of images inserted.
   */
  public List<String> getImageList() {
    if (markDownConverter.isDataProcessed()) {
      return markDownConverter.getImages();
    } else {
      return markDownConverter.processData(this).getImages();
    }
  }
  /**
   * setter method to subscribe for listening to focus change.
   * @param editorFocusReporter callback for editor focus.
   */
  public void setEditorFocusReporter(EditorFocusReporter editorFocusReporter) {
    this.editorFocusReporter = editorFocusReporter;
  }

  public interface EditorFocusReporter {
    void onFocusedViewHas(int mode, int textComponentStyle);
  }
}
