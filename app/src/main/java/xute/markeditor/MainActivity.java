package xute.markeditor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import xute.markdeditor.EditorControlBar;
import xute.markdeditor.MarkDEditor;
import xute.markdeditor.datatype.DraftDataItemModel;
import xute.markdeditor.models.DraftModel;
import xute.markdeditor.utilities.FilePathUtils;

import static xute.markdeditor.Styles.TextComponentStyle.BLOCKQUOTE;
import static xute.markdeditor.Styles.TextComponentStyle.H1;
import static xute.markdeditor.Styles.TextComponentStyle.H3;
import static xute.markdeditor.Styles.TextComponentStyle.NORMAL;
import static xute.markdeditor.components.TextComponentItem.MODE_OL;
import static xute.markdeditor.components.TextComponentItem.MODE_PLAIN;

public class MainActivity extends AppCompatActivity implements EditorControlBar.EditorControlListener {
  private final int REQUEST_IMAGE_SELECTOR = 110;
  private MarkDEditor markDEditor;
  private EditorControlBar editorControlBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    markDEditor = findViewById(R.id.mdEditor);
    markDEditor.configureEditor(
     "https://api.hapramp.com/api/v2/",
     "",
     true,
     "Start Here...",
     BLOCKQUOTE
    );
    markDEditor.loadDraft(getDraftContent());
    editorControlBar = findViewById(R.id.controlBar);
    editorControlBar.setEditorControlListener(this);
    editorControlBar.setEditor(markDEditor);
  }

  private DraftModel getDraftContent() {
    ArrayList<DraftDataItemModel> contentTypes = new ArrayList<>();
    DraftDataItemModel heading = new DraftDataItemModel();
    heading.setItemType(DraftModel.ITEM_TYPE_TEXT);
    heading.setContent("Kajal Aggarwal filmography");
    heading.setMode(MODE_PLAIN);
    heading.setStyle(H1);

    DraftDataItemModel sub_heading = new DraftDataItemModel();
    sub_heading.setItemType(DraftModel.ITEM_TYPE_TEXT);
    sub_heading.setContent("Nominated");
    sub_heading.setMode(MODE_PLAIN);
    sub_heading.setStyle(H3);

    DraftDataItemModel bl = new DraftDataItemModel();
    bl.setItemType(DraftModel.ITEM_TYPE_TEXT);
    bl.setContent("A super star of south movies!");
    bl.setMode(MODE_PLAIN);
    bl.setStyle(BLOCKQUOTE);

    DraftDataItemModel body = new DraftDataItemModel();
    body.setItemType(DraftModel.ITEM_TYPE_TEXT);
    body.setContent("\n" +
     "Kajal Aggarwal in March 2017\n" +
     "Kajal Aggarwal is an Indian actress who appears in primarily in Tamil and Telugu films.[1] She made her acting debut with a minor role in the Hindi film Kyun! Ho Gaya Na..., a box office failure. She later signed up P. Bharathiraja's Tamil film Bommalattam, which was to have been her first film in that language, but it was delayed.");
    body.setMode(MODE_PLAIN);
    body.setStyle(NORMAL);

    DraftDataItemModel hrType = new DraftDataItemModel();
    hrType.setItemType(DraftModel.ITEM_TYPE_HR);

    DraftDataItemModel imageType = new DraftDataItemModel();
    imageType.setItemType(DraftModel.ITEM_TYPE_IMAGE);
    imageType.setDownloadUrl("https://cdn.shopify.com/s/files/1/0166/3704/products/78008-3_grande.jpg");
    imageType.setCaption("Cute Pink Photo");

    DraftDataItemModel filmsList1 = new DraftDataItemModel();
    filmsList1.setItemType(DraftModel.ITEM_TYPE_TEXT);
    filmsList1.setStyle(NORMAL);
    filmsList1.setMode(MODE_OL);
    filmsList1.setContent("2009 – Filmfare Award for Best Actress – Telugu for Magadheera");

    DraftDataItemModel filmsList2 = new DraftDataItemModel();
    filmsList2.setItemType(DraftModel.ITEM_TYPE_TEXT);
    filmsList2.setStyle(NORMAL);
    filmsList2.setMode(MODE_OL);
    filmsList2.setContent("2010 – Filmfare Award for Best Actress – Telugu for Darling");
    contentTypes.add(heading);
    contentTypes.add(filmsList1);
    contentTypes.add(imageType);
//    contentTypes.add(filmsList2);
//    contentTypes.add(filmsList2);
//    contentTypes.add(filmsList2);
//    contentTypes.add(imageType);
//    contentTypes.add(imageType);
//    contentTypes.add(filmsList2);
//    contentTypes.add(filmsList2);
    return new DraftModel(contentTypes);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_SELECTOR) {
      if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
        Uri uri = data.getData();
        String filePath = FilePathUtils.getPath(this, uri);
        addImage(filePath);
      }
    }
  }

  public void addImage(String filePath) {
    markDEditor.insertImage(filePath);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_IMAGE_SELECTOR:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          openGallery();
        } else {
          //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
          //Toast.makeText()"Permission not granted to access images.");
        }
        break;
    }
  }

  private void openGallery() {
    try {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
      } else {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onInsertImageClicked() {
    openGallery();
  }

  @Override
  public void onInserLinkClicked() {
    markDEditor.addLink("Click Here", "http://www.hapramp.com");
  }

  public void printStack(View view) {
    sendMail();
  }

  private void sendMail() {
    DraftModel dm = markDEditor.getDraft();
    String json = new Gson().toJson(dm);
    Log.d("MarkDEditor", json);
  }
}
