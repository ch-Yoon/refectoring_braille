package com.project.why.braillelearning.CustomTouch;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by hyuck on 2018-01-21.
 * CustomTouchEvent를 상속받은 touch class
 * 메뉴 화면과 학습화면은 손가락 1개의 action이 다르다.
 * 이에 따라, customTouchEvent를 상속받은 현재 class를 구현하여 일반버전과 시각장애인 버전의 손가락 1개 함수를 재정의한다.
 */

public class CustomLearningTouchEvent extends CustomTouchEvent {
    private CustomLearningTouchListener customLearningTouchListener;

    public CustomLearningTouchEvent(Context context, CustomLearningTouchListener customLearningTouchListener){
        super(context, customLearningTouchListener);
        this.customLearningTouchListener = customLearningTouchListener;
    }


    /**
     * 일반버전 손가락 1개 함수 재정의
     * MOVE와 DOWN의 경우 점자를 읽는 동작으로 처리
     * @param event
     */
    @Override
    protected void basicOneFingerTouch(MotionEvent event){
        if(specialFunctionState == false) {
            if(touchLock == false) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        multiFinger = false;
                        fingerCoordinate.initialize();
                        customLearningTouchListener.onOneFingerFunction(fingerCoordinate);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        multiFinger = false;
                        fingerCoordinate.setDownCoordinate(event, ONE_FINGER);
                        customLearningTouchListener.onOneFingerMoveFunction(fingerCoordinate);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (multiFinger == false) {
                            fingerCoordinate.setDownCoordinate(event, ONE_FINGER);
                            customLearningTouchListener.onOneFingerMoveFunction(fingerCoordinate);
                        }
                        break;
                }
            }
        } else
            super.basicOneFingerTouch(event);
    }


    /**
     * 시각장애인버전의 손가락 1개 함수 재정의
     * MOVE와 DOWN의 경우 점자를 읽는 동작으로 처리
     * @param event
     */
    @Override
    protected  void blindOneFIngerTouch(MotionEvent event) {
        if (specialFunctionState == false) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    multiFinger = false;
                    hoverError = false;

                    if(touchLock == false) {
                        fingerCoordinate.setDownCoordinate(event, ONE_FINGER);
                        customLearningTouchListener.onStopSound();
                        customLearningTouchListener.onOneFingerMoveFunction(fingerCoordinate);
                    }
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    fingerCoordinate.setDownCoordinate(event, ONE_FINGER);
                    customLearningTouchListener.onOneFingerMoveFunction(fingerCoordinate);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    fingerCoordinate.initialize();
                    customLearningTouchListener.onOneFingerFunction(fingerCoordinate);
                    break;
                case MotionEvent.ACTION_DOWN:
                    multiFinger = false;
                    hoverError = false;

                    customLearningTouchListener.onStopSound();
                    fingerCoordinate.setHoverDownCoordinate(event, ONE_FINGER + 1);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (hoverError == false) {
                        hoverError = fingerCoordinate.checkHoverError(event);
                        if (hoverError == false)
                            fingerCoordinate.setHoverUpCoordinate(event, ONE_FINGER + 1);
                    } else
                        break;

                    break;
                case MotionEvent.ACTION_UP:
                    if (multiFinger == false) {
                        if (hoverError == false)
                            twoFingerUp(event, ONE_FINGER + 1);
                        else {
                            customLearningTouchListener.onError();
                            hoverError = false;
                        }
                    }
                    break;
            }
        } else
            super.blindOneFIngerTouch(event);
    }
}
