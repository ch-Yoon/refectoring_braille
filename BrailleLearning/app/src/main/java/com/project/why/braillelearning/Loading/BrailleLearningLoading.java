package com.project.why.braillelearning.Loading;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.Menu.MenuActivity;
import com.project.why.braillelearning.Module.FullScreenModule;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.Accessibility.AccessibilityCheckService;
import com.project.why.braillelearning.R;

import java.util.Timer;
import java.util.TimerTask;

public class BrailleLearningLoading extends Activity {
    private ImageView Loading_Animation_imageview; // 애니메이션 imageview
    private TimerTask LoadingTimer;
    private Timer timer; // 애니메이션을 위한 시간 timer
    private int MenuImageSize; // menuimagesize
    private final int TimerTaskTime = 2000; // 0.05초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitFullScreen();
        setContentView(R.layout.activity_braille_learning_loading);
        Loading_Animation_imageview = (ImageView) findViewById(R.id.braiilelearningloading_imageview);
        setLoadingImage();
    }


    @Override
    protected void onResume(){
        super.onResume();
        Timer_Start(); // 애니메이션 시작
        Log.d("onResume","onResume");
    }


    @Override
    protected void onPause(){
        super.onPause();
        Timer_Stop();
        recycleImage();
        Log.d("onPause","onPause");
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            InitDisplaySize(); // 화면 해상도 구하기
            setMenuImageSize();
            setLoadingImage();
        }

    }


    public void InitDisplaySize(){  // Display 해상도를 구하기 위한 메소드
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Global.DisplayX = size.x; // Display의 가로값. Global변수에 저장하여 상시 사용
        Global.DisplayY = size.y; // Display의 세로값. Global변수에 저장하여 상시 사용
    }


    public void setMenuImageSize(){ // 이미지 size setting
        MenuImageSize = (int)(Global.DisplayY*0.4); // imageview의 width와 height는 세로 높이의 80%
        Loading_Animation_imageview.getLayoutParams().height = MenuImageSize;
        Loading_Animation_imageview.getLayoutParams().width = MenuImageSize;
        Loading_Animation_imageview.requestLayout();
    }


    public void InitFullScreen(){ // 전체화면 함수
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public void Timer_Start(){ //1초의 딜레이 시간을 갖는 함수
        LoadingTimer = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Timer_Stop();
                        loadingfinish();
                    }
                });

            }
        };
        timer = new Timer();
        timer.schedule(LoadingTimer,TimerTaskTime); // 0.05초의 딜레이시간
    }


    public void Timer_Stop(){ // 스레드 중지
        if(timer != null){
            timer.cancel();
            timer = null;
        }

        if(LoadingTimer != null){
            LoadingTimer.cancel();
            LoadingTimer = null;
        }
    }


    public void setLoadingImage(){ // 애니메이션 이미지 셋팅 함수
        Loading_Animation_imageview = (ImageView) findViewById(R.id.braiilelearningloading_imageview);
        Loading_Animation_imageview.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.loadingimage));
    }


    public void loadingfinish(){
        Intent i = new Intent(BrailleLearningLoading.this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        finish();
    }


    public void recycleImage(){     //이미지 메모리 해제 함수
        if(Loading_Animation_imageview != null) {
            Drawable image = Loading_Animation_imageview.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            Loading_Animation_imageview.setImageDrawable(null);
        }
    }
}
