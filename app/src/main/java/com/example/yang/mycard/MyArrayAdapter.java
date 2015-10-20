package com.example.yang.mycard;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yang on 2015/5/16.
 */
public class MyArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    private List<String> list;

    public MyArrayAdapter(Context context, int resource, List<String> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    static class ViewHolder {
        private TextView since;
        private TextView num;
        private TextView days;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            convertView = layoutInflater.inflate(resource, null);
            holder.since = (TextView) convertView.findViewById(R.id.since);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.days = (TextView) convertView.findViewById(R.id.da);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DisplayMetrics dm = new DisplayMetrics();
        int width = dm.widthPixels;


        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Dekar Light.otf");

//        view.setAlpha(0.3f);
        holder.since.setTypeface(tf);
        holder.num.setTypeface(tf);

        holder.days.setTypeface(tf);
        holder.num.setTypeface(tf);
//        view.setMinimumWidth(width);
        convertView.setMinimumHeight(width);
        holder.since.setText(list.get(list.size() - position - 1).split("=")[0]);
        holder.num.setText(list.get(list.size() - position - 1).split("=")[1]);
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

        alphaAnimation.setDuration(800);

        animationSet.addAnimation(alphaAnimation);

        convertView.startAnimation(animationSet);

        return convertView;
    }


}
