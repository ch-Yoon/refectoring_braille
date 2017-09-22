package com.project.why.braillelearning.Practice;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.project.why.braillelearning.BrailleInformationFactory.BrailleData;
import com.project.why.braillelearning.BrailleInformationFactory.BrailleDataManager;
import com.project.why.braillelearning.Module.FullScreenModule;
import com.project.why.braillelearning.R;

import java.util.ArrayList;

public class BrailleLearning extends Activity {
    private View decorView; // 최상단 BackgroundView
    private int uiOption;
    private BrailleLearningView view;
    private BrailleDataManager brailleDataManager;
    private ArrayList<BrailleData> BrailleDataArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitFullScreen();

        Intent i = getIntent();
        String JsonFileName = i.getStringExtra("JsonFileName");
        brailleDataManager = new BrailleDataManager(this, JsonFileName);
        BrailleDataArray.addAll(brailleDataManager.getBrailleDataArray());
        view = new BrailleLearningView(this, BrailleDataArray);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.AppBasicColor));
        setContentView(view);
    }

    public void InitFullScreen(){ // 전체화면 함수
        decorView = getWindow().getDecorView(); // 실제 윈도우의 배경 drawable을 담고있는 decorView
        FullScreenModule fullScreenModule = new FullScreenModule(this);
        uiOption = fullScreenModule.getFullScreenOption(); // decorView의 ui를 변경하기 위한 값
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
        decorView.setSystemUiVisibility(uiOption); // 전체화면 적용
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            decorView.setSystemUiVisibility(uiOption); // 전체화면 적용
        }
    }

}
