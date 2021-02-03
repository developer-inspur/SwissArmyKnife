package com.wanjian.sak.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SAKContainerView extends FrameLayout {
  public SAKContainerView(Context context) {
    super(context);
    setBackgroundColor(Color.WHITE);
  }

  public SAKContainerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setBackgroundColor(Color.WHITE);
  }
}
