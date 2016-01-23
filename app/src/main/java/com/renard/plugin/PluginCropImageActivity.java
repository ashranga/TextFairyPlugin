package com.renard.plugin;

import android.graphics.RectF;

import com.renard.ocr.cropimage.CropImageActivity;

/**
 * Created by ganymed on 23/12/15.
 */
public class PluginCropImageActivity extends CropImageActivity {

  @Override
  protected RectF createDefaultCroppingRectangle(int width, int height) {
    // i prefer that whole image is selected by default (speeds up process if whole image should be recognized)
    return new RectF(1, 1, width - 2, height - 2);
  }
}
