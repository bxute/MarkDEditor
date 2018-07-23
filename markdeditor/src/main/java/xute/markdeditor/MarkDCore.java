package xute.markdeditor;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import xute.markdeditor.components.TextComponentItem;
import xute.markdeditor.models.BulletGroupModel;

import static xute.markdeditor.components.TextComponentItem.MODE_OL;

public class MarkDCore extends LinearLayout {
  ArrayList<BulletGroupModel> bulletGroupModels;
  public MarkDCore(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    this.setOrientation(VERTICAL);
    bulletGroupModels = new ArrayList<>();
  }

  protected void refreshViewOrder() {
    makeBulletGroups();
    invalidateComponentMode(bulletGroupModels);
  }


  private void makeBulletGroups() {
    bulletGroupModels.clear();
    int startIndex;
    int endIndex = -1;
    View child;
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      child = getChildAt(i);
      //skip non-textcomponent items
      if ((child instanceof TextComponentItem)) {
        if (((TextComponentItem) child).getMode() == MODE_OL) {
          startIndex = i;
          //search end of this group
          for (int j = i; j < childCount; j++) {
            i = j;
            child = getChildAt(j);
            if (((TextComponentItem) child).getMode() == MODE_OL) {
              endIndex = i;
            } else {
              break;
            }
          }
          //prepate model and add
          BulletGroupModel groupModel = new BulletGroupModel();
          groupModel.setOrderType(MODE_OL);
          groupModel.setStartIndex(startIndex);
          groupModel.setEndIndex(endIndex);
          bulletGroupModels.add(groupModel);
        }
      }
    }
  }

  private void invalidateComponentMode(ArrayList<BulletGroupModel> bulletGroupModels) {
    int ot;
    int si;
    int ei;
    TextComponentItem _tempChild;
    //loop through each group
    for (int i = 0; i < bulletGroupModels.size(); i++) {
      ot = bulletGroupModels.get(i).getOrderType();
      si = bulletGroupModels.get(i).getStartIndex();
      ei = bulletGroupModels.get(i).getEndIndex();
      if (ot == MODE_OL) {
        //set ol mode
        int ci = 1;
        for (int j = si; j <= ei; j++) {
          _tempChild = (TextComponentItem) getChildAt(j);
          _tempChild.setMode(MODE_OL);
          _tempChild.setIndicator(ci + ". ");
          ci++;
        }
      }
    }
  }
}
