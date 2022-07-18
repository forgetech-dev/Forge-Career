package com.forgetech.forgecareer.decorators;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.forgetech.forgecareer.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

/**
 * Use a custom selector
 */
public class MySelectorDecorator implements DayViewDecorator {


  private final Drawable drawable;

  public MySelectorDecorator(Activity context) {
    drawable = context.getResources().getDrawable(R.drawable.my_selector);
  }

  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return true;
  }

  @Override
  public void decorate(DayViewFacade view) {
    view.setSelectionDrawable(drawable);
  }
}
