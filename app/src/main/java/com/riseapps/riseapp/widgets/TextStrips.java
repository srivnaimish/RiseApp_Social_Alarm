package com.riseapps.riseapp.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.riseapps.riseapp.R;

/**
 * Created by naimish on 4/11/17.
 */

public class TextStrips extends LinearLayout {
    public TextStrips(Context context) {
        super(context);

        LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,0,8);
        this.setLayoutParams(layoutParams);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setBackgroundResource(R.drawable.textstrip_background);
        this.setGravity(Gravity.CENTER_VERTICAL);

        TextView textView = new TextView(context);
        layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(16, 8, 16, 8);
        textView.setLayoutParams(layoutParams);
        textView.setId(R.id.text);
        textView.setTextSize(18);
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        this.addView(textView);

        ImageButton imageButton = new ImageButton(context);
        imageButton.setId(R.id.close);
        imageButton.setImageResource(R.drawable.ic_close);
        imageButton.setBackgroundResource(R.drawable.ripple);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginStart(16);
        imageButton.setLayoutParams(layoutParams);
        this.addView(imageButton);
    }

}
