package xute.markeditor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import xute.markdeditor.MarkDEditor;

public class MainActivity extends AppCompatActivity {
  int heading = -1;
  private MarkDEditor markDEditor;

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

  public void addLink(View view) {
    //markDEditor.changeToCodeView();
  }

  public void olMode(View view) {
    markDEditor.changeToOLMode();
  }

  public void ulMode(View view) {
    markDEditor.changeToULMode();
  }
}
