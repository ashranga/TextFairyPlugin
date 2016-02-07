package com.renard.plugin;

/**
 * Created by ganymed on 18/08/15.
 */
public class Constants {

  public final static String INTENT_KEY_RECOGNITION_SOURCE = "RecognitionSource";

  public final static String RECOGNITION_SOURCE_RECOGNIZE_FROM_URI = "RecognizeFromUri";

  public final static String RECOGNITION_SOURCE_CAPTURE_IMAGE = "CaptureImage";

  public final static String RECOGNITION_SOURCE_GET_FROM_GALLERY = "GetFromGallery";

  public final static String RECOGNITION_SOURCE_ASK_USER = "AskUser";


  public final static String INTENT_KEY_IMAGE_TO_RECOGNIZE_URI = "ImageUri";

  public final static String INTENT_KEY_SHOW_SETTINGS_UI = "ShowSettingsUi";


  public final static String INTENT_KEY_OCR_RESULT_HOCR_STRING = "HOCR_String";
  public final static String INTENT_KEY_OCR_RESULT_UTF8_STRING = "Utf8_String";
  public final static String INTENT_KEY_OCR_RESULT_ACCURACY = "Accuracy";


  public final static String SEND_OCR_RESULT_INTENT_ACTION = "TextFairyOcrResult";

  public final static String IS_USER_CANCELLED_OCR_RESULT_EXTRA_NAME = "UserCancelled";
  public final static String RECOGNITION_SUCCESSFUL_OCR_RESULT_EXTRA_NAME = "RecognitionSuccessful";
  public final static String ERROR_MESSAGE_OCR_RESULT_EXTRA_NAME = "ErrorMessage";

  public final static String ACCURACY_OCR_RESULT_EXTRA_NAME = "Accuracy";
  public final static String IS_DONE_OCR_RESULT_EXTRA_NAME = "IsDone";

  public final static String HOCR_OCR_RESULT_EXTRA_NAME = "HOCR";
  public final static String UTF8_OCR_RESULT_EXTRA_NAME = "Utf8";

}
