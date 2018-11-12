package xute.markdeditor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import xute.markdeditor.datatype.DraftDataItemModel;

public class DraftModel {
  public static final int ITEM_TYPE_TEXT = 0;
  public static final int ITEM_TYPE_IMAGE = 1;
  public static final int ITEM_TYPE_HR = 2;

  @Expose
  @SerializedName("draftTitle")
  String draftTitle;
  @Expose
  @SerializedName("draftId")
  long draftId;
  @Expose
  @SerializedName("items")
  ArrayList<DraftDataItemModel> items;

  public DraftModel() {
  }

  public DraftModel(ArrayList<DraftDataItemModel> items) {
    this.items = items;
  }

  public String getDraftTitle() {
    return draftTitle;
  }

  public void setDraftTitle(String draftTitle) {
    this.draftTitle = draftTitle;
  }

  public long getDraftId() {
    return draftId;
  }

  public void setDraftId(long draftId) {
    this.draftId = draftId;
  }

  public ArrayList<DraftDataItemModel> getItems() {
    return items;
  }

  public void setItems(ArrayList<DraftDataItemModel> items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "DraftModel{" +
     "items=" + items +
     '}';
  }
}
