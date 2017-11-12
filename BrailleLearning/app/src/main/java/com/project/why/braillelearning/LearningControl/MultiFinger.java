package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;

/**
 * Created by User on 2017-10-09.
 */

public class MultiFinger implements MultiFingerFunction {
    private int FINGER;
    private MediaSoundManager mediaSoundManager;

    public MultiFinger(Context context, FingerFunctionType type){
        mediaSoundManager = new MediaSoundManager(context);
        FINGER = type.getNumber();
    }

    @Override
    public FingerFunctionType fingerFunctionType(FingerCoordinate fingerCoordinate) {
        FingerFunctionType type = FingerFunctionType.NONE;

        if(FingerFunctionType.TWO_FINGER.getNumber() <= FINGER && FINGER <= FingerFunctionType.THREE_FINGER.getNumber()) {
            double Finger_gapX[] = new double[FINGER]; // 첫번째와 두번째 손가락의 downX 좌표와 upX좌표의 격차
            double Finger_gapY[] = new double[FINGER]; // 첫번째와 두번째 손가락의 downY 좌표와 upY좌표의 격차
            int Drag_countX = 0; // 좌측 이동인지 우측 이동인지를 확인하기 위한 변수
            int Drag_countY = 0; // 뒤로가기인지 특수기능인지를 확인하기 위한 변수
            double DragSpace = Global.DisplayX * (0.2); // 화면전환 범위는 해상도 가로축의 20%
            int downX[] = fingerCoordinate.getDownX();
            int downY[] = fingerCoordinate.getDownY();
            int upX[] = fingerCoordinate.getUpX();
            int upY[] = fingerCoordinate.getUpY();

            for (int i = 0; i < FINGER; i++) {
                Finger_gapX[i] = downX[i] - upX[i]; //손가락 2개의 X좌표 격차
                Finger_gapY[i] = downY[i] - upY[i]; //손가락 2개의 Y좌표 격차

                if (Finger_gapX[i] > DragSpace) // x격차가 양수이면서 화면전환 허용 범위 충족시 오른쪽 화면전환 변수 값 증가
                    Drag_countX++;
                else if (Finger_gapX[i] < DragSpace * (-1)) // x격차가 음수이면서 화면전환 허용 범위 충족시 왼쪽 화면 전환 변수 값 증가
                    Drag_countX--;

                if (Finger_gapY[i] < DragSpace * (-1)) // y격차가 음수이면서 화면전환 허용 범위 충족시 뒤로가기 전환 변수 값 증가
                    Drag_countY++; // 왼쪽 화면전환 변수 값 증가
                else if (Finger_gapY[i] > DragSpace) // y격차가 양수이면서 화면전환 허용 범위 충족시 특수기능 변수 값 증가
                    Drag_countY--; // 오른쪽 화면전환 변수 값 증가
            }

            boolean DragX = false; // 화면 전환을 충족했다면 true
            boolean DragY = false;

            if (Drag_countX == FINGER || Drag_countX == FINGER*(-1))
                DragX = true;
            else if (Drag_countY == FINGER || Drag_countY == FINGER*(-1))
                DragY = true;

            if (FINGER == FingerFunctionType.TWO_FINGER.getNumber())
                type = getTwoFingerFunction(DragX, DragY, Drag_countX, Drag_countY, Finger_gapX, Finger_gapY);
            else if (FINGER == FingerFunctionType.THREE_FINGER.getNumber())
                type = getThreeFingerFunction(DragX, DragY, Drag_countY);
        }

        mediaSoundManager.start(type);

        return type;
    }

    public FingerFunctionType getTwoFingerFunction(boolean DragX, boolean DragY, int Drag_countX, int Drag_countY, double Finger_gapX[], double Finger_gapY[]){
        FingerFunctionType type = FingerFunctionType.NONE;

        if (DragX == false && DragY == false) { // x과 y축 모두 화면전환 조건을 충족하지 못했을 경우
            type = FingerFunctionType.NONE;
        } else if (DragX == true && DragY == false) { // x축만 화면전환 조건을 충족하였을 경우
            if (Drag_countX > 0) // 우측 페이지 전환
                type = FingerFunctionType.RIGHT;
            else // 좌측 페이지 전환
                type = FingerFunctionType.LEFT;
        } else if (DragX == false && DragY == true) { // y축만 화면전환 조건을 충족하였을 경우
            if (Drag_countY > 0) // 특수기능
                type = FingerFunctionType.REFRESH;
            else // 뒤로가기
                type = FingerFunctionType.BACK;
        } else if (DragX == true && DragY == true) { // x축 화면전환 조건과 y축 화면전환 조건 모두 충족하였을 경우 이동거리로 구분
            double gapX = 0;
            double gapY = 0;

            for (int i = 0; i < FINGER; i++) {
                gapX = gapX + Finger_gapX[i];
                gapY = gapY + Finger_gapY[i];
            }

            if (gapX >= gapY) { // x축의 이동거리가 클 경우
                if (Drag_countX > 0) // 우측 페이지 전환
                    type = FingerFunctionType.RIGHT;
                else // 좌측 페이지 전환
                    type = FingerFunctionType.LEFT;
            } else if (gapY > gapX) { // y축의 이동거리가 클 경우
                if (Drag_countY > 0) // 특수기능
                    type = FingerFunctionType.REFRESH;
                else // 뒤로가기
                    type = FingerFunctionType.BACK;
            }
        }

        return type;
    }

    public FingerFunctionType getThreeFingerFunction(boolean DragX, boolean DragY, int Drag_countY){
        FingerFunctionType type = FingerFunctionType.NONE;

        if (DragX == false && DragY == false) { // x과 y축 모두 화면전환 조건을 충족하지 못했을 경우
            type = FingerFunctionType.NONE;
        } else if (DragX == false && DragY == true) { // y축만 화면전환 조건을 충족하였을 경우
            if (Drag_countY > 0) // 특수기능
                type = FingerFunctionType.SPEECH;
            else // 뒤로가기
                type = FingerFunctionType.MYNOTE;
        }

        return type;
    }
}