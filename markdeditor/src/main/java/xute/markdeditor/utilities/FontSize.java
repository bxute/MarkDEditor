package xute.markdeditor.utilities;

import static xute.markdeditor.Styles.TextStyle.H1;
import static xute.markdeditor.Styles.TextStyle.H2;
import static xute.markdeditor.Styles.TextStyle.H3;
import static xute.markdeditor.Styles.TextStyle.H4;
import static xute.markdeditor.Styles.TextStyle.H5;
import static xute.markdeditor.Styles.TextStyle.NO_HEADING;

public class FontSize {
  public static final int H1_SIZE = 32;
  public static final int H2_SIZE = 28;
  public static final int H3_SIZE = 24;
  public static final int H4_SIZE = 20;
  public static final int H5_SIZE = 16;
  public static final int NORMAL = 16;

  public static int getFontSize(int heading) {
    switch (heading) {
      case H1:
        return H1_SIZE;
      case H2:
        return H2_SIZE;
      case H3:
        return H3_SIZE;
      case H4:
        return H4_SIZE;
      case H5:
        return H5_SIZE;
      case NO_HEADING:
        return NORMAL;
    }
    return H5_SIZE;
  }
}
