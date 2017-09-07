package com.project.why.braillelearning;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {
    private View decorView; // 최상단 BackgroundView
    private int uiOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitFullScreen();
        setContentView(R.layout.activity_main);
    }

    public void InitFullScreen(){ // 전체화면 함수
        decorView = getWindow().getDecorView(); // 실제 윈도우의 배경 drawable을 담고있는 decorView
        FullScreenModule fullScreenModule = new FullScreenModule(this);
        uiOption = fullScreenModule.getFullScreenOption(); // decorView의 ui를 변경하기 위한 값
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
        decorView.setSystemUiVisibility(uiOption); // 전체화면 적용
    }


    public void InitDisplaySize(){  // Display 해상도를 구하기 위한 메소드
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Global.DisplayX = size.x; // Display의 가로값. Global변수에 저장하여 상시 사용
        Global.DisplayY = size.y; // Display의 세로값. Global변수에 저장하여 상시 사용
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            decorView.setSystemUiVisibility(uiOption); // 전체화면 적용
            InitDisplaySize(); // 화면 해상도 구하기
            Intent i = new Intent(MainActivity.this, BrailleLearningLoading.class);
            //startActivity(i);
            //overridePendingTransition(R.anim.fade, R.anim.hold);
            //finish();
            Log.d("onWindowFocus","start");
        }
    }
}
