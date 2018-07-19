package xute.markdeditor.utilities;

import xute.markdeditor.models.ComponentTag;

public class ComponentMetadataHelper {
  public static ComponentTag getNewComponentTag(int index) {
    ComponentTag componentTag = new ComponentTag();
    componentTag.setComponentIndex(index);
    return componentTag;
  }
}
