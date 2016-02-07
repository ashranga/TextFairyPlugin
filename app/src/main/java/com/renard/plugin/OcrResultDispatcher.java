package com.renard.plugin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ganymed on 18/08/15.
 */
public class OcrResultDispatcher {

  public static void sendOcrResult(Context context, String hocrString, String utf8String, int accuracy) {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Constants.SEND_OCR_RESULT_INTENT_ACTION); // TODO: really use a generic action? What if several applications have registered a Broadcast Receiver for
    // this action? Why not letting calling application defining Action value (e.g. over a Intent.putExtra() value)?

    sendIntent.putExtra(Constants.RECOGNITION_SUCCESSFUL_OCR_RESULT_EXTRA_NAME, true);
    sendIntent.putExtra(Constants.HOCR_OCR_RESULT_EXTRA_NAME, hocrString);
    sendIntent.putExtra(Constants.UTF8_OCR_RESULT_EXTRA_NAME, utf8String);
    try { sendIntent.putExtra(Constants.ACCURACY_OCR_RESULT_EXTRA_NAME, accuracy / 100f);
    } catch(Exception ex) { Log.e("OcrResultDispatcher", "Could not convert integer Accuracy " + accuracy + " to a float value", ex); }

//    sendIntent.putExtra(Constants.IS_DONE_OCR_RESULT_EXTRA_NAME, false);
//    sendIntent.setType("text/plain");

    context.sendBroadcast(sendIntent);
  }

  public static void sendOcrError(Context context, String errorMessage) {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Constants.SEND_OCR_RESULT_INTENT_ACTION); // TODO: really use a generic action? What if several applications have registered a Broadcast Receiver for
    // this action? Why not letting calling application defining Action value (e.g. over a Intent.putExtra() value)?
    sendIntent.putExtra(Constants.RECOGNITION_SUCCESSFUL_OCR_RESULT_EXTRA_NAME, false);
    sendIntent.putExtra(Constants.ERROR_MESSAGE_OCR_RESULT_EXTRA_NAME, errorMessage);
//    sendIntent.putExtra(Constants.IS_DONE_OCR_RESULT_EXTRA_NAME, true);

    context.sendBroadcast(sendIntent);
  }

  public static void sendOcrProcessDone(Context context) {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Constants.SEND_OCR_RESULT_INTENT_ACTION); // TODO: really use a generic action? What if several applications have registered a Broadcast Receiver for
    // this action? Why not letting calling application defining Action value (e.g. over a Intent.putExtra() value)?
    sendIntent.putExtra(Constants.IS_DONE_OCR_RESULT_EXTRA_NAME, true);

    context.sendBroadcast(sendIntent);
  }

}
