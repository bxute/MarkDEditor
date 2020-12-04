# MarkDEditor [![](https://jitpack.io/v/bxute/MarkDEditor.svg)](https://jitpack.io/#bxute/MarkDEditor)
#### A Markdown Editor For Android.

Your editor will look something like:

<img src="https://user-images.githubusercontent.com/10809719/43141356-50efed8a-8f73-11e8-860b-b88bafa2c8b8.png" width="320px" height="640px"/> <img align="right" src="https://user-images.githubusercontent.com/10809719/43141350-4f74aa5e-8f73-11e8-9966-243383e7c7bc.png" width="320px" height="640px"/>

**Supported Styles**
 - Normal Text
 - H1, H2, H3, H4, H5
 - UL (Unordered List)
 - OL (Ordered List)
 - Blockquote
 - Links
 - Horizontal rulers
 - Images.
 
 ### How to use it ?
 Step 1. Add the JitPack repository to your build file
 ```groovy
 allprojects {
   repositories {
     maven { url 'https://jitpack.io' }
   }
 }
```

Step 2. Add the dependency
```groovy
dependencies {
  implementation 'com.github.bxute:MarkDEditor:v0.16'
}
```

Step 3. Add XML declaration
```xml
<ScrollView
  android:background="#ffffff"
  android:layout_above="@+id/controlBar"
  android:layout_width="match_parent"
  android:layout_height="match_parent">

 <xute.markdeditor.MarkDEditor
  android:id="@+id/mdEditor"
  android:layout_width="match_parent"
  android:layout_height="match_parent" />

</ScrollView>

<xute.markdeditor.EditorControlBar
  android:id="@+id/controlBar"
  android:layout_alignParentBottom="true"
  android:layout_width="match_parent"
  android:layout_height="48dp" />
```

Step 4. In Activity file
```java
MarkDEditor markDEditor = findViewById(R.id.mdEditor);
editorControlBar = findViewById(R.id.controlBar);
editorControlBar.setEditorControlListener(this);
markDEditor.configureEditor(
     "",//server url for image upload
     "",              //serverToken
     true,           // isDraft: set true when you are loading draft
     "Type here...", //default hint of input box
     NORMAL
    );
//load draft
//markDEditor.loadDraft(getDraftContent());
editorControlBar.setEditor(markDEditor);
```

Step 5. Implement Callback methods
```java
@Override
  public void onInsertImageClicked() {
    //openGallery();
  }

  @Override
  public void onInserLinkClicked() {
    //markDEditor.addLink("Click Here", "http://www.hapramp.com");
    
  }
```

**Inserting Image**
```java
markDEditor.insertImage(filePath);
```

**Inserting Link**
```java
 markDEditor.addLink("Click Here", "http://www.hapramp.com");
```
> Note: You can add take input for link using a DialogFragment.


**Getting Markdown from Editor**
```java
String md = markDEditor.getMarkdownContent();
```

**Getting List of Images**
```java
List<String> images = markDEditor.getImageList();
```


### Contributions

Any contributions are welcome. You can send PR or open issues.

### License
MIT License

Copyright (c) 2018 Hapramp Studio Pvt. Ltd.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


