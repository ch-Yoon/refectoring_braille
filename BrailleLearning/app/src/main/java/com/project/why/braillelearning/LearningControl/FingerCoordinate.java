package com.project.why.braillelearning.LearningControl;

import android.view.MotionEvent;

/**
 * Created by User on 2017-10-09.
 */

public class FingerCoordinate {
    private int downX[];
    private int downY[];
    private int upX[];
    private int upY[];

    public FingerCoordinate(int fingerCount){
        downX = new int[fingerCount]; // 손가락 2개 down 좌표
        downY = new int[fingerCount];
        upX = new int[fingerCount]; // 손가락 2개 up 좌표
        upY = new int[fingerCount];
    }

    public void setDownCoordinate(MotionEvent event, int count){
        for (int i = 0 ; i<count ; i++) {
            downX[i] = (int) event.getX(i);
            downY[i] = (int) event.getY(i);
        }
    }

    public void setUpCoordinate(MotionEvent event, int count){
        for (int i = 0 ; i<count ; i++) {
            upX[i] = (int) event.getX(i);
            upY[i] = (int) event.getY(i);
        }
    }

    public int[] getDownX() {
        return downX;
    }

    public int[] getDownY() {
        return downY;
    }

    public int[] getUpX() {
        return upX;
    }

    public int[] getUpY() {
        return upY;
    }


}
