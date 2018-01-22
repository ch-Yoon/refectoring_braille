package com.project.why.braillelearning.LearningControl;

import android.view.MotionEvent;

import com.project.why.braillelearning.Global;

/**
 * Created by User on 2017-10-09.
 */

/**
 * 좌표 저장 클래스
 * 손가락 갯수만큼의 down, up좌표를 저장
 */

public class FingerCoordinate {
    private int downX[];
    private int downY[];
    private int upX[];
    private int upY[];
    private int fingerCount;

    public FingerCoordinate(int fingerCount){
        downX = new int[fingerCount]; // 손가락 2개 down 좌표
        downY = new int[fingerCount];
        upX = new int[fingerCount]; // 손가락 2개 up 좌표
        upY = new int[fingerCount];
    }


    public void setHoverDownCoordinate(MotionEvent event, int count){
        this.fingerCount = count;

        downInitialize();
        for (int i = 0 ; i<count ; i++) {
            downX[i] = (int) event.getX();
            downY[i] = (int) event.getY();
        }

        setHoverUpCoordinate(event, count);
    }

    public void setHoverUpCoordinate(MotionEvent event, int count){
        this.fingerCount = count;

        upInitialize();
        for (int i = 0 ; i<count ; i++) {
            upX[i] = (int) event.getX();
            upY[i] = (int) event.getY();
        }
    }

    public boolean checkHoverError(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(x < 0 || y < 0)
            return true;
        else {
            double tolerance = Global.DisplayX * (0.3); // 화면전환 범위는 해상도 가로축의 20%
            if(tolerance < upX[0] - x )
                return true;
            else if(tolerance < upY[0] - y )
                return true;
            else
                return false;
        }
    }

    public void setDownCoordinate(MotionEvent event, int count){
        this.fingerCount = count;

        downInitialize();
        for (int i = 0 ; i<count ; i++) {
            downX[i] = (int) event.getX(i);
            downY[i] = (int) event.getY(i);
        }
    }

    public void setUpCoordinate(MotionEvent event, int count){
        this.fingerCount = count;

        upInitialize();
        for (int i = 0 ; i<count ; i++) {
            upX[i] = (int) event.getX(i);
            upY[i] = (int) event.getY(i);
        }
    }

    public void initialize(){
        for(int i=0 ; i<downX.length ; i++){
            downX[i] = 0;
            downY[i] = 0;
            upX[i] = 0;
            upY[i] = 0;
        }

        fingerCount = 0;
    }

    public void downInitialize(){
        for(int i=0 ; i<downX.length ; i++){
            downX[i] = 0;
            downY[i] = 0;
        }
    }

    public void upInitialize(){
        for(int i=0 ; i<downX.length ; i++){
            upX[i] = 0;
            upY[i] = 0;
        }
    }

    public int getFingerCount(){
        return fingerCount;
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
