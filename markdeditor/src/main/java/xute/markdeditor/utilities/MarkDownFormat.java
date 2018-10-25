package xute.markdeditor.utilities;

import static xute.markdeditor.Styles.TextComponentStyle.BLOCKQUOTE;
import static xute.markdeditor.Styles.TextComponentStyle.H1;
import static xute.markdeditor.Styles.TextComponentStyle.H2;
import static xute.markdeditor.Styles.TextComponentStyle.H3;
import static xute.markdeditor.Styles.TextComponentStyle.H4;
import static xute.markdeditor.Styles.TextComponentStyle.H5;

public class MarkDownFormat {

  public static String getTextFormat(int heading, String content) {
    String pref;
    switch (heading) {
      case H1:
        pref = "# ";
        break;
      case H2:
        pref = "## ";
        break;
      case H3:
        pref = "### ";
        break;
      case H4:
        pref = "#### ";
        break;
      case H5:
        pref = "##### ";
        break;
      case BLOCKQUOTE:
        pref = "> ";
        break;
      default:
        pref = "";
    }
    return String.format("\\n%s%s\\n", pref, content);
  }

  public static String getImageFormat(String url) {
    return String.format("\\n<center>![Image](%s)</center>", url);
  }

  public static String getCaptionFormat(String caption) {
    return caption != null ? String.format("<center>%s</center>\\n\\n\\n", caption) : "\\n\\n\\n";
  }

  public static String getLineFormat() {
    return "\\n\\n---\\n\\n";
  }

  public static String getULFormat(String content) {
    return String.format("  - %s\\n", content);
  }

  public static String getOLFormat(String indicator, String content) {
    return String.format("  %s %s\\n", indicator, content);
  }
}
