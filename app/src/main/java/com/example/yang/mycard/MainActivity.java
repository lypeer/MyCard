package com.example.yang.mycard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import android.support.v7.app.AlertDialog;

/**
 * 这是一个纪念日记录类的APP，主要是要记录一些用户想要记录的纪念日，
 * 以及距今已经多少天
 *
 * @author lypeer
 * @version 1.0
 */

public class MainActivity extends Activity {
    //记录card中的信息键值对的list，其中数据的存储方式为 thing+days
    private List<String> list = new ArrayList<>();
    //添加卡片的按钮
    private Button add;
    //填装card的gridview
    private GridView gridView;
    //这个是title上的标志
    private TextView biaozi;
    //自定义的MyArrayAdapter对象
    private MyArrayAdapter myArrayAdapter;
    //数据库帮助类的对象
    private MySQLiteHelper sqLiteHelper;

    private List<Long> time_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //初始化TypeFace对象，这个是标志的字体
        Typeface tf1 = Typeface.createFromAsset(MainActivity.this.getAssets(),
                "fonts/manteka.ttf");

        //初始化view和数据库对象
        add = (Button) findViewById(R.id.add);
        gridView = (GridView) findViewById(R.id.gridview);
        biaozi = (TextView) findViewById(R.id.biaozhi);
        biaozi.setTypeface(tf1);
        sqLiteHelper = new MySQLiteHelper(this, "VERSION3", null, 1);
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();


        //目的在于在用户第一次打开软件时添加一个默认的card
        SharedPreferences sharedPreferences = this.getSharedPreferences("times", MODE_PRIVATE);

        //搜索sharedpregerence中有没有“first”的值，如果有，就不添加默认卡片
        if (!sharedPreferences.contains("second5")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("second5", 1);
            editor.apply();
            addmorenCard();
        } else {


            //每次进入应用的时候搜索数据库中的信息，并将其赋给list完成初始化
            String sql = "select * from sincedata";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String s1 = cursor.getString(1);
                String s3 = cursor.getString(3);
                System.out.println(s1 + "==" + s3);
                if (s3 == null || s3.equals("null")) {
                    list.add(s1 + "=" + 0);
                } else {

                    long now_time = System.currentTimeMillis();
                    long set_time = Long.parseLong(s3);

                    Date date = new Date(set_time);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");
                    String week = simpleDateFormat.format(new Date(date.getYear(), date.getMonth(),
                            date.getDate()));
                    Log.e("week" , week);

                    time_list.add(set_time);
                    int num = (int) ((now_time - set_time) / (1000 * 60 * 60 * 24));
                    list.add(s1 + "=" + num);
                }
            }
            cursor.moveToFirst();
            cursor.close();
        }
        myArrayAdapter = new MyArrayAdapter(MainActivity.this, R.layout.card, list);

        //设置card出现的时候的动画并且设置adapter
        gridView.setLayoutAnimation(getAnimationController());
        gridView.setAdapter(myArrayAdapter);

        //点击时旋转并且跳到AddCard
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(true);

                RotateAnimation rotateAnimation = new RotateAnimation(0, -45,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(100);
                animationSet.addAnimation(rotateAnimation);
                add.startAnimation(animationSet);
                Intent intent = new Intent(MainActivity.this, AddACard.class);
                startActivityForResult(intent, 0);

            }
        });

        //设置Item的点击事件，将参数传给BigCard
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AnimationSet animationSet = new AnimationSet(true);
                RotateAnimation rotateAnimation = new RotateAnimation(0, 45,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(100);
                animationSet.addAnimation(rotateAnimation);
                add.startAnimation(animationSet);

                rotateyAnimRun(view);

                TextView thing = (TextView) view.findViewById(R.id.since);
                TextView num = (TextView) view.findViewById(R.id.num);
                String s1 = thing.getText().toString().trim();
                String s2 = num.getText().toString().trim();
                Intent intent = new Intent(MainActivity.this, BigCardView.class);
                Bundle bundle = new Bundle();
                bundle.putString("thing", s1);
                bundle.putString("num", s2);
                bundle.putInt("position", position);
                bundle.putLong("time" , time_list.get(time_list.size() - position - 1));
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

            }
        });
//        AVObject testObject = new AVObject("TestObject1");
//        testObject.put("foo", db);
//
//        testObject.saveInBackground();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //判断从activity返回的requestCode，0代表是从AddACard返回，1代表是从BigCard返回
        if (requestCode == 0) {
            if (resultCode == 5) {

                //执行数据库的添加操作
                SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
                String sql = "insert into sincedata( thing , days , time , realid) values (? , ? , ? , ?)";
                Bundle bundle = data.getExtras();
                String info = bundle.getString("value");
                list.add(info);
                time_list.add(Long.parseLong(info.split("=")[2]));

                db.execSQL(sql, new String[]{info.split("=")[0],
                        info.split("=")[1], info.split("=")[2], 1 + ""});

                setReadId(db);

                gridView.setLayoutAnimation(getAnimationController());
                gridView.setAdapter(myArrayAdapter);
            }
        } else if (requestCode == 1) {

            //判断返回来的resultCode，10代表更改了数据 ， 20表示删除了这张卡片
            if (resultCode == 10) {
                SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
                String sql = "update sincedata set thing = ? ," +
                        "days = ? ,time = ? where realid = ? ";
                Bundle bundle = data.getExtras();
                String info = bundle.getString("value");
                int i = bundle.getInt("positi");
                list.set(list.size() - i - 1, info);
                time_list.set(list.size() - i - 1, Long.parseLong(info.split("=")[2]));


                db.execSQL(sql, new String[]{info.split("=")[0], info.split("=")[1],
                        info.split("=")[2], list.size() - i + ""});

                setReadId(db);

                gridView.setLayoutAnimation(getAnimationController());
                gridView.setAdapter(myArrayAdapter);
            } else if (resultCode == 20) {
                SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
                String sql = "delete from sincedata where realid = ?";
                Bundle bundle = data.getExtras();
                int i = bundle.getInt("positi");

                list.remove(list.size() - i - 1);
                time_list.remove(list.size() - i - 1);
                db.execSQL(sql, new String[]{list.size() - i + 1 + ""});
                if (list.size() != 0) {
                    setReadId(db);
                }
                gridView.setLayoutAnimation(getAnimationController());
                gridView.setAdapter(myArrayAdapter);
            }
        }
    }

    /**
     * 这个方法是将gridview卡片出来的动画封装起来
     *
     * @return 返回一个封装好的LayoutAnimationController对象
     */
    protected LayoutAnimationController getAnimationController() {

        //执行时间
        int duration = 700;
        AnimationSet set = new AnimationSet(true);

        //透明度变化的animation，从完全透明变成完全不透明
        Animation animation = new AlphaAnimation(0.0f, 1f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        //移动动画的animation，TranslateAnimation(float fromXDelta,
        // float toXDelta, float fromYDelta, float toYDelta)
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        //第一个参数是AnimationSet对象，第二个参数是daley时间，是浮点数所以要加f
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    /**
     * 由于数据库不会自动排序，这个方法就是人工的给数据库排序
     *
     * @param db 传入一个数据库对象
     */
    private void setReadId(SQLiteDatabase db) {
        int i = 1;
        String sql = "select * from sincedata";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        do {
            int id = cursor.getInt(0);
            String days = cursor.getString(2);
            String sql1 = "update sincedata set realid  = ? where id = ? ";
            db.execSQL(sql1, new String[]{i + "", id + ""});
            i = i + 1;
        } while (cursor.moveToNext());
        cursor.moveToFirst();
    }

    /**
     * 重写这个方法使得按下back键时防止误按
     */
    @Override
    public void onBackPressed() {

        new android.app.AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("WARNING")
                .setMessage("Are you sure to LEAVE?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    /**
     * 添加默认卡片的方法
     */
    private void addmorenCard() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String sql = "insert into sincedata( thing , days , time , realid) values (? , ? , ? , ?)";
        long now = System.currentTimeMillis();
        long set_time = 1399519835643l;
        long set_time2 = 1407813017011l;
        int num = (int) ((now - set_time) / (1000 * 60 * 60 * 24));
        int num2 = (int) ((now - set_time2) / (1000 * 60 * 60 * 24));
        String moren = "SINCE I meet you=" + num;
        String moren2 = "SINCE we are together=" + num2;
        list.add(moren);
        list.add(moren2);
        db.execSQL(sql, new String[]{"SINCE I meet you", num + "", "1399519835643", "1"});
        db.execSQL(sql, new String[]{"SINCE we are together", num2 + "", "1407813017011", "2"});

    }

    /**
     * 实现点击效果
     * @param view 实现点击效果的组件
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void rotateyAnimRun(final View view)
    {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f).setDuration(300);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setTarget(view);
        set.playTogether(alpha, scaleX, scaleY);
        set.start();
    }
}
