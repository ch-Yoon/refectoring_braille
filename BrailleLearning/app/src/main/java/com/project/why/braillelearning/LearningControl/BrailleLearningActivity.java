package com.project.why.braillelearning.LearningControl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.project.why.braillelearning.AccessibilityCheckService;
import com.project.why.braillelearning.BrailleInformationFactory.BrailleFactory;
import com.project.why.braillelearning.BrailleInformationFactory.BrailleInformationFactory;
import com.project.why.braillelearning.BrailleInformationFactory.GettingInformation;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.LearningView.BrailleLearningViewManager;
import com.project.why.braillelearning.LearningView.ViewObservers;
import com.project.why.braillelearning.Menu.MenuInformationActivity;
import com.project.why.braillelearning.R;


/**
 * 점자 학습 Activity
 * 점자 정보를 담고 있는 Object의 값들로 화면에 점자를 그리는 View와 점자 학습 Control을 결정함
 */
public class BrailleLearningActivity extends Activity implements ControlListener {
    public static final int MENU_INFO = 0;
    private GettingInformation object;
    private Control learningModule;
    private ViewObservers learningView;
    private Json jsonFileName;
    private BrailleLearningType brailleLearningType;
    private Database databaseTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("test","brailleLearningActivity oncreate");
        Intent i = getIntent();
        Menu menuName = (Menu) i.getSerializableExtra("MENUNAME");
        object = getBrailleInformationObject(menuName);
        jsonFileName = object.getJsonFileName();
        brailleLearningType =  object.getBrailleLearningType();
        databaseTableName =  object.getDatabaseTableName();
        startMenuInfo();

    }

    private void startMenuInfo(){
        Log.d("test","startMenuInfo");
        Intent i = new Intent(this, MenuInformationActivity.class);
        i.putExtra("BRAILLELEARNINGTYPE",brailleLearningType);
        startActivityForResult(i, MENU_INFO);
    }

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
        stopService(new Intent(this, AccessibilityCheckService.class));

    }

    /**
     * 화면이 다시 시작될 때, 일시정지했던 control과 view를 재가동시키는 함수
     */
    @Override
    public void onResume(){
        super.onResume();
        if(learningModule != null && learningView != null)
            learningModule.nodifyObservers();

        startService(new Intent(this, AccessibilityCheckService.class));
    }

    /**
     * 전체화면을 위한 함수
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("onwindow",hasFocus+"");
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            setFullScreen();
        }
    }

    /**
     * 전체화면 적용 함수
     */
    public void setFullScreen(){ // 전체화면 함수
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 점자 학습 모듈을 setting하는 함수
     */
    public void initBrailleControl(){
        BrailleLearningModuleManager brailleLearningModuleManager = new BrailleLearningModuleManager(this, this, jsonFileName, databaseTableName, brailleLearningType);
        learningModule = brailleLearningModuleManager.getLearningModule();
    }

    /**
     * 점자 화면 view를 setting하는 함수
     */
    public void initBrailleView(){
        BrailleLearningViewManager brailleLearningViewManager = new BrailleLearningViewManager(this, brailleLearningType);
        learningView = brailleLearningViewManager.getView();
        View view = learningView.getView();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.AppBasicColor));
        learningModule.addObservers(learningView);    // view를 learning module observer로 등록
        setContentView(view);
    }

    /**
     * menu에 따라 결정되는 점자 정보 class를 얻는 함수
     * @param menuName : 선택된 메뉴 이름
     * @return : 점자 학습 정보 class 리턴
     */
    public GettingInformation getBrailleInformationObject(Menu menuName){
        BrailleInformationFactory brailleInformationFactory = new BrailleFactory();
        GettingInformation object = brailleInformationFactory.getInformationObject(menuName);
        return object;
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
        Log.d("test","exit");
        finish();
        Log.d("test","finish");
    }
}
