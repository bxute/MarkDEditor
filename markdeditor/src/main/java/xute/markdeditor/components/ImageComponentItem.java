package xute.markdeditor.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import xute.markdeditor.R;
import xute.markdeditor.api.ImageUploader;
import xute.markdeditor.models.ComponentTag;
import xute.markdeditor.models.ImageComponentModel;
import xute.markdeditor.utilities.ImageHelper;

public class ImageComponentItem extends FrameLayout implements ImageUploader.ImageUploadCallback {
  ImageView imageView;
  EditText captionEt;
  ProgressBar imageUploadProgressBar;
  ImageView retryUpload;
  TextView statusMessage;
  ImageView removeImageButton;

  private boolean imageUploaded;
  private boolean imageUploading;
  private String downloadUrl;
  private String caption;
  private String filePath;
  private ImageUploader imageUploader;
  private Context mContext;
  private ImageComponentListener imageComponentListener;
  private OnClickListener imageClickListener = new OnClickListener() {
    @Override
    public void onClick(View view) {
      if (imageUploaded) {
        uploadedState();
      } else if (imageUploading) {
        uploadingState();
      } else {
        failedState();
      }
      hideExtraInfroWithDelay();
    }
  };
  private String serverToken;

  public ImageComponentItem(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    downloadUrl = null;
    imageUploader = new ImageUploader();
    imageUploader.setImageUploadCallback(this);
    View view = LayoutInflater.from(context).inflate(R.layout.image_component_item, this);
    imageView = view.findViewById(R.id.image);
    captionEt = view.findViewById(R.id.caption);
    retryUpload = view.findViewById(R.id.retry_image_upload_btn);
    imageUploadProgressBar = view.findViewById(R.id.image_uploading_progress_bar);
    statusMessage = view.findViewById(R.id.message);
    removeImageButton = view.findViewById(R.id.removeImageBtn);
    attachListeners();
  }

  private void attachListeners() {
    retryUpload.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        setImageInformation(filePath, serverToken, false,"");
      }
    });

    removeImageButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (imageComponentListener != null) {
          imageComponentListener.onImageRemove(getSelfIndex());
        }
      }
    });

    captionEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        checkReturnPressToInsertNewTextComponent(charSequence);
        setCaption(charSequence.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
  }

  public void setImageInformation(String filePath, String serverToken, boolean imageUploaded, String caption) {
    this.serverToken = serverToken;
    this.filePath = filePath;
    setCaption(caption);
    if (caption != null) {
      captionEt.setText(caption);
    }
    loadImage(filePath);
    if (imageUploaded) {
      onImageUploaded(filePath);
    } else {
      startImageUpload(filePath);
    }
  }

  private int getSelfIndex() {
    ComponentTag tag = (ComponentTag) getTag();
    return tag.getComponentIndex();
  }

  private void checkReturnPressToInsertNewTextComponent(CharSequence charSequence) {
    int clen = charSequence.length();
    if (clen > 0) {
      String sequenceToCheckNewLineCharacter = (clen > 1) ? charSequence.subSequence(clen - 2, clen).toString()
       :
       charSequence.subSequence(clen - 1, clen).toString();
      boolean noReadableCharactersAfterCursor = sequenceToCheckNewLineCharacter.trim().length() == 0;
      //if last characters are [AB\n<space>] or [AB\n] then we insert new TextComponent
      //else if last characters are [AB\nC] ignore the insert.
      if (sequenceToCheckNewLineCharacter.contains("\n") && noReadableCharactersAfterCursor) {
        if (imageComponentListener != null) {
          imageComponentListener.onExitFromCaptionAndInsertNewTextComponent(getSelfIndex() + 1);
        }
      }
    }
  }

  private void loadImage(String filePath) {
    ImageHelper.load(mContext, imageView, filePath);
  }

  @Override
  public void onImageUploaded(String downloadUrl) {
    setDownloadUrl(downloadUrl);
    imageUploading = false;
    imageUploaded = true;
    uploadedState();
  }

  /**
   * Uploads image to server.
   *
   * @param filePath local path of image to be uploaded.
   */
  private void startImageUpload(String filePath) {
    imageUploading = true;
    uploadingState();
    imageUploader.uploadImage(filePath, serverToken);
  }

  private void uploadedState() {
    removeImageButton.setVisibility(VISIBLE);
    retryUpload.setVisibility(GONE);
    imageUploadProgressBar.setVisibility(GONE);
    statusMessage.setVisibility(VISIBLE);
    statusMessage.setText("\u2713 Uploaded");
    hideExtraInfroWithDelay();
    //set listener
    imageView.setOnClickListener(imageClickListener);
  }

  private void uploadingState() {
    retryUpload.setVisibility(GONE);
    statusMessage.setVisibility(VISIBLE);
    statusMessage.setText("Uploading...");
    imageUploadProgressBar.setVisibility(VISIBLE);
    removeImageButton.setVisibility(VISIBLE);
    //remove listener
    imageView.setOnClickListener(null);
  }

  private void hideExtraInfroWithDelay() {
    new android.os.Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        statusMessage.setVisibility(GONE);
        removeImageButton.setVisibility(GONE);
        retryUpload.setVisibility(GONE);
        imageUploadProgressBar.setVisibility(GONE);
      }
    }, 2000);
  }

  @Override
  public void onImageUploadFailed() {
    setDownloadUrl(null);
    imageUploading = false;
    imageUploaded = false;
    failedState();
  }

  private void failedState() {
    retryUpload.setVisibility(VISIBLE);
    statusMessage.setVisibility(VISIBLE);
    statusMessage.setText("Failed To Upload. Try again!");
    imageUploadProgressBar.setVisibility(GONE);
    removeImageButton.setVisibility(VISIBLE);
    //remove listener
    imageView.setOnClickListener(null);
  }

  public ImageComponentItem(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public boolean isImageUploaded() {
    return imageUploaded;
  }

  public boolean isImageUploading() {
    return imageUploading;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
    ComponentTag tag = (ComponentTag) getTag();
    ((ImageComponentModel) tag.getComponent()).setUrl(downloadUrl);
  }

  public void setFocus() {
    imageView.setEnabled(true);
    captionEt.requestFocus();
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
    ComponentTag tag = (ComponentTag) getTag();
    ((ImageComponentModel) tag.getComponent()).setCaption(caption);
  }


  public void setImageComponentListener(ImageComponentListener imageComponentListener) {
    this.imageComponentListener = imageComponentListener;
  }

  public interface ImageComponentListener {
    void onImageRemove(int removeIndex);

    void onExitFromCaptionAndInsertNewTextComponent(int currentIndex);
  }
}
