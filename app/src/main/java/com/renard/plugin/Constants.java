package com.renard.plugin;

/**
 * Created by ganymed on 18/08/15.
 */
public class Constants {

  public final static String INTENT_KEY_CAPTURE_IMAGE = "CaptureImage";

  public final static String INTENT_KEY_IMAGE_TO_RECOGNIZE_URI = "ImageUri";

  public final static String INTENT_KEY_SHOW_SETTINGS_UI = "ShowSettingsUi";

  public final static String INTENT_KEY_SHOW_MESSAGE_ON_REMOTE_DEVICE_WHEN_PROCESSING_DONE = "ShowMessageOnRemoteDeviceWhenProcessingDone";


  public final static String SEND_OCR_RESULT_INTENT_ACTION = "TextFairyOcrResult";

  public final static String IS_USER_CANCELLED_OCR_RESULT_EXTRA_NAME = "UserCancelled";
  public final static String RECOGNITION_SUCCESSFUL_OCR_RESULT_EXTRA_NAME = "RecognitionSuccessful";
  public final static String ERROR_MESSAGE_OCR_RESULT_EXTRA_NAME = "ErrorMessage";

  public final static String ACCURACY_OCR_RESULT_EXTRA_NAME = "Accuracy";
  public final static String IS_DONE_OCR_RESULT_EXTRA_NAME = "IsDone";

  public final static String HOCR_OCR_RESULT_EXTRA_NAME = "HOCR";
  public final static String UTF8_OCR_RESULT_EXTRA_NAME = "Utf8";

}
