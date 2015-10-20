package com.example.yang.mycard;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 卡片点开之后的大卡片activity
 * Created by yang on 2015/5/16.
 *
 * @author lypeer
 * @version 1.0
 */
public class BigCardView extends Activity implements View.OnClickListener, View.OnTouchListener {
    private static final int DELETE = 20;
    private static final int MENU_UP = 10;
    private static final int MENU_DOWN = 11;
    private static boolean CHANGE_IS_CLICK = false;
    private static final int MONDAY = 1;
    private static final int TUESDAY = 2;
    private static final int WENDEADAY = 3;
    private static final int THUESDAY = 4;
    private static final int FRIDAY = 5;
    private static final int SATEDAY = 6;
    private static final int SUNDAY = 0;


    /**
     * 菜单按钮
     */
    private Button menu;
    //返回按钮
    private Button back;
    //表示什么事情的EditText
    private EditText change_thing;
    //至今已经多少天的TextView
    private TextView change_haomany;
    //这个是修改之后的日期的毫秒值
    private String s;
    //position写的是点击的时候传进来的item的position
    private int position;
    /**
     * 按钮选项的布局
     */
    private LinearLayout menu_item;
    /**
     * 主要界面的布局
     */
    private LinearLayout body_layout;
    /**
     * 日历的布局
     */
    private LinearLayout calendar_layout;
    /**
     * 改变日期的布局
     */
    private RelativeLayout change_layout;
    /**
     * 菜单页的布局
     */
    private RelativeLayout first_layout;
    /**
     * 判断菜单键的状态的int值
     */
    private int up_or_down = MENU_UP;
    /**
     * 判断是星期几的int值
     */
    private int week_num_cal;
    /**
     * 传进来的年份
     */
    private int year_cal;
    /**
     * 传进来的月份
     */
    private int month_cal;
    /**
     * 传进来的日子
     */
    private int day_cal;
    /**
     * 传进来的月份有多少天
     */
    private int day_haomany_cal;
    /**
     * 页数
     */
    private int page_cal = 0;
    /**
     * 圆圈的位置
     */
    private int circle_position;

    private CalendarAdapter adapter;

    private int[] day_hoamany_array = new int[]{
            31, 28, 31, 30, 31, 30,
            31, 31, 30, 31, 30, 31
    };
    /**
     * 显示年月的textview
     */
    private TextView month_year;
    /**
     * 显示星期几的布局
     */
    private LinearLayout xinqi;
    /**
     * 日历的gridview
     */
    private GridView calendarView;
    /**
     * 控制日历的消息
     */
    SharedPreferences sharedPreferences;
    /**
     * 控制日历的消息的editor
     */
    SharedPreferences.Editor editor;
    /**
     * 日历界面的返回按钮
     */
    private Button calendar_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置进入时的方式
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        setContentView(R.layout.bigcardview);

        init();

    }

    private void init() {
        sharedPreferences = this.getSharedPreferences("calendar", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //两种字体的tf对象
        Typeface tf = Typeface.createFromAsset(BigCardView.this.getAssets(),
                "fonts/Dekar Light.otf");
        Typeface tf1 = Typeface.createFromAsset(BigCardView.this.getAssets(),
                "fonts/manteka.ttf");

        //初始化各种对象以及设置字体
        menu_item = (LinearLayout) findViewById(R.id.menu_item);
        body_layout = (LinearLayout) findViewById(R.id.body_layout);
        calendar_layout = (LinearLayout) findViewById(R.id.calendar_layout);
        xinqi = (LinearLayout) findViewById(R.id.xinqi);
        change_layout = (RelativeLayout) findViewById(R.id.change_layout);
        first_layout = (RelativeLayout) findViewById(R.id.first_layout);

        calendarView = (GridView) findViewById(R.id.calendar_gridview);

        back = (Button) findViewById(R.id.back);

        menu = (Button) findViewById(R.id.menu);

        Button del = (Button) findViewById(R.id.del);

        Button change = (Button) findViewById(R.id.change);
        Button calendar = (Button) findViewById(R.id.calendar);

        Button choose = (Button) findViewById(R.id.choose);
        Button sureto = (Button) findViewById(R.id.sureto);
        Button cancel_change = (Button) findViewById(R.id.cancel_change);

        Button shang = (Button) findViewById(R.id.shang);
        Button xia = (Button) findViewById(R.id.xia);

        calendar_back = (Button) findViewById(R.id.calendar_back);

        menu.setOnClickListener(this);
        change.setOnClickListener(this);
        choose.setOnClickListener(this);
        sureto.setOnClickListener(this);
        del.setOnClickListener(this);
        calendar.setOnClickListener(this);
        back.setOnClickListener(this);
        cancel_change.setOnClickListener(this);
        shang.setOnClickListener(this);
        xia.setOnClickListener(this);
        calendar_back.setOnClickListener(this);

        menu.setOnTouchListener(this);
        change.setOnTouchListener(this);
        choose.setOnTouchListener(this);
        sureto.setOnTouchListener(this);
        del.setOnTouchListener(this);
        calendar.setOnTouchListener(this);
        back.setOnTouchListener(this);
        cancel_change.setOnTouchListener(this);
        calendar_back.setOnTouchListener(this);

        change_thing = (EditText) findViewById(R.id.change_thing);
        change_haomany = (TextView) findViewById(R.id.change_haomany);
        TextView day = (TextView) findViewById(R.id.day);
        TextView biaozi = (TextView) findViewById(R.id.biaozhi);

        TextView ri = (TextView) findViewById(R.id.ri);
        TextView yi = (TextView) findViewById(R.id.yi);
        TextView er = (TextView) findViewById(R.id.er);
        TextView san = (TextView) findViewById(R.id.san);
        TextView si = (TextView) findViewById(R.id.si);
        TextView wu = (TextView) findViewById(R.id.wu);
        TextView liu = (TextView) findViewById(R.id.liu);

        month_year = (TextView) findViewById(R.id.mon_year);

        biaozi.setTypeface(tf1);
        change_thing.setTypeface(tf);
        change_haomany.setTypeface(tf);
        change.setTypeface(tf);
        choose.setTypeface(tf);
        sureto.setTypeface(tf);
        day.setTypeface(tf);
        menu.setTypeface(tf);
        calendar.setTypeface(tf);
        del.setTypeface(tf);
        cancel_change.setTypeface(tf);
        month_year.setTypeface(tf);
        calendar_back.setTypeface(tf);
        ri.setTypeface(tf);
        yi.setTypeface(tf);
        er.setTypeface(tf);
        san.setTypeface(tf);
        si.setTypeface(tf);
        wu.setTypeface(tf);
        liu.setTypeface(tf);

        setMessage();

        transAnimation(first_layout, 800, 0.0F, 1.0F, 0.0F, 0.0F, 200, 0);

    }

    /**
     * 获取传来的信息并且初始化值
     */
    private void setMessage() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String thing = (String) bundle.get("thing");
        String num = (String) bundle.get("num");
        Long time = (Long) bundle.get("time");
        position = bundle.getInt("position");

        change_thing.setText(thing);
        change_haomany.setText(num);

        Date date = new Date(time);

        year_cal = date.getYear() + 1900;
        month_cal = date.getMonth() + 1;
        day_cal = date.getDate();

        month_year.setText(month_cal + "," + year_cal);
        getDayNumAndWeek(year_cal, month_cal);

        circle_position = day_cal + week_num_cal - 1;

        Log.e("test", "===" + year_cal + "nian" + month_cal + "yue" + day_cal + "ri" +
                week_num_cal + "xinqi" + day_haomany_cal);
    }

    private void getDayNumAndWeek(int year, int month) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        week_num_cal = getWeekNum(simpleDateFormat.format(new Date(year - 1900, month - 1,
                1)));

        if (month_cal == 2 && ((year_cal % 4 == 0 && year_cal % 100 != 0) || year_cal % 400 == 0)) {
            day_haomany_cal = day_hoamany_array[1] + 1;
        } else {
            day_haomany_cal = day_hoamany_array[month_cal - 1];
        }
    }

    /**
     * 判断输入的字符串代表的是星期几
     *
     * @param w 代表星期几的字符串
     * @return 代表星期几的int值
     */
    private int getWeekNum(String w) {
        int num = 0;
        switch (w) {
            case "周日":
                num = SUNDAY;
                break;
            case "周一":
                num = MONDAY;
                break;
            case "周二":
                num = TUESDAY;
                break;
            case "周三":
                num = WENDEADAY;
                break;
            case "周四":
                num = THUESDAY;
                break;
            case "周五":
                num = FRIDAY;
                break;
            case "周六":
                num = SATEDAY;
                break;
        }
        return num;
    }

    /**
     * 位移与渐变的动画的集合
     *
     * @param view      动画的组件
     * @param duration  动画进行的时间
     * @param alphaFrom 开始时透明度
     * @param alphaEnd  结束时的透明度
     * @param tranFromX x轴上开始的位移
     * @param tranEndX  x轴上结束的位移
     * @param tranFromY y轴上开始的位移
     * @param tranEndY  y轴上结束的位移
     */
    private void transAnimation(View view, int duration,
                                float alphaFrom, float alphaEnd,
                                float tranFromX, float tranEndX,
                                float tranFromY, float tranEndY) {

        final AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(alphaFrom, alphaEnd);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(tranFromX,
                tranEndX,
                tranFromY, tranEndY);
        animation.setDuration(duration);
        animation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(animation);

        view.startAnimation(set);

    }


    private void doAnimation(int num) {
        ShuziZengjia numAnimation = new ShuziZengjia(this, change_haomany, num);
        numAnimation.setDuration(Math.abs(num));
        numAnimation.startAnimation();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.del:
                new AlertDialog.Builder(BigCardView.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("WARNING")
                        .setMessage("The card will be deleted, are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.menu:
                if (up_or_down == MENU_UP) {
                    rotateyMenu(menu, 0.0F, 180.0F);
                    scaleMenuItem(0.0F, 1.0F);
                    menu_item.setVisibility(View.VISIBLE);
                    up_or_down = MENU_DOWN;
                } else {
                    rotateyMenu(menu, 180.0F, 360.0F);
                    scaleMenuItem(1.0F, 0.0F);
                    menu_item.setVisibility(View.INVISIBLE);
                    up_or_down = MENU_UP;
                }
                break;
            case R.id.change:

                CHANGE_IS_CLICK = true;
                transAnimation(first_layout, 600, 1.0F, 0.0F, 0, -300, 0.0F, 0.0F);
                first_layout.setVisibility(View.INVISIBLE);
                transAnimation(change_layout, 600, 0.0F, 1.0F, 300, 0, 0.0F, 0.0F);
                change_layout.setVisibility(View.VISIBLE);
                change_thing.setFocusableInTouchMode(true);
                change_thing.setFocusable(true);
                break;
            case R.id.cancel_change:
                change_Back();
                break;
            case R.id.choose:
                final Calendar c = Calendar.getInstance();

                new DatePickerDialog(BigCardView.this,

                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(year, monthOfYear, dayOfMonth);
                                long set_time = c.getTimeInMillis();
                                s = set_time + "";
                                long now_time = System.currentTimeMillis();
                                int num = (int) ((now_time - set_time) / (1000 * 60 * 60 * 24));

                                change_haomany.setText(num + "");
                                doAnimation(num);
                            }
                        }

                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.sureto:
                save();
                break;
            case R.id.back:
                rotateView(back, -45, 100);
                Intent intent = new Intent(BigCardView.this, MainActivity.class);
                startActivity(intent);
                BigCardView.this.finish();
                break;
            case R.id.calendar:
                if (!sharedPreferences.contains("first")) {
                    adapter = new CalendarAdapter(this, day_cal, week_num_cal,
                            day_haomany_cal, page_cal, circle_position);

                    calendarView.setAdapter(adapter);
                    transAnimation(findViewById(R.id.calendar_title), 500, 0.0F, 1.0F, 0, 0,
                            -500, 0);
                    transAnimation(findViewById(R.id.xinqi), 500, 0.0F, 1.0F, 0, 0,
                            -500, 0);
                    transAnimation(findViewById(R.id.body_layout), 500, 1.0F, 0.0F, 0, 0,
                            0, 0);
                    rotateyCalendar(calendarView, 0.0F, 1.0F, 0, 0, -500, 0);

                    transAnimation(first_layout, 600, 1.0F, 0.0F, 0, 0, 0, 500);
                    transAnimation(calendar_back, 500, 0.0F, 1.0F, 0, 0, -500, 0);
                    first_layout.setVisibility(View.INVISIBLE);
                    calendar_back.setVisibility(View.VISIBLE);

                    findViewById(R.id.calendar_title).setVisibility(View.VISIBLE);
                    calendarView.setVisibility(View.VISIBLE);
                    xinqi.setVisibility(View.VISIBLE);

                    body_layout.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.shang:
                page_cal -= 1;

                if (month_cal == 1) {
                    month_cal = 12;
                    year_cal -= 1;
                } else {
                    month_cal -= 1;
                }
                month_year.setText(month_cal + "," + year_cal);
                getDayNumAndWeek(year_cal, month_cal);
                adapter.refresh(day_cal, week_num_cal, day_haomany_cal, page_cal, circle_position);
                rotateyCalendar(calendarView, 0.0F, 1.0F, 0, 0, -500, 0);
                break;
            case R.id.xia:
                page_cal += 1;

                if (month_cal == 12) {
                    month_cal = 1;
                    year_cal += 1;
                } else {
                    month_cal += 1;
                }
                month_year.setText(month_cal + "," + year_cal);
                getDayNumAndWeek(year_cal, month_cal);
                adapter.refresh(day_cal, week_num_cal, day_haomany_cal, page_cal, circle_position);
                rotateyCalendar(calendarView, 0.0F, 1.0F, 0, 0, -500, 0);
                break;
            case R.id.calendar_back:
                calendarBack();
                break;
        }
    }

    /**
     * c
     */
    private void calendarBack() {
        transAnimation(findViewById(R.id.calendar_title), 600, 1.0F, 0.0F, 0, 0,
                0, -500);
        transAnimation(findViewById(R.id.body_layout), 600, 0.0F, 1.0F, 0, 0,
                0, 0);
        transAnimation(calendarView, 600, 1.0F, 0.0F, 0, 0,
                0, -500);
        transAnimation(findViewById(R.id.xinqi), 600, 1.0F, 0.0F, 0, 0,
                0, -500);

        transAnimation(first_layout, 600, 0.0F, 1.0F, 0, 0, 500, 0);
        transAnimation(calendar_back, 600, 1.0F, 0.0F, 0, 0, 0, -500);

        first_layout.setVisibility(View.VISIBLE);
        calendar_back.setVisibility(View.INVISIBLE);

        findViewById(R.id.calendar_title).setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        xinqi.setVisibility(View.INVISIBLE);

        body_layout.setVisibility(View.VISIBLE);

        removePrefrences();
    }

    /**
     * 删除当前卡片的方法
     */
    private void delete() {
        Intent intent = new Intent(BigCardView.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("positi", position);
        intent.putExtras(bundle);
        BigCardView.this.setResult(DELETE, intent);
        BigCardView.this.finish();

    }

    /**
     * 保存当前的数据的方法
     */
    private void save() {
        Intent intent = new Intent(BigCardView.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("value", change_thing.getText() + "=" + change_haomany.getText() + "=" + s);
        bundle.putInt("positi", position);

        intent.putExtras(bundle);
        BigCardView.this.setResult(10, intent);
        BigCardView.this.finish();
    }

    /**
     * 返回按钮的动画类
     *
     * @param view     传进去的组件
     * @param oc       旋转的角度
     * @param duration 动画进行的时间
     */
    private void rotateView(View view, int oc, int duration) {
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, oc,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        rotateAnimation.setDuration(duration);

        animationSet.addAnimation(rotateAnimation);
        view.startAnimation(animationSet);
    }

    /**
     * menu键的动画类
     *
     * @param view   传进去的执行动画的组件
     * @param fromFl 起始的角度
     * @param endFL  结束的角度
     */
    private void rotateyMenu(View view, float fromFl, float endFL) {
        ObjectAnimator//
                .ofFloat(view, "rotationX", fromFl, endFL)//
                .setDuration(500)//
                .start();

    }

    /**
     * 日历进出的动画类
     *
     * @param view       进行动画的组件
     * @param alphaFrom  透明书的起始值
     * @param alphaEnd   透明度的终止值
     * @param transFromX 移动在x轴上的起始值
     * @param transEndx  移动在x轴上的终止值
     * @param transFromY 移动在y轴上的起始值
     * @param transEndY  移动在y轴上的终止值
     */
    private void rotateyCalendar(GridView view, float alphaFrom, float alphaEnd,
                                 float transFromX, float transEndx,
                                 float transFromY, float transEndY) {

        AnimationSet set = new AnimationSet(true);

        TranslateAnimation tranAnim = new TranslateAnimation(transFromX, transEndx,
                transFromY, transEndY);
        tranAnim.setDuration(400);

        AlphaAnimation alpAnim = new AlphaAnimation(alphaFrom, alphaEnd);
        alpAnim.setDuration(400);

        ScaleAnimation scaleAnim = new ScaleAnimation(0.0F, 1.0F, 1.0F, 1.0F,
                Animation.RELATIVE_TO_SELF, 1.0F, Animation.RELATIVE_TO_SELF, 1.0F);
        scaleAnim.setDuration(500);

        ScaleAnimation scaleAnimRight = new ScaleAnimation(1.0F, 0.0F, 1.0F, 1.0F,
                Animation.RELATIVE_TO_SELF, 1.0F, Animation.RELATIVE_TO_SELF, 1.0F);
        scaleAnimRight.setRepeatCount(1);
        scaleAnimRight.setRepeatMode(Animation.REVERSE);
        scaleAnimRight.setDuration(200);

        ScaleAnimation scaleAnimLeft = new ScaleAnimation(1.0F, 0.0F, 1.0F, 1.0F,
                Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F);
        scaleAnimLeft.setRepeatCount(1);
        scaleAnimLeft.setRepeatMode(Animation.REVERSE);
        scaleAnimLeft.setDuration(200);

        if (!sharedPreferences.contains("first")) {
            set.addAnimation(tranAnim);
            set.addAnimation(alpAnim);
            set.addAnimation(scaleAnim);

            editor.putInt("first", 1);
        } else {
            if (month_cal == 1 && sharedPreferences.getInt("page", 0) == 12) {
                set.addAnimation(scaleAnimRight);
            } else if (month_cal == 12 && sharedPreferences.getInt("page", 0) == 1) {
                set.addAnimation(scaleAnimLeft);
            } else {
                if (month_cal > sharedPreferences.getInt("page", 0)) {
                    set.addAnimation(scaleAnimRight);
                } else if (month_cal < sharedPreferences.getInt("page", 0)) {
                    set.addAnimation(scaleAnimLeft);
                }
            }

        }
        editor.putInt("page", month_cal);
        editor.apply();

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.02F);
        controller.setOrder(LayoutAnimationController.ORDER_RANDOM);

        view.setLayoutAnimation(controller);

    }


    /**
     * 清空preference
     */
    private void removePrefrences() {
        editor.remove("first");
        editor.remove("page");
        editor.apply();
    }

    /**
     * 菜单项弹出的方法
     *
     * @param fromY Y轴上的起始值
     * @param endY  Y轴上的结束值
     */
    private void scaleMenuItem(float fromY, float endY) {
        ScaleAnimation scalrAnim = new ScaleAnimation(1.0F, 1.0F, fromY, endY,
                Animation.RELATIVE_TO_SELF, 1.0F,
                Animation.RELATIVE_TO_SELF, 1.0F);
        scalrAnim.setDuration(500);
        menu_item.startAnimation(scalrAnim);
    }

    /**
     * 返回到主界面的时候调用
     */
    private void change_Back() {
        CHANGE_IS_CLICK = false;
        transAnimation(first_layout, 600, 0.0F, 1.0F, -300, 0, 0.0F, 0.0F);
        first_layout.setVisibility(View.VISIBLE);
        transAnimation(change_layout, 600, 1.0F, 0.0F, 0, 300, 0.0F, 0.0F);
        change_layout.setVisibility(View.INVISIBLE);
        change_thing.setFocusableInTouchMode(false);
        change_thing.setFocusable(false);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() != R.id.menu && v.getId() != R.id.back) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setBackgroundColor(getResources().getColor(R.color.card));
            }
            return false;
        }
        return false;
    }


    @Override
    public void onBackPressed() {

        if (CHANGE_IS_CLICK) {
            change_Back();
        } else if (sharedPreferences.contains("first")) {
            calendarBack();
        } else {
            rotateView(back, -45, 100);
            Intent intent = new Intent(BigCardView.this, MainActivity.class);
            startActivity(intent);
            BigCardView.this.finish();
        }

        removePrefrences();
    }


    @Override
    public void finish() {
        removePrefrences();
        super.finish();
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);

    }

    @Override
    protected void onDestroy() {
        removePrefrences();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        removePrefrences();
        super.onStop();
    }
}
