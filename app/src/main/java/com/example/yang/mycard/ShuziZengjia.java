package com.example.yang.mycard;

/**
 * Created by yang on 2015/5/16.
 */

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ShuziZengjia extends Animation {
    Context context;
    TextView textView;
    int num;

    public ShuziZengjia(Context context, TextView textView, int num) {
        this.context = context;
        this.textView = textView;
        this.num = num;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        textView.setText(formatFloat(num * interpolatedTime));
    }

    public void startAnimation() {
        textView.startAnimation(this);
    }

    public final static String formatFloat(float value) {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(value);
    }
}

