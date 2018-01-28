package com.project.why.braillelearning.CustomTouch;

/**
 * Created by hyuck on 2018-01-28.
 */

import android.content.Context;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hyuck on 2018-01-21.
 * CustomTouchEvent를 상속받은 touch class
 * 일반 사용자의 경우 메뉴 화면과 메뉴 정보 안내 손가락 1개의 action이 다르다.
 * 이에 따라, CustomMenuInfoTouchEvent 상속받은 현재 class를 구현하여 일반버전의 손가락 1개 함수를 재정의한다.
 */

public class CustomMenuInfoTouchEvent extends CustomTouchEvent {
    private TimerTask mTask;
    private Timer mTimer;
    private int touchCount = 0;

    public CustomMenuInfoTouchEvent(Context context, CustomTouchEventListener customTouchEventListener){
        super(context, customTouchEventListener);
    }

    /**
     * 일반버전 손가락 1개 함수 재정의
     * down 될때 thread start, up 될때 터치횟수 확인
     * @param event
     */
    @Override
    protected void basicOneFingerTouch(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                threadMaking();
                multiFinger = false;
                functionLock = false;
                break;
            case MotionEvent.ACTION_UP:
                multiFinger = false;
                touchCount++;
                if(touchCount == 2) {
                    threadStop();
                    customTouchEventListener.onOneFingerFunction(fingerCoordinate);
                }
                break;
        }
    }


    /**
     * 더블탭 확인을 위한 시간 스레드
     */
    private synchronized void threadMaking(){
        if(mTask == null) {
            mTask = new TimerTask() {
                @Override
                public void run() {
                    threadStop();
                }
            };
            mTimer = new Timer();
            mTimer.schedule(mTask, 1000); // 1초마다 생성
        }
    }


    /**
     * 더블 탭 확인을 위한 시간 스레드를 종료하는 함수
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

        touchCount = 0;
    }
}