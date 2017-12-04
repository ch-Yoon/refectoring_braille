package com.project.why.braillelearning.Menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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

public class MenuInformationActivity extends Activity {
    private final int ONE_FINGER = 1;
    private final int TWO_FINGER = 2;
    private final int THREE_FINGER = 3;
    private boolean multiTouch = false; // 멀티터치 체크 변수
    private boolean touchLock = false;
    private FingerCoordinate fingerCoordinate = new FingerCoordinate(THREE_FINGER);
    private ImageResizeModule imageResizeModule;
    private ImageView information_ImageView;
    private TimerTask aniTimerTask;
    private Timer aniTimer; // 애니메이션을 위한 시간 timer
    private TimerTask touchTimerTask;
    private Timer touchTimer;
    private final int TimerTaskTime = 700; // 0.05초
    private int aniDrawableId[];
    private int index = 0;
    private int widthSize = 0;
    private int heightSize = 0;
    private BrailleLearningType brailleLearningType;
    private MediaSoundManager mediaSoundManager = new MediaSoundManager(this);
    private MultiFinger multiFingerFunction = new MultiFinger(this);
    private int touchCount = 0;
    private int touchTimerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_information);
        Intent i = getIntent();
        brailleLearningType = (BrailleLearningType) i.getSerializableExtra("BRAILLELEARNINGTYPE");

        initImageView();
        initAniDrawableId();
        setFullScreen();
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
    }

    public void mediaStart(){
        stopMediaPlayer();
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
    protected void onPause(){
        super.onPause();
        aniTimerStop();
        touchTimerStop();
        recycleImage();
        stopMediaPlayer();
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

    public void initImageView(){ // 이미지 size setting
        information_ImageView = (ImageView) findViewById(R.id.information_imageview);
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
        int pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수

        if(pointer_Count > THREE_FINGER) // 발생된 터치 이벤트가 3개를 초과하여도 3개까지만 인식
            pointer_Count = THREE_FINGER;

        if(pointer_Count == ONE_FINGER) {
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    touchTimerStart();
                    touchCount++;
                    break;
                case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                    touchCount++;
                    if(multiTouch == true)
                        multiTouch = false;
                    else {
                        if(touchCount == 4)
                            exit(0);
                    }
                    fingerCoordinate.initialize();
                    break;
                default:
                    if(multiTouch == false) {
                        fingerCoordinate.setDownCoordinate(event, pointer_Count);
                    }
                    break;
            }
        } else if(pointer_Count > ONE_FINGER) {
            multiTouch = true;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    touchLock = false;
                    fingerCoordinate.setDownCoordinate(event, pointer_Count);
                    break;
                case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    if(touchLock == false) {
                        touchLock = true;
                        fingerCoordinate.setUpCoordinate(event, pointer_Count);

                        if (pointer_Count == TWO_FINGER)
                            twoFingerFunction();
                    }
            }
        }

        return false;
    }


    /**
     * 손가락 2개에 대한 event 함수
     * BACK(상위메뉴), RIGHT(오른쪽 화면), LEFT(왼쪽 화면), REFRESH(다시듣기)
     */
    public void twoFingerFunction() {
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

    public void touchTimerStart(){
        touchTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(touchTimerCount<1)
                            touchTimerCount++;
                        else {
                            touchCount = 0;
                            touchTimerCount = 0;
                            touchTimerStop();
                        }
                    }
                });

            }
        };

        if(touchTimer == null) {
            touchTimer = new Timer();
            touchTimer.schedule(touchTimerTask, 0, TimerTaskTime); // 0.05초의 딜레이시간
        }
    }

    public void touchTimerStop(){
        if(touchTimerTask != null){
            touchTimerTask.cancel();
            touchTimerTask = null;
        }

        if(touchTimer != null){
            touchTimer.cancel();
            touchTimer = null;
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

    public void stopMediaPlayer(){
        mediaSoundManager.stop();
    }

    public void exit(int result){
        aniTimerStop();
        touchTimerStop();
        Log.d("test","exit");
        if(result == 0) {
            if(brailleLearningType == BrailleLearningType.TUTORIAL)
                setResult(RESULT_CANCELED);
            else {
                mediaSoundManager.start(FingerFunctionType.ENTER);
                setResult(RESULT_OK);
            }
        }
        else if(result == 1)
            setResult(RESULT_CANCELED);
        finish();
    }

    public void checkSoundPlaying(){
        Log.d("test","checkSoundPlaing");
        if((mediaSoundManager.getMediaQueueSize() == 0) && (mediaSoundManager.getMediaPlaying() == false))
            exit(0);
    }
}
