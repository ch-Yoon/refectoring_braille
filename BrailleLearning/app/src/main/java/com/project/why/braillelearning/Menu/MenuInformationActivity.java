package com.project.why.braillelearning.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.project.why.braillelearning.ActivityManagerSingleton;
import com.project.why.braillelearning.CustomTouch.CustomTouchConnectListener;
import com.project.why.braillelearning.CustomTouch.CustomTouchEvent;
import com.project.why.braillelearning.CustomTouch.CustomTouchEventListener;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningControl.FingerCoordinate;
import com.project.why.braillelearning.LearningControl.MultiFinger;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 각 메뉴 접속시 안내 멘트를 출력해주는 activity
 */
public class MenuInformationActivity extends Activity implements CustomTouchEventListener{
    private LinearLayout layout;
    private ImageResizeModule imageResizeModule;
    private ImageView information_ImageView;
    private TimerTask aniTimerTask;
    private Timer aniTimer; // 애니메이션을 위한 시간 timer
    private final int TimerTaskTime = 700; // 0.05초
    private int aniDrawableId[];
    private int index = 0;
    private int widthSize = 0;
    private int heightSize = 0;
    private BrailleLearningType brailleLearningType;
    private MediaSoundManager mediaSoundManager;
    private MultiFinger multiFingerFunction;
    private CustomTouchConnectListener customTouchConnectListener;
    private boolean finish = false;
    private ImageView logoImgaeview;
    private ActivityManagerSingleton activityManagerSingleton = ActivityManagerSingleton.getInstance();
    private ImageView menuinfofocus_ImageView;
    private AccessibilityManager accessibilityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_information);
        activityManagerSingleton.addArrayList(this);
        Intent i = getIntent();
        brailleLearningType = (BrailleLearningType) i.getSerializableExtra("BRAILLELEARNINGTYPE");
        mediaSoundManager = new MediaSoundManager(this);
        multiFingerFunction = new MultiFinger(this);
        setLayout();
        initImageView();
        initAniDrawableId();
        setFullScreen();
        initTouchEvent();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            setFullScreen();
            onFocusRefresh();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        screenAlwaysOnSetting();
        mediaStart();
        aniTimerStart(); // 애니메이션 시작
        connectTouchEvent();
        setkakaoLogo();
    }

    @Override
    protected void onPause(){
        super.onPause();
        screenAlwaysOnDisable();
        aniTimerStop();
        recycleImage();
        recylceRogo();
        stopSound();
        pauseTouchEvent();
    }

    private void screenAlwaysOnSetting(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void screenAlwaysOnDisable(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private void setkakaoLogo(){
        if(logoImgaeview == null) {
            logoImgaeview = (ImageView) findViewById(R.id.imageView_kakao);
            logoImgaeview.setImageDrawable(imageResizeModule.getDrawableImage(R.drawable.kakao_image, 408, 60)); //현재화면에 이미지 설정
        }
    }


    private void setLayout(){
        layout = (LinearLayout) findViewById(R.id.information_layout);
        layout.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });
    }


    /**
     * 메뉴 음성 가이드 출력 함수
     */
    private void mediaStart(){
        stopSound();
        mediaSoundManager.start(brailleLearningType);
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void setFullScreen(){ // 전체화면 함수
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /**
     * imageView setting 함수
     */
    private void initImageView(){ // 이미지 size setting
        information_ImageView = (ImageView) findViewById(R.id.information_imageview);
        widthSize = (int)(Global.DisplayY*0.7); // imageview의 width는 세로 높이의 90%
        heightSize = (int)(widthSize*0.8); //imageView의 height는 width의 80%
        information_ImageView.getLayoutParams().height = heightSize;
        information_ImageView.getLayoutParams().width = widthSize;
        information_ImageView.requestLayout();

        menuinfofocus_ImageView = (ImageView) findViewById(R.id.menuinfofocus_imageview);
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    /**
     * 손가락 1개, 2개, 3개에 대한 event를 처리하는 touchEvent
     * @param event : touchEvent를 관리하는 변수
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(customTouchConnectListener != null)
            customTouchConnectListener.touchEvent(event);

        return false;
    }


    private void initTouchEvent(){
        customTouchConnectListener = new CustomTouchEvent(this, this);
        connectTouchEvent();
    }

    private void connectTouchEvent(){
        customTouchConnectListener.onResume();
    }

    private void pauseTouchEvent(){
        customTouchConnectListener.onPause();
    }

    @Override
    public void onFocusRefresh() {
        focusSetting();
    }

    /**
     * focus setting 함수
     */
    private void focusSetting(){
        if(accessibilityManager.isTouchExplorationEnabled() == false) {
            Animation animation = AnimationUtils.loadAnimation(MenuInformationActivity.this, R.anim.focus_fade);
            menuinfofocus_ImageView.startAnimation(animation);
            menuinfofocus_ImageView.setBackgroundResource(R.drawable.focusborder);
            mediaSoundManager.focusSoundStart();
        } else {
            layout.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
            layout.requestFocus();
        }
    }

    @Override
    public void onStopSound() {
    }

    @Override
    public void onError() {
    }

    @Override
    public void onOneFingerFunction(FingerCoordinate fingerCoordinate) {
        exit(0);
    }

    public void stopSound(){
        mediaSoundManager.stop();
    }

    /**
     * 손가락 2개에 대한 event 함수
     * BACK(상위메뉴), RIGHT(오른쪽 화면), LEFT(왼쪽 화면), REFRESH(다시듣기)
     */
    @Override
    public void onTwoFingerFunction(FingerCoordinate fingerCoordinate) {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
        switch (type) {
            case BACK:
                exit(1);
                break;
            case REFRESH:
                mediaStart();
                break;
        }
    }

    @Override
    public void onThreeFingerFunction(FingerCoordinate fingerCoordinate) {
    }


    private void aniTimerStart(){ //1초의 딜레이 시간을 갖는 함수
        aniTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playAnimation();
                        checkSoundPlaying();
                    }
                });

            }
        };

        aniTimer = new Timer();
        aniTimer.schedule(aniTimerTask,0,TimerTaskTime); // 0.05초의 딜레이시간
    }


    private void aniTimerStop(){ // 스레드 중지
        if(aniTimerTask != null){
            aniTimerTask.cancel();
            aniTimerTask = null;
        }

        if(aniTimer != null){
            aniTimer.cancel();
            aniTimer = null;
        }
    }


    private void initAniDrawableId(){
        imageResizeModule = new ImageResizeModule(getResources());
        aniDrawableId = new int[]{R.drawable.speech0, R.drawable.speech1, R.drawable.speech2, R.drawable.speech3, R.drawable.speech4, R.drawable.speech5};
    }


    synchronized private void playAnimation(){ // 애니메이션 이미지 셋팅 함수
        if(index == aniDrawableId.length)
            index = 0;

        recycleImage();
        information_ImageView.setImageDrawable(imageResizeModule.getDrawableImage(aniDrawableId[index++], widthSize, heightSize));
    }


    private void recycleImage(){     //이미지 메모리 해제 함수
        if(information_ImageView != null) {
            Drawable image = information_ImageView.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            information_ImageView.setImageDrawable(null);
        }
    }

    private void recylceRogo(){
        if(logoImgaeview != null){
            Drawable image = logoImgaeview.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            logoImgaeview.setImageDrawable(null);
            logoImgaeview = null;
        }
    }

    private synchronized void exit(int result){
        if(finish == false) {
            finish = true;
            aniTimerStop();
            stopSound();

            if (result == 0) {
                if (brailleLearningType == BrailleLearningType.TUTORIAL) {
                    setResult(RESULT_CANCELED);
                    activityManagerSingleton.nowActivityFinish();
                    savePreferences();
                }
                else {
                    mediaSoundManager.start(FingerFunctionType.ENTER);
                    setResult(RESULT_OK);
                    activityManagerSingleton.nowActivityFinish();
                }
            } else if (result == 1) {
                if(brailleLearningType == BrailleLearningType.TUTORIAL){
                    SharedPreferences pref = getSharedPreferences("tutorial", MODE_PRIVATE);
                    String state = pref.getString("FIRST_RUN","FALSE");

                    if(state == "FALSE" || state.equals("FALSE"))
                        activityManagerSingleton.allActivityFinish();
                    else
                        activityManagerSingleton.nowActivityFinish();

                } else {
                    setResult(RESULT_CANCELED);
                    activityManagerSingleton.nowActivityFinish();
                }
            }
        }
    }

    private void savePreferences(){
        SharedPreferences pref = getSharedPreferences("tutorial", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("FIRST_RUN","TRUE");
        editor.commit();
    }

    private void checkSoundPlaying(){
        if(mediaSoundManager.getMenuInfoPlaying() == false && mediaSoundManager.getMediaPlaying() == false )
            exit(0);
    }
}
