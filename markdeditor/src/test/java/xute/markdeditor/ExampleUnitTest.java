package xute.markdeditor;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test
  public void addition_isCorrect() {
    ArrayList<Group> groups = new ArrayList<>();
    int si = -1;
    int ei = -1;

    int[] n = {0,1,1,1,1,0,2,2};
    //group of 1 and 0 are valid
    for (int i = 0; i < n.length; i++) {
      if (n[i] == 1) {
        si = i;
        //search for end of 0
        for (int j = i; j < n.length; j++) {
          i = j;
          if (n[j] == 1) {
            ei = j;
          } else {
            break;
          }
        }
        groups.add(new Group(si, ei));
      }
    }
    System.out.println(groups.toString());
  }

  class Group {
    private int start;
    private int end;

    public Group(int start, int end) {
      this.start = start;
      this.end = end;
    }

    public int getStart() {
      return start;
    }

    public void setStart(int start) {
      this.start = start;
    }

    public int getEnd() {
      return end;
    }

    public void setEnd(int end) {
      this.end = end;
    }

    @Override
    public String toString() {
      return "Group{" +
       "start=" + start +
       ", end=" + end +
       '}';
    }
  }
}