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
    private View decorView; // 최상단 BackgroundView
    private int uiOption;
    private ImageResizeModule imageResizeModule; // Image Resize Module
    private ImageView Loading_Animation_imageview; // 애니메이션 imageview
    private TimerTask LoadingTimer;
    private Timer timer; // 애니메이션을 위한 시간 timer
    private int LoadingImageId; // 애니메이션 사진 id
    private int LoadingAnimationIndex = 0; // 애니메이션을 위한 index
    private int MenuImageSize; // menuimagesize
    private boolean Blind_person = false; //토크백이 활성화 되어있을 경우 변수가 true로 바뀜
    private final int TimerTaskWaitTime = 500; // 0.5초
    private final int TimerTaskTime = 50; // 0.05초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitFullScreen();
        setContentView(R.layout.activity_braille_learning_loading);
        InitLoadingImage();
        Loading_Animation_imageview = (ImageView) findViewById(R.id.braiilelearningloading_imageview);
        setLoadingImage();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Timer_Start(); // 애니메이션 시작
        Log.d("onResume","onResume");
        //startService(new Intent(this, AccessibilityCheckService.class));
    }

    @Override
    protected void onPause(){
        super.onPause();
        Timer_Stop();
        recycleImage();
        LoadingAnimationIndex = 0; // 애니메이션을 위한 index 초기화
        Log.d("onPause","onPause");
      //  stopService(new Intent(this, AccessibilityCheckService.class));
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event){ //토크백 활성화 되었을 시 호출되는 함수
        Blind_person = super.dispatchPopulateAccessibilityEvent(event);
        return Blind_person;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            decorView.setSystemUiVisibility(uiOption); // 전체화면 적용
            InitDisplaySize(); // 화면 해상도 구하기
            setKakaoImage(); // by yeo
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
    public void setKakaoImage(){
        DisplayMetrics metrics = new DisplayMetrics();
        int density = getResources().getDisplayMetrics().densityDpi;
        System.out.println(density);
    }
    public void setMenuImageSize(){ // 이미지 size setting
        MenuImageSize = (int)(Global.DisplayY*0.4); // imageview의 width와 height는 세로 높이의 80%
        Loading_Animation_imageview.getLayoutParams().height = MenuImageSize;
        Loading_Animation_imageview.getLayoutParams().width = MenuImageSize;
        Loading_Animation_imageview.requestLayout();
    }

    public void InitLoadingImage(){ // 로딩 이미지 초기화 함수
        imageResizeModule = new ImageResizeModule(getResources());
        LoadingImageId = R.drawable.loadingimage;
    }

    public void InitFullScreen(){ // 전체화면 함수
        decorView = getWindow().getDecorView(); // 실제 윈도우의 배경 drawable을 담고있는 decorView
        FullScreenModule fullScreenModule = new FullScreenModule(this);
        uiOption = fullScreenModule.getFullScreenOption(); // decorView의 ui를 변경하기 위한 값
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
        decorView.setSystemUiVisibility(uiOption); // 전체화면 적용
    }
    public void Timer_Start(){ //1초의 딜레이 시간을 갖는 함수
        LoadingTimer = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(LoadingAnimationIndex == 30)
                            Timer_Stop();
                        else
                            LoadingAnimationIndex++;
                    }
                });

            }
        };
        timer = new Timer();
        timer.schedule(LoadingTimer,TimerTaskWaitTime,TimerTaskTime); // 0.05초의 딜레이시간
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
        Loadingfinish();
    }
    public void setLoadingImage(){ // 애니메이션 이미지 셋팅 함수
        Loading_Animation_imageview = (ImageView) findViewById(R.id.braiilelearningloading_imageview);
        Loading_Animation_imageview.setImageDrawable(ContextCompat.getDrawable(this, LoadingImageId));
    }
    public void Loadingfinish(){
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
