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
import android.view.View;

import xute.markdeditor.EditorControlBar;
import xute.markdeditor.MarkDEditor;
import xute.markdeditor.utilities.FilePathUtils;

public class MainActivity extends AppCompatActivity implements EditorControlBar.EditorControlListener {
  private MarkDEditor markDEditor;
  private EditorControlBar editorControlBar;
  private final int REQUEST_IMAGE_SELECTOR = 110;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    markDEditor = findViewById(R.id.mdEditor);
    markDEditor.setServerInfo("https://api.hapramp.com/api/v2/","7997ac6f-77fd-48e6-a358-7f85c148e769");
    editorControlBar = findViewById(R.id.controlBar);
    editorControlBar.setEditorControlListener(this);
    editorControlBar.setEditor(markDEditor);
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
    String md = markDEditor.getMarkdownContent();
    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    emailIntent.setType("text/plain");
    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ankit.kumar071460@gmail.com"});
    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Generated Markdown");
    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, md);
    startActivity(Intent.createChooser(emailIntent,
     "Send email using..."));
  }
}
