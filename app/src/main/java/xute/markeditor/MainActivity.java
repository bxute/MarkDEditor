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
import android.widget.Toast;

import xute.markdeditor.MarkDEditor;
import xute.markdeditor.utilities.FilePathUtils;

public class MainActivity extends AppCompatActivity {
  int heading = -1;
  private MarkDEditor markDEditor;
  private final int REQUEST_IMAGE_SELECTOR = 110;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    markDEditor = findViewById(R.id.mdEditor);

  }

  public void changeHeading(View view) {
    int _heading = ((++heading) % 5);
    markDEditor.setHeading(_heading);
  }

  public void changeHToBlockquote(View view) {
    markDEditor.changeToBlockquote();
  }

  public void changeToNormal(View view) {
    markDEditor.setHeading(-1);
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

  public void olMode(View view) {
    markDEditor.changeToOLMode();
  }

  public void ulMode(View view) {
    markDEditor.changeToULMode();
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

  public void addImage(View view) {
    openGallery();
  }
}
