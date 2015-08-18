package com.renard.install;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;

import com.renard.ocr.R;
import com.renard.util.Util;

import java.io.File;

/**
 * Created by ganymed on 18/08/15.
 */
public class InstallHelper {

  public static final int REQUEST_CODE_INSTALL = 234;


  /**
   * Start the InstallActivity if possible and needed.
   */
  public static void startInstallActivityIfNeeded(final Activity context) {
    final String state = Environment.getExternalStorageState();
    if (state.equals(Environment.MEDIA_MOUNTED)) {
      if (!InstallHelper.IsInstalled(context)) {
        // install the languages if needed, create directory structure
        // (one
        // time)
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName(context, com.renard.install.InstallActivity.class.getName());
        context.startActivityForResult(intent, REQUEST_CODE_INSTALL);
      }
    } else {
      AlertDialog.Builder alert = new AlertDialog.Builder(context);
      // alert.setTitle(R.string.no_sd_card);
      alert.setMessage(context.getString(R.string.no_sd_card));
      alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
          context.finish();
        }
      });
      alert.show();
    }
  }

  /**
   * @return if the language assets are installed or not
   */
  public static boolean IsInstalled(Context appContext) {

    // check directories
    File tessDir = Util.getTrainingDataDir(appContext);

    if (!tessDir.exists()) {
      return false;
    }

    return true;
  }

  /**
   * @return the total size of the language-assets in the zip file
   */
  public static long getTotalUnzippedSize(AssetManager manager) {
        /*
         * long ret = 0; FileInputStream in = null; try { AssetFileDescriptor fd
		 * = manager.openFd(TESSDATA_FILE_NAME); ret = fd.getLength(); if (ret
		 * == AssetFileDescriptor.UNKNOWN_LENGTH) { in = fd.createInputStream();
		 * ret = 0; byte[] buffer = new byte[1024]; int bytesRead = 0; while
		 * ((bytesRead = in.read(buffer)) != -1) { ret += bytesRead; } } } catch
		 * (IOException ioe) { Log.v(DEBUG_TAG, "exception:" + ioe.toString());
		 * return 0; } finally { if (in != null) { try { in.close(); } catch
		 * (IOException ignore) { } } } return ret;
		 */
    // return 5374633;
    return 24314653;
  }
}
