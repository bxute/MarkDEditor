package xute.markdeditor;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import static xute.markdeditor.Styles.TextComponentStyle.BLOCKQUOTE;
import static xute.markdeditor.Styles.TextComponentStyle.H1;
import static xute.markdeditor.Styles.TextComponentStyle.H2;
import static xute.markdeditor.Styles.TextComponentStyle.H3;
import static xute.markdeditor.Styles.TextComponentStyle.H4;
import static xute.markdeditor.Styles.TextComponentStyle.H5;
import static xute.markdeditor.Styles.TextComponentStyle.NORMAL;
import static xute.markdeditor.components.TextComponentItem.MODE_OL;
import static xute.markdeditor.components.TextComponentItem.MODE_PLAIN;
import static xute.markdeditor.components.TextComponentItem.MODE_UL;

public class EditorControlBar extends FrameLayout implements MarkDEditor.EditorFocusReporter {
  public static final int MAX_HEADING = 5;
  private Context mContext;
  private MarkDEditor mEditor;
  private TextView normalTextBtn;
  private TextView headingBtn, headingNumberBtn;
  private ImageView bulletBtn;
  private ImageView blockQuoteBtn;
  private ImageView linkBtn;
  private ImageView hrBtn;
  private ImageView imageBtn;
  private int enabledColor;
  private int disabledColor;

  private int currentHeading = 1;
  private boolean olEnabled;
  private boolean ulEnabled;
  private boolean blockquoteEnabled;

  private EditorControlListener editorControlListener;

  public EditorControlBar(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.editor_control_bar, this);
    normalTextBtn = view.findViewById(R.id.normalTextBtn);
    headingBtn = view.findViewById(R.id.headingBtn);
    headingNumberBtn = view.findViewById(R.id.headingNumberBtn);
    bulletBtn = view.findViewById(R.id.bulletBtn);
    blockQuoteBtn = view.findViewById(R.id.blockquoteBtn);
    linkBtn = view.findViewById(R.id.insertLinkBtn);
    hrBtn = view.findViewById(R.id.insertHrBtn);
    imageBtn = view.findViewById(R.id.insertImageBtn);
    enabledColor = Color.parseColor("#0994cf");
    disabledColor = Color.parseColor("#3e3e3e");

    normalTextBtn.setTextColor(enabledColor);
    headingBtn.setTextColor(disabledColor);
    headingNumberBtn.setTextColor(disabledColor);
    bulletBtn.setColorFilter(disabledColor);
    blockQuoteBtn.setColorFilter(disabledColor);
    linkBtn.setColorFilter(disabledColor);
    hrBtn.setColorFilter(disabledColor);
    imageBtn.setColorFilter(disabledColor);
    attachListeners();
  }

  public EditorControlBar(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public void setEditor(MarkDEditor editor) {
    this.mEditor = editor;
    subscribeForStyles();
  }

  private void subscribeForStyles() {
    if (mEditor != null) {
      mEditor.setEditorFocusReporter(this);
    }
  }

  private void attachListeners() {
    normalTextBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        mEditor.setHeading(NORMAL);
        invalidateStates(MODE_PLAIN, NORMAL);
      }
    });

    headingBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (currentHeading == MAX_HEADING) {
          currentHeading = 1;
          mEditor.setHeading(currentHeading);
        } else {
          mEditor.setHeading(++currentHeading);
        }
        invalidateStates(MODE_PLAIN, currentHeading);
      }
    });

    bulletBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (olEnabled) {
          //switch to normal
          mEditor.setHeading(NORMAL);
          invalidateStates(MODE_PLAIN, NORMAL);
          olEnabled = false;
          ulEnabled = false;
        } else if (ulEnabled) {
          // switch to ol mode
          mEditor.changeToOLMode();
          invalidateStates(MODE_OL, NORMAL);
          olEnabled = true;
          ulEnabled = false;
        } else if (!olEnabled && !ulEnabled) {
          // switch to ul mode
          mEditor.changeToULMode();
          invalidateStates(MODE_UL, NORMAL);
          ulEnabled = true;
          olEnabled = false;
        }
      }
    });

    blockQuoteBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (blockquoteEnabled) {
          //switch to normal
          mEditor.setHeading(NORMAL);
          invalidateStates(MODE_PLAIN, NORMAL);
        } else {
          //blockquote
          mEditor.changeToBlockquote();
          invalidateStates(MODE_PLAIN, BLOCKQUOTE);
        }
      }
    });

    hrBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        mEditor.insertHorizontalDivider();
      }
    });

    linkBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (editorControlListener != null) {
          editorControlListener.onInserLinkClicked();
        }
      }
    });

    imageBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (editorControlListener != null) {
          editorControlListener.onInsertImageClicked();
        }
      }
    });
  }

  private void enableNormalText(boolean enabled) {
    if (enabled) {
      normalTextBtn.setTextColor(enabledColor);
    } else {
      normalTextBtn.setTextColor(disabledColor);
    }
  }

  private void enableHeading(boolean enabled, int headingNumber) {
    if (enabled) {
      currentHeading = headingNumber;
      headingBtn.setTextColor(enabledColor);
      headingNumberBtn.setTextColor(enabledColor);
      headingNumberBtn.setText(String.valueOf(headingNumber));
    } else {
      currentHeading = 0;
      headingBtn.setTextColor(disabledColor);
      headingNumberBtn.setTextColor(disabledColor);
      headingNumberBtn.setText("1");
    }
  }

  private void enableBullet(boolean enable, boolean isOrdered) {
    if (enable) {
      if (isOrdered) {
        olEnabled = true;
        ulEnabled = false;
        bulletBtn.setImageResource(R.drawable.ol);
      } else {
        ulEnabled = true;
        olEnabled = false;
        bulletBtn.setImageResource(R.drawable.ul);
      }
      bulletBtn.setColorFilter(enabledColor);
    } else {
      ulEnabled = false;
      olEnabled = false;
      bulletBtn.setImageResource(R.drawable.ul);
      bulletBtn.setColorFilter(disabledColor);
    }
  }

  private void enableBlockquote(boolean enable) {
    blockquoteEnabled = enable;
    if (enable) {
      blockQuoteBtn.setColorFilter(enabledColor);
    } else {
      blockQuoteBtn.setColorFilter(disabledColor);
    }
  }

  private void invalidateStates(int mode, int textComponentStyle) {
    if (mode == MODE_OL) {
      enableBlockquote(false);
      enableHeading(false, 1);
      enableNormalText(false);
      enableBullet(true, true);
    } else if (mode == MODE_UL) {
      enableBlockquote(false);
      enableHeading(false, 1);
      enableNormalText(false);
      enableBullet(true, false);
    } else if (mode == MODE_PLAIN) {
      if (textComponentStyle == H1) {
        enableBlockquote(false);
        enableHeading(true, 1);
        enableNormalText(false);
        enableBullet(false, false);
      } else if (textComponentStyle == H2) {
        enableBlockquote(false);
        enableHeading(true, 2);
        enableNormalText(false);
        enableBullet(false, false);
      } else if (textComponentStyle == H3) {
        enableBlockquote(false);
        enableHeading(true, 3);
        enableNormalText(false);
        enableBullet(false, false);
      } else if (textComponentStyle == H4) {
        enableBlockquote(false);
        enableHeading(true, 4);
        enableNormalText(false);
        enableBullet(false, false);
      } else if (textComponentStyle == H5) {
        enableBlockquote(false);
        enableHeading(true, 5);
        enableNormalText(false);
        enableBullet(false, false);
      } else if (textComponentStyle == BLOCKQUOTE) {
        enableBlockquote(true);
        enableHeading(false, 1);
        enableNormalText(false);
        enableBullet(false, false);
      } else if (textComponentStyle == NORMAL) {
        enableBlockquote(false);
        enableHeading(false, 1);
        enableNormalText(true);
        enableBullet(false, false);
      }
    }
  }

  @Override
  public void onFocusedViewHas(int mode, int textComponentStyle) {
    invalidateStates(mode, textComponentStyle);
  }

  public void setEditorControlListener(EditorControlListener editorControlListener) {
    this.editorControlListener = editorControlListener;
  }

  public interface EditorControlListener {
    void onInsertImageClicked();

    void onInserLinkClicked();
  }
}
