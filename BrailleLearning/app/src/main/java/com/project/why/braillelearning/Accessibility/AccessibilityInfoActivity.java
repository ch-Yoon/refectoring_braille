package com.project.why.braillelearning.Accessibility;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import com.project.why.braillelearning.R;
import java.util.Timer;
import java.util.TimerTask;

public class AccessibilityInfoActivity extends Activity {
    private TimerTask mTask;
    private Timer mTimer;
    private TextView guideTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_info);
        guideTextView = (TextView) findViewById(R.id.accessibility_info_textview);
        String guidText = getResources().getString(R.string.accessibility_guide);
        guideTextView.setText(guidText.replace(" ", "\u00A0"));

        guideTextView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_HOVER_ENTER:
                        focusRequest();
                        threadMaking();
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        threadStop();
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            setFullScreen();
            focusRequest();
        }
    }

    /**
     * 전체화면 함수
     * 소프트키는 숨기지 않는다.
     */
    public void setFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /**
     * 화면에 포커스를 잡아주는 함수
     */
    private void focusRequest(){
        guideTextView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
        guideTextView.requestFocus();
    }


    @Override
    protected void onResume(){
        focusRequest();
        super.onResume();
    }


    /**
     * 화면을 3초간 누르고 있는지 확인하는 함수
     * 3초 뒤에 접근성 설정 화면으로 넘어감
     */
    private synchronized void threadMaking(){
        if(mTask == null) {
            mTask = new TimerTask() {
                @Override
                public void run() {
                    threadStop();
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    finish();
                }
            };
            mTimer = new Timer();
            mTimer.schedule(mTask, 3000);
        }
    }


    /**
     * 쓰레드 종료 함수
     */
    private void threadStop(){
        if(mTask != null) {
            mTask.cancel();
            mTask = null;
        }

        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

}
