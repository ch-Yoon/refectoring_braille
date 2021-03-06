package com.project.why.braillelearning.LearningControl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import com.project.why.braillelearning.LearningView.ActivityManagerSingleton;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.LearningView.BrailleLearningView;
import com.project.why.braillelearning.LearningView.ViewObservers;
import com.project.why.braillelearning.Loading.BrailleLearningLoading;
import com.project.why.braillelearning.Menu.MenuInformationActivity;
import com.project.why.braillelearning.R;


/**
 * 점자 학습 Activity
 * 점자 정보를 담고 있는 Object의 값들로 화면에 점자를 그리는 View와 점자 학습 Control을 결정함
 */
public class BrailleLearningActivity extends Activity implements ControlListener {
    private static final int MENU_INFO = 0;
    private Control learningModule;
    private ViewObservers learningView;
    private Json jsonFileName;
    private BrailleLearningType brailleLearningType;
    private Database databaseTableName;
    private ActivityManagerSingleton activityManagerSingleton = ActivityManagerSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            Intent i = new Intent(this, BrailleLearningLoading.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            activityManagerSingleton.addArrayList(this);
            Intent i = getIntent();
            jsonFileName = (Json) i.getSerializableExtra("JSONFILENAME");
            brailleLearningType = (BrailleLearningType) i.getSerializableExtra("BRAILLELEARNINGTYPE");
            databaseTableName = (Database) i.getSerializableExtra("DATABASENAME");
            startMenuInfo();
        }
    }


    /**
     * 각 메뉴에 맞는 사용방법 가이드 activity 실행
     */
    private void startMenuInfo(){
        Intent i = new Intent(this, MenuInformationActivity.class);
        i.putExtra("BRAILLELEARNINGTYPE",brailleLearningType);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        startActivityForResult(i, MENU_INFO);
    }


    /**
     * 사용방법 가이드 activity 종료 후 호출 함수
     * 사용방법을 모두 들었다면 학습 view와 control 연결
     * 사용방법을 모두 듣지 않고 뒤로가기를 했다면 현재 activity 종료
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MENU_INFO) {
            if (resultCode == RESULT_OK) {
                initBrailleControl(); // 학습 모듈
                initBrailleView(); // 학습 화면
            } else if(resultCode == RESULT_CANCELED){
                exit();
            }
        }
    }


    /**
     * 화면이 일시정지 될 때, control과 view를 일시정지하는 함수
     */
    @Override
    public void onPause(){
        super.onPause();
        if(learningModule != null && learningView != null) {
            learningModule.onPause();
            learningView.onPause();
        }
    }


    /**
     * 화면이 다시 시작될 때, 일시정지했던 control과 view를 재 가동시키는 함수
     */
    @Override
    public void onResume(){
        super.onResume();
        if(learningModule != null && learningView != null)
            learningModule.nodifyObservers();
    }


    /**
     * 전체화면을 위한 함수
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            setFullScreen();
        }
    }


    /**
     * 전체화면 적용 함수
     */
    private void setFullScreen(){ // 전체화면 함수
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /**
     * 점자 학습 모듈을 setting하는 함수
     */
    private void initBrailleControl(){
        GettingLearningModule brailleLearningModuleManager = new BrailleLearningModuleFactory(this, this, jsonFileName, databaseTableName, brailleLearningType);
        learningModule = brailleLearningModuleManager.getLearningModule();
    }


    /**
     * 점자 화면 view를 setting하는 함수
     */
    private void initBrailleView(){
        BrailleLearningView basicView = new BrailleLearningView(this);
        learningView = basicView;
        learningModule.addObservers(learningView);    // view를 learning module observer로 등록
        View view = learningView.getView();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.AppBasicColor));
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });

        overridePendingTransition(R.anim.fade, R.anim.hold);
        setContentView(view);
    }


    /**
     * 발생되는 touch event를 control로 전달하는 함수
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        learningModule.touchEvent(event);
        return true;
    }


    /**
     * 학습 종료 함수
     */
    @Override
    public void exit() {
        activityManagerSingleton.nowActivityFinish();
    }


    /**
     * 뒤로가기 버튼 재정의
     */
    @Override
    public void onBackPressed() {
        exit();
    }
}
