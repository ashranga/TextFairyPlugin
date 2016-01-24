package com.renard.plugin;

import android.app.AlertDialog;
import android.graphics.RectF;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renard.ocr.LayoutQuestionDialog;
import com.renard.ocr.R;
import com.renard.ocr.cropimage.CropImageActivity;
import com.renard.util.PreferencesUtils;

/**
 * Created by ganymed on 23/12/15.
 */
public class PluginCropImageActivity extends CropImageActivity {

  protected MenuItem chosenLanguageAndLayoutItem = null;


  @Override
  protected RectF createDefaultCroppingRectangle(int width, int height) {
    // i prefer that whole image is selected by default (speeds up process if whole image should be recognized)
    return new RectF(1, 1, width - 2, height - 2);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    boolean result = super.onPrepareOptionsMenu(menu);

    if(showActionItems()) {
      chosenLanguageAndLayoutItem = menu.findItem(R.id.item_chosen_language_and_layout);
      showMenuItemLanguageAndLayout(chosenLanguageAndLayoutItem);

      RelativeLayout rlyChosenLanguageAndLayout = (RelativeLayout)chosenLanguageAndLayoutItem.getActionView().findViewById(R.id.rlyChosenLanguageAndLayout);
      rlyChosenLanguageAndLayout.setOnClickListener(languageAndLayoutMenuItemViewClickListener);
    }

    return result;
  }

  protected void showMenuItemLanguageAndLayout(MenuItem chosenLanguageAndLayoutItem) {
    setLanguageAndLayoutMenuItemView(chosenLanguageAndLayoutItem);
    chosenLanguageAndLayoutItem.setVisible(true);
  }

  protected void setLanguageAndLayoutMenuItemView(MenuItem chosenLanguageAndLayoutItem) {
    TextView txtvwChosenLanguage = (TextView)chosenLanguageAndLayoutItem.getActionView().findViewById(R.id.txtvwChosenLanguage);
    txtvwChosenLanguage.setText(getSelectedLanguage());

    ImageView imgvwChosenLayout = (ImageView)chosenLanguageAndLayoutItem.getActionView().findViewById(R.id.imgvwChosenLayout);
    LayoutQuestionDialog.LayoutKind selectedLayout = getSelectedLayout();

    if(selectedLayout == LayoutQuestionDialog.LayoutKind.COMPLEX) {
      imgvwChosenLayout.setImageResource(R.drawable.column_layout);
    }
    else {
      imgvwChosenLayout.setImageResource(R.drawable.page_layout);
    }
  }

  protected String getSelectedLanguage() {
    return PreferencesUtils.getPluginLastSelectedOCRLanguage(this);
  }

  protected LayoutQuestionDialog.LayoutKind getSelectedLayout() {
    return PreferencesUtils.getPluginLastSelectedLayoutKind(this);
  }


  protected View.OnClickListener languageAndLayoutMenuItemViewClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      showLayoutQuestionDialog();
    }
  };

  protected void showLayoutQuestionDialog() {
    AlertDialog alertDialog = LayoutQuestionDialog.createDialog(this, new LayoutQuestionDialog.LayoutChoseListener() {
      @Override
      public void onLayoutChosen(LayoutQuestionDialog.LayoutKind layoutKind, String language) {
        chosenLanguageAndLayoutChanged(layoutKind, language);
      }
    }, false, getSelectedLayout(), getSelectedLanguage(), true);

    alertDialog.show();
  }

  protected void chosenLanguageAndLayoutChanged(LayoutQuestionDialog.LayoutKind layoutKind, String language) {
    PreferencesUtils.savePluginLastSelectedLayoutKind(this, layoutKind);
    PreferencesUtils.savePluginLastSelectedOCRLanguage(this, language);

    updateMenuItemLanguageAndLayout();
  }

  protected void updateMenuItemLanguageAndLayout() {
    if(chosenLanguageAndLayoutItem != null) {
      showMenuItemLanguageAndLayout(chosenLanguageAndLayoutItem);
    }
  }

}
