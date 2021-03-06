package com.project.why.braillelearning.Loading;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import com.project.why.braillelearning.LearningView.ActivityManagerSingleton;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.Menu.MenuActivity;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.R;
import java.util.Timer;
import java.util.TimerTask;


/**
 * application 로딩화면 activity
 */
public class BrailleLearningLoading extends Activity {
    private ImageView loading_Animation_imageview; // 애니메이션 imageview
    private TimerTask loadingTimer;
    private Timer timer; // 애니메이션을 위한 시간 timer
    private int menuImageSize; // menuimagesize
    private final int TimerTaskTime = 2000;
    private ImageResizeModule imageResizeModule;
    private ActivityManagerSingleton activityManagerSingleton = ActivityManagerSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageResizeModule = new ImageResizeModule(getResources());
        activityManagerSingleton.addArrayList(this);
        setContentView(R.layout.activity_braille_learning_loading);
        loading_Animation_imageview = (ImageView) findViewById(R.id.braiilelearningloading_imageview);
        setLoadingImage();

    }


    @Override
    protected void onResume(){
        super.onResume();
        Timer_Start(); // 애니메이션 시작
    }


    @Override
    protected void onPause(){
        super.onPause();
        Timer_Stop();
        recycleImage();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            InitDisplaySize(); // 화면 해상도 구하기
            setMenuImageSize();
            setLoadingImage();
            InitFullScreen();
        }

    }


    /**
     * Display 해상도를 구하기 위한 함수
     */
    private void InitDisplaySize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Global.displayX = size.x; // Display의 가로값. Global변수에 저장하여 상시 사용
        Global.displayY = size.y; // Display의 세로값. Global변수에 저장하여 상시 사용
    }


    /**
     * 이미지 size setting 함수
     */
    private void setMenuImageSize(){
        menuImageSize = (int)(Global.displayY*0.2); // imageview의 width와 height는 2:1 비율
        loading_Animation_imageview.getLayoutParams().height = menuImageSize;
        loading_Animation_imageview.getLayoutParams().width = menuImageSize*2;
        loading_Animation_imageview.requestLayout();
    }


    /**
     * 전체화면 함수
     */
    private void InitFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /**
     * 로딩화면 시간 체크 스레드 함수
     */
    private void Timer_Start(){
        loadingTimer = new TimerTask() {
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
        timer.schedule(loadingTimer,TimerTaskTime);
    }


    /**
     * 로딩화면 시간 체크 스레드 종료 함수
     */
    private void Timer_Stop(){ // 스레드 중지
        if(timer != null){
            timer.cancel();
            timer = null;
        }

        if(loadingTimer != null){
            loadingTimer.cancel();
            loadingTimer = null;
        }
    }


    /**
     * 애니메이션 이미지 셋팅 함수
     */
    private void setLoadingImage(){
        if(loading_Animation_imageview == null)
            loading_Animation_imageview = (ImageView) findViewById(R.id.braiilelearningloading_imageview);
        loading_Animation_imageview.setImageDrawable(imageResizeModule.getDrawableImage(R.drawable.loadingimage, menuImageSize*2, menuImageSize));
    }


    /**
     * 로딩화면 종료 후 application 접속 함수
     */
    private void loadingfinish(){
        Intent i = new Intent(BrailleLearningLoading.this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        activityManagerSingleton.nowActivityFinish();
    }


    /**
     * 이미지 메모리 해제 함수
     */
    private void recycleImage(){
        if(loading_Animation_imageview != null) {
            Drawable image = loading_Animation_imageview.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            loading_Animation_imageview.setImageDrawable(null);
        }
    }
}
