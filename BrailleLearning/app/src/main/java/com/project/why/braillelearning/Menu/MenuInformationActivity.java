package com.project.why.braillelearning.Menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static com.project.why.braillelearning.R.drawable.kakao_image;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_information);
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
        mediaStart();
        aniTimerStart(); // 애니메이션 시작
        connectTouchEvent();
        setkakaoLogo();
    }

    @Override
    protected void onPause(){
        super.onPause();
        aniTimerStop();
        recycleImage();
        stopSound();
        pauseTouchEvent();
    }
    public void setkakaoLogo(){
        logoImgaeview.setImageDrawable(ContextCompat.getDrawable(this, kakao_image));
    }
    public void setLayout(){
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
    public void mediaStart(){
        stopSound();
        switch (brailleLearningType){
            case TUTORIAL:
                mediaSoundManager.start(R.raw.tutorial_info);
                break;
            case BASIC:
                mediaSoundManager.start(R.raw.basic_info);
                break;
            case MASTER:
                mediaSoundManager.start(R.raw.master_info);
                break;
            case TRANSLATION:
                mediaSoundManager.start(R.raw.translation_info);
                break;
            case QUIZ:
                mediaSoundManager.start(R.raw.quiz_info);
                break;
            case MYNOTE:
                mediaSoundManager.start(R.raw.mynote_info);
                break;
            case TEACHER:
                mediaSoundManager.start(R.raw.teachermode_info);
                break;
            case STUDENT:
                mediaSoundManager.start(R.raw.studentmode_info);
                break;
        }
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
    public void initImageView(){ // 이미지 size setting
        information_ImageView = (ImageView) findViewById(R.id.information_imageview);
        logoImgaeview = (ImageView) findViewById(R.id.imageView_kakao);
        widthSize = (int)(Global.DisplayY*0.7); // imageview의 width는 세로 높이의 90%
        heightSize = (int)(widthSize*0.8); //imageView의 height는 width의 80%
        information_ImageView.getLayoutParams().height = heightSize;
        information_ImageView.getLayoutParams().width = widthSize;
        information_ImageView.requestLayout();
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


    public void initTouchEvent(){
        customTouchConnectListener = new CustomTouchEvent(this, this);
        connectTouchEvent();
    }

    public void connectTouchEvent(){
        customTouchConnectListener.onResume();
    }

    public void pauseTouchEvent(){
        customTouchConnectListener.onPause();
    }

    @Override
    public void onFocusRefresh() {
        layout.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
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

    public void aniTimerStart(){ //1초의 딜레이 시간을 갖는 함수
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

    public void aniTimerStop(){ // 스레드 중지
        if(aniTimerTask != null){
            aniTimerTask.cancel();
            aniTimerTask = null;
        }

        if(aniTimer != null){
            aniTimer.cancel();
            aniTimer = null;
        }
    }

    public void initAniDrawableId(){
        imageResizeModule = new ImageResizeModule(getResources());
        aniDrawableId = new int[]{R.drawable.speech0, R.drawable.speech1, R.drawable.speech2, R.drawable.speech3, R.drawable.speech4, R.drawable.speech5};
    }

    synchronized public void playAnimation(){ // 애니메이션 이미지 셋팅 함수
        if(index == aniDrawableId.length)
            index = 0;

        recycleImage();
        information_ImageView.setImageDrawable(imageResizeModule.getDrawableImage(aniDrawableId[index++], widthSize, heightSize));
    }

    public void recycleImage(){     //이미지 메모리 해제 함수
        if(information_ImageView != null) {
            Drawable image = information_ImageView.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
                image = null;
            }
            information_ImageView.setImageDrawable(null);
        }
    }

    public synchronized void exit(int result){
        if(finish == false) {
            finish = true;
            if (result == 0) {
                if (brailleLearningType == BrailleLearningType.TUTORIAL)
                    setResult(RESULT_CANCELED);
                else {
                    mediaSoundManager.start(FingerFunctionType.ENTER);
                    setResult(RESULT_OK);
                }
            } else if (result == 1)
                setResult(RESULT_CANCELED);

            aniTimerStop();
            stopSound();

            finish();
        }
    }

    public void checkSoundPlaying(){
        if(mediaSoundManager.getMenuInfoPlaying() == false && mediaSoundManager.getMediaPlaying() == false )
            exit(0);

    }
}
