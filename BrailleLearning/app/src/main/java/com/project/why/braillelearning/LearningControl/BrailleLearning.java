package com.project.why.braillelearning.LearningControl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.project.why.braillelearning.BrailleInformationFactory.BrailleFactory;
import com.project.why.braillelearning.BrailleInformationFactory.BrailleInformationFactory;
import com.project.why.braillelearning.BrailleInformationFactory.GettingInformation;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.EnumConstant.ServerClient;
import com.project.why.braillelearning.LearningView.BrailleLearningViewManager;
import com.project.why.braillelearning.LearningView.Observers;
import com.project.why.braillelearning.R;

import java.util.ArrayList;

public class BrailleLearning extends Activity {
    private FingerFunction learningModule;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Menu menuName = (Menu) i.getSerializableExtra("MENUNAME");

        GettingInformation object = getBrailleInformationObject(menuName);

        ServerClient serverClient = object.getServerClientType();
        ArrayList<String> jsonFileNameArray = object.getJsonFileNameArray();
        String databaseTableName = object.getDatabaseTableName();
        BrailleLearningType brailleLearningType = object.getBrailleLearningType();
        BrailleLength brailleLength = object.gettBrailleLength();

        initBrailleModule(serverClient, jsonFileNameArray, databaseTableName, brailleLearningType); // 학습 모듈
        initBrailleView(brailleLength, brailleLearningType); // 학습 화면

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

    public void initBrailleModule(ServerClient serverClient, ArrayList<String> jsonFileNameArray, String databaseTableName, BrailleLearningType brailleLearningType){
        BrailleLearningModuleManager brailleLearningModuleManager = new BrailleLearningModuleManager(this, serverClient, jsonFileNameArray, databaseTableName, brailleLearningType);
        learningModule = brailleLearningModuleManager.getLearningModule();
    }

    public void initBrailleView(BrailleLength brailleLength, BrailleLearningType brailleLearningType){
        BrailleLearningViewManager brailleLearningViewManager = new BrailleLearningViewManager(this, brailleLength, brailleLearningType);
        Observers brailleLearningView = brailleLearningViewManager.getView();
        learningModule.addObservers(brailleLearningView);

        view = brailleLearningView.getView();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.AppBasicColor));
    }

    public GettingInformation getBrailleInformationObject(Menu menuName){
        BrailleInformationFactory brailleInformationFactory = new BrailleFactory();
        GettingInformation object = brailleInformationFactory.getInformationObject(menuName);
        return object;
    }
}
