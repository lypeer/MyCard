package com.example.yang.mycard;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

/**
 * Created by yang on 2015/5/16.
 */
public class AddACard extends Activity {
    private Button select;
    private Button back;
    private TextView haomany;
    private EditText thing;
    private Button save;
    private String s;
    private TextView biaozi;
    private TextView day;


    private void doAnimation(int num) {
        ShuziZengjia numAnimation = new ShuziZengjia(this, haomany, num);
        numAnimation.setDuration(Math.abs(num));
        numAnimation.startAnimation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
        setContentView(R.layout.addlayout);
        Typeface tf = Typeface.createFromAsset(AddACard.this.getAssets(),
                "fonts/Dekar Light.otf");
        Typeface tf1 = Typeface.createFromAsset(AddACard.this.getAssets(),
                "fonts/manteka.ttf");

        select = (Button) findViewById(R.id.select);
        select.setTypeface(tf);
        haomany = (TextView) findViewById(R.id.haomany);
        haomany.setTypeface(tf);
        thing = (EditText) findViewById(R.id.thing);
        thing.setTypeface(tf);
        save = (Button) findViewById(R.id.save);
        save.setTypeface(tf);
        back = (Button) findViewById(R.id.back);
        back.setTypeface(tf);
        day = (TextView)findViewById(R.id.day);
        day.setTypeface(tf);
        biaozi = (TextView)findViewById(R.id.biaozhi);
        biaozi.setTypeface(tf1);


        int duration = 800;
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(0.0f,
                0.0f,
                200, 0);
        animation.setDuration(duration);
        animation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(animation);
        select.startAnimation(set);
        save.startAnimation(set);

        haomany.requestFocus();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                new DatePickerDialog(AddACard.this,

                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(year, monthOfYear, dayOfMonth);
                                long set_time = c.getTimeInMillis();
                                s = set_time + "";
                                Log.e("AddACard" , s + "===");
                                long now_time = System.currentTimeMillis();
                                int num = (int) ((now_time - set_time) / (1000 * 60 * 60 * 24));

                                haomany.setText(num + "");
                                doAnimation(num);
                            }
                        }

                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        select.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    select.setBackgroundColor(getResources().getColor(R.color.white));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    select.setBackgroundColor(getResources().getColor(R.color.card));
                }
                return false;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rotate();

                checkSIsNull();

                Log.e("AddACard", s + "");
                Intent intent = new Intent(AddACard.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("value", thing.getText() + "=" + haomany.getText() + "=" + s);
                intent.putExtras(bundle);
                AddACard.this.setResult(5, intent);
                AddACard.this.finish();
            }
        });
        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    save.setBackgroundColor(getResources().getColor(R.color.white));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    save.setBackgroundColor(getResources().getColor(R.color.card));
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

//        thing.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(s.toString())) {
//                } else {
//
//                    final float fontScale = AddACard.this.getResources().getDisplayMetrics().scaledDensity;
//
//                    WindowManager wm = (WindowManager) AddACard.this.getSystemService(Context.WINDOW_SERVICE);
//                    int width = wm.getDefaultDisplay().getWidth() - 10;
//                    int edut_size = thing.getWidth() + 10;
//                    float textsize = (int) (thing.getTextSize() / fontScale + 0.5f);
//                    System.out.println(textsize);
//                    if (textsize >= 15) {
//                        if (edut_size >= width) {
//
//                            thing.setTextSize(textsize - 5);
//                            ss[(int) ((30 - textsize) / 5)] = s.toString().toCharArray().length - 2;
//                        }
//                        System.out.println("-----" + s.toString().toCharArray().length);
//                        if (textsize < 30) {
//                            for (int str : ss) {
//                                if (str == s.toString().toCharArray().length) {
//                                    thing.setTextSize(textsize + 5);
//                                }
//                            }
//                        }
//                    }else{
//                        Toast.makeText(AddACard.this , "Please Limit Your Input!" , Toast.LENGTH_SHORT).show();
//                        s.delete(s.toString().length() - 1, s.toString().length());
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(AddACard.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("WARNING")
                .setMessage("Do you want to save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rotate();

                        checkSIsNull();

                        Intent intent = new Intent(AddACard.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("value", thing.getText() + "=" + haomany.getText() + "=" + s);
                        intent.putExtras(bundle);
                        AddACard.this.setResult(5, intent);
                        AddACard.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rotate();

                        Intent intent = new Intent(AddACard.this, MainActivity.class);
                        startActivity(intent);
                        AddACard.this.finish();
                    }
                })
                .show();
    }

    private void rotate(){
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 45,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(100);
        animationSet.addAnimation(rotateAnimation);
        back.startAnimation(animationSet);
    }

    private void back(){
        new android.app.AlertDialog.Builder(AddACard.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("WARNING")
                .setMessage("Do you want to save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rotate();

                        checkSIsNull();

                        Intent intent = new Intent(AddACard.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("value", thing.getText() + "=" + haomany.getText() + "=" + s);
                        intent.putExtras(bundle);
                        Log.e("AddACaRD" , s);
                        AddACard.this.setResult(5, intent);
                        AddACard.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rotate();

                        Intent intent = new Intent(AddACard.this, MainActivity.class);
                        startActivity(intent);
                        AddACard.this.finish();
                    }
                })
                .show();

    }

    private void checkSIsNull(){
        if(s == null){
            s = System.currentTimeMillis() + "";
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
