package com.renard.plugin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.renard.install.InstallHelper;
import com.renard.ocr.BaseDocumentActivitiy;
import com.renard.ocr.ImageSource;
import com.renard.ocr.PixLoadStatus;
import com.renard.ocr.R;
import com.renard.ocr.cropimage.CropImageActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginStartActivity extends BaseDocumentActivitiy {

  private final static Logger log = LoggerFactory.getLogger(PluginStartActivity.class);

  protected static Intent lastHandledIntent = null;

  protected OcrSource lastSource;
  protected String lastSourceUri;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_plugin_start);

    InstallHelper.startInstallActivityIfNeeded(this);

    Intent intent = getIntent();
    if(intent == null)
      startCamera();
    else if(intent != lastHandledIntent) {
      lastHandledIntent = intent;
      handleIntent(intent);
    }
  }

  protected void handleIntent(Intent intent) {
    if(intent.hasExtra(Constants.OCR_SOURCE_EXTRA_NAME)) {
      String recognitionSource = intent.getStringExtra(Constants.OCR_SOURCE_EXTRA_NAME);

      if(Constants.OCR_SOURCE_RECOGNIZE_FROM_URI.equals(recognitionSource)) {
        recognizeTextOfAProvidedImage(intent);
      } else if(Constants.OCR_SOURCE_CAPTURE_IMAGE.equals(recognitionSource)) {
        startCamera();
      } else if(Constants.OCR_SOURCE_GET_FROM_GALLERY.equals(recognitionSource)) {
        startGallery();
      } else { // if recognitionSource equals Constants.RECOGNITION_SOURCE_ASK_USER or an unknown value is supplied
        askUserForRecognitionSource();
      }
    } else {
      askUserForRecognitionSource();
    }
  }

  protected void recognizeTextOfAProvidedImage(Intent intent) {
    lastSource = OcrSource.RecognizeFromUri;

    String imageUriString = intent.getStringExtra(Constants.IMAGE_TO_RECOGNIZE_URI_EXTRA_NAME);
    if(imageUriString != null) {
      Uri imageUri = Uri.parse(imageUriString); // TODO: if imageUri points a Web image, download image
      boolean showSettingsUi = intent.getBooleanExtra(Constants.SHOW_SETTINGS_UI_EXTRA_NAME, false);

      loadBitmapFromContentUri(imageUri, ImageSource.INTENT, !showSettingsUi);
    }
    else {
      sendOcrError(R.string.error_image_source_not_set);
    }
  }

  @Override
  protected void startCamera() {
    lastSource = OcrSource.CaptureImage;

    super.startCamera();

    if(cameraPicUri != null) {
      lastSourceUri = cameraPicUri.toString();
    }
  }

  @Override
  protected void startGallery() {
    lastSource = OcrSource.ChoseImageFromGallery;

    super.startGallery();
  }

  protected void askUserHowToProceed() {
    askUserForRecognitionSource(true);
  }

  protected void askUserForRecognitionSource() {
    askUserForRecognitionSource(false);
  }

  protected void askUserForRecognitionSource(boolean hasAnImageAlreadyBeenRecognized) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setCancelable(false);

    try {
      View view = getLayoutInflater().inflate(R.layout.dialog_ask_user_for_recognition_source, null);
      builder.setView(view);
      AlertDialog alert = builder.create();

      TextView txtvwTakePicture = (TextView) view.findViewById(R.id.txtvwTakePicture);
      TextView txtvwSelectPictureFromGallery = (TextView) view.findViewById(R.id.txtvwSelectPictureFromGallery);
      TextView txtvwReturnToCallingApplication = (TextView) view.findViewById(R.id.txtvwReturnToCallingApplication);

      setOnClickListeners(alert, txtvwTakePicture, txtvwSelectPictureFromGallery, txtvwReturnToCallingApplication);

      if(hasAnImageAlreadyBeenRecognized) {
        adjustOptionNames(txtvwTakePicture, txtvwSelectPictureFromGallery, txtvwReturnToCallingApplication);
      }

      alert.show();
    } catch(Exception ex) {
      log.error("Could not show dialog_ask_user_for_recognition_source", ex);
      returnToCallingApplication();
    }
  }

  protected void setOnClickListeners(final AlertDialog alert, TextView txtvwTakePicture, TextView txtvwSelectPictureFromGallery, TextView txtvwReturnToCallingApplication) {
    txtvwTakePicture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alert.dismiss();
        startCamera();
      }
    });

    txtvwSelectPictureFromGallery.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alert.dismiss();
        startGallery();
      }
    });

    txtvwReturnToCallingApplication.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alert.dismiss();
        returnToCallingApplication();
      }
    });
  }

  protected void adjustOptionNames(TextView txtvwTakePicture, TextView txtvwSelectPictureFromGallery, TextView txtvwReturnToCallingApplication) {
    txtvwTakePicture.setText(R.string.take_another_picture);

    txtvwSelectPictureFromGallery.setText(R.string.select_another_picture_from_gallery);

    txtvwReturnToCallingApplication.setText(R.string.return_to_calling_application);
  }


  @Override
  protected int getParentId() {
    return -1;
  }

  @Override
  protected Class getOCRActivityClass() {
    return PluginOCRActivity.class;
  }

  @Override
  protected Class getCropImageActivityClass() {
    return PluginCropImageActivity.class;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_OCR) { // OCR process has completed
      sendOcrResult(data);

      if (lastHandledIntent != null && lastHandledIntent.hasExtra(Constants.IMAGE_TO_RECOGNIZE_URI_EXTRA_NAME)) { // we were only ask to do OCR on an image, don't ask User if she/he likes to process
        returnToCallingApplication();
      } else {
        askUserHowToProceed();
      }
    }
    else if(resultCode == RESULT_OK || isTakeNewImageActivityResult(requestCode, resultCode, data)) { // let BaseDocumentActivity handle this result
      super.onActivityResult(requestCode, resultCode, data);
      if(mCameraResult != null && lastSource == OcrSource.ChoseImageFromGallery) {
        lastSourceUri = mCameraResult.mData.getDataString();
      }
    } else { // previous Action (take picture / select picture from gallery) has been cancelled
      askUserHowToProceed();
    }
  }

  protected void sendOcrResult(Intent data) {
    sendOcrResult(data.getStringExtra(Constants.HOCR_OCR_RESULT_EXTRA_NAME), data.getStringExtra(Constants.UTF8_OCR_RESULT_EXTRA_NAME),
        data.getIntExtra(Constants.ACCURACY_OCR_RESULT_EXTRA_NAME, 0));
  }

  protected void sendOcrResult(String hocrString, String utf8String, int accuracy) {
    OcrResultDispatcher.sendOcrResult(this, hocrString, utf8String, accuracy, lastSource, getOcrSourceUri(lastSource));
  }

  protected String getOcrSourceUri(OcrSource lastSource) {
    if(lastSource == OcrSource.RecognizeFromUri) {
      if(lastHandledIntent != null && lastHandledIntent.hasExtra(Constants.IMAGE_TO_RECOGNIZE_URI_EXTRA_NAME)) {
        return lastHandledIntent.getStringExtra(Constants.IMAGE_TO_RECOGNIZE_URI_EXTRA_NAME);
      }
    }

    return lastSourceUri;
  }

  protected boolean isTakeNewImageActivityResult(int requestCode, int resultCode, Intent data) {
    return requestCode == REQUEST_CODE_CROP_PHOTO && resultCode == CropImageActivity.RESULT_NEW_IMAGE;
  }

  @Override
  protected void showFileError(PixLoadStatus second, DialogInterface.OnClickListener positiveListener) {
    sendOcrError(getErrorMessageForPixLoadStatus(second));
    super.showFileError(second, positiveListener);
  }

  protected void sendOcrError(int errorMessageId) {
    sendOcrError((String)getText(errorMessageId));
  }

  protected void sendOcrError(String errorMessage) {
    OcrResultDispatcher.sendOcrError(this, errorMessage);
  }

  protected void returnToCallingApplication() {
    sendOcrProcessDone();

    finish();
  }

  protected void sendOcrProcessDone() {
    OcrResultDispatcher.sendOcrProcessDone(this);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.menu_plugin_start, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
