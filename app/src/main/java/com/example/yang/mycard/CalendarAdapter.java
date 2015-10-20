package com.example.yang.mycard;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by yang on 2015/7/5.
 */
public class CalendarAdapter extends BaseAdapter {
    private static final int SIZE = 41;


    /**
     * 日期
     */
    private int day;
    /**
     * 星期几
     */
    private int week;
    /**
     * 月份的天数
     */
    private int num;
    /**
     * 传进来的Context
     */
    private Context context;
    /**
     * 页数,控制背景色
     */
    private int page;
    /**
     * 圆圈的位置
     */
    private int circle_position;

    /**
     * adapter的构造方法
     *
     * @param context context
     * @param day     日期
     * @param week    星期几
     * @param num     月份的天数
     */
    public CalendarAdapter(Context context, int day, int week, int num, int page, int position) {
        this.context = context;
        this.day = day;
        this.week = week;
        this.num = num;
        this.page = page;
        this.circle_position = position;
    }

    /**
     * viewholder类，优化使用率
     */
    static class ViewHolder {
        private TextView riqi;
    }

    @Override
    public int getCount() {
        return SIZE;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Dekar Light.otf");

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            convertView = layoutInflater.inflate(R.layout.calendar_item, null);
            holder.riqi = (TextView) convertView.findViewById(R.id.calendar_day);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.riqi.setTypeface(tf);

        if (position < week || position > (num + week - 1)) {
            holder.riqi.setText("");
            convertView.setBackgroundColor(context.getResources().getColor(R.color.empty));
            if (position == circle_position) {
                holder.riqi.setBackground(context.getResources().
                        getDrawable(R.drawable.empty));
            }
        } else {
            holder.riqi.setText(position - week + 1 + "");
            if (page < 0) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.calendar_before));
                if (position == circle_position) {
                    holder.riqi.setBackground(context.getResources().
                            getDrawable(R.drawable.empty));
                }
            } else if (page > 0) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.calendar_after));
                if (position == circle_position) {
                    holder.riqi.setBackground(context.getResources().
                            getDrawable(R.drawable.empty));
                }
            } else {
                if (position - week + 1 < day) {
                    convertView.setBackgroundColor(context.getResources().
                            getColor(R.color.calendar_before));
                } else if (position - week + 1 > day) {
                    convertView.setBackgroundColor(context.getResources().
                            getColor(R.color.calendar_after));
                } else {
                    holder.riqi.setBackground(context.getResources().
                            getDrawable(R.drawable.calendar_this));
                    convertView.setBackgroundColor(context.getResources().
                            getColor(R.color.calendar_after));
                }
            }
        }

        return convertView;
    }

    /**
     * 刷新gridview
     *
     * @param day  日期
     * @param week 星期几
     * @param num  月份的天数
     */
    public void refresh(int day, int week, int num, int page, int position) {
        this.day = day;
        this.week = week;
        this.num = num;
        this.page = page;
        this.circle_position = position;
        notifyDataSetChanged();
    }
}
