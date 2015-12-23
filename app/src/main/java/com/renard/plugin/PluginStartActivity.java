package com.renard.plugin;

import android.app.AlertDialog;
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
import com.renard.ocr.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginStartActivity extends BaseDocumentActivitiy {

  private final static Logger log = LoggerFactory.getLogger(PluginStartActivity.class);

  protected static Intent lastHandledIntent = null;


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
    if(intent.hasExtra(Constants.INTENT_KEY_IMAGE_TO_RECOGNIZE_URI)) {
      String imageUriString = intent.getStringExtra(Constants.INTENT_KEY_IMAGE_TO_RECOGNIZE_URI);
      if(imageUriString != null) {
        Uri imageUri = Uri.parse(imageUriString); // TODO: if imageUri points a Web image, download image
        boolean showSettingsUi = intent.getBooleanExtra(Constants.INTENT_KEY_SHOW_SETTINGS_UI, false);

        loadBitmapFromContentUri(imageUri, ImageSource.INTENT, !showSettingsUi);
      }
    }
    else if(intent.hasExtra(Constants.INTENT_KEY_CAPTURE_IMAGE)) {
      boolean captureImage = intent.getBooleanExtra(Constants.INTENT_KEY_CAPTURE_IMAGE, true);
      if(captureImage)
        startCamera();
    }
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
    if(requestCode == REQUEST_CODE_OCR) {
      if(lastHandledIntent != null && lastHandledIntent.hasExtra(Constants.INTENT_KEY_IMAGE_TO_RECOGNIZE_URI)) // we were only ask to do OCR on an image, don't ask User if she/he likes to process
      returnToCallingApplication();
      else
        askUserHowToProceed();
    }
    else
      super.onActivityResult(requestCode, resultCode, data);
  }

  protected void askUserHowToProceed() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setCancelable(false);

    try {
      View view = getLayoutInflater().inflate(R.layout.dialog_ask_user_how_to_proceed, null);

      TextView txtvwTakeAnotherPicture = (TextView) view.findViewById(R.id.txtvwTakeAnotherPicture);
      txtvwTakeAnotherPicture.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          startCamera();
        }
      });

      TextView txtvwReturnToCallingApplication = (TextView) view.findViewById(R.id.txtvwReturnToCallingApplication);
      txtvwReturnToCallingApplication.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          returnToCallingApplication();
        }
      });

      builder.setView(view);
      builder.create().show();
    } catch(Exception ex) {
      log.error("Could not show dialog_ask_user_how_to_proceed", ex);
      returnToCallingApplication();
    }
  }

  protected void returnToCallingApplication() {
    OcrResultDispatcher.ocrRecognitionProcessDone(this);

    finish();
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
