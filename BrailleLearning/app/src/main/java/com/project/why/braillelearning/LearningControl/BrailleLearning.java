package com.project.why.braillelearning.LearningControl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.project.why.braillelearning.BrailleInformationFactory.BrailleFactory;
import com.project.why.braillelearning.BrailleInformationFactory.BrailleInformationFactory;
import com.project.why.braillelearning.BrailleInformationFactory.GettingInformation;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.LearningView.BrailleLearningViewManager;
import com.project.why.braillelearning.LearningView.ViewObservers;
import com.project.why.braillelearning.R;

import java.util.ArrayList;

public class BrailleLearning extends Activity {
    private GettingInformation object;
    private FingerFunction learningModule;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Menu menuName = (Menu) i.getSerializableExtra("MENUNAME");

        object = getBrailleInformationObject(menuName);
/*
        ServerClient serverClient = object.getServerClientType();
        ArrayList<String> jsonFileNameArray = object.getJsonFileNameArray();
        String databaseTableName = object.getDatabaseTableName();
        BrailleLearningType brailleLearningType = object.getBrailleLearningType();
        BrailleLength brailleLength = object.gettBrailleLength();
*/
        initBrailleModule(); // 학습 모듈
        initBrailleView(); // 학습 화면

        setContentView(view);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            setFullScreen();
        }
    }

    public void setFullScreen(){ // 전체화면 함수
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void initBrailleModule(){
        BrailleLearningModuleManager brailleLearningModuleManager = new BrailleLearningModuleManager(this, object);
        learningModule = brailleLearningModuleManager.getLearningModule();
    }

    public void initBrailleView(){
        BrailleLearningType brailleLearningType = object.getBrailleLearningType();
        BrailleLearningViewManager brailleLearningViewManager = new BrailleLearningViewManager(this, brailleLearningType);
        ViewObservers brailleLearningView = brailleLearningViewManager.getView();
        view = brailleLearningView.getView();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.AppBasicColor));
        learningModule.addObservers(brailleLearningView);    // view를 learning module observer로 등록
    }

    public GettingInformation getBrailleInformationObject(Menu menuName){
        BrailleInformationFactory brailleInformationFactory = new BrailleFactory();
        GettingInformation object = brailleInformationFactory.getInformationObject(menuName);
        return object;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean finish = learningModule.touchEvent(event);
        if(finish == true)
            finish();
        return true;
    }
}
