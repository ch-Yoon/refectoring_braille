package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;

/**
 * Created by User on 2017-10-09.
 */

/**
 * 다중 터치 이벤트 class
 * 손가락 2개, 3개에 관한 이벤트를 분석하여 return
 */

public class MultiFinger{
    private MediaSoundManager mediaSoundManager;

    public MultiFinger(Context context){
        mediaSoundManager = new MediaSoundManager(context);
    }

    /**
     * 손가락 2개와 3개에 대한 event를 분석하는 함수
     * 좌표값이 드래그 허용범위를 충족시켜는지와 x축과 y축 중 어느 축의 드래그 값이 높은지를 판단
     * @param fingerCoordinate : 좌표값
     * @return : 이벤트 type
     */
    public FingerFunctionType getFingerFunctionType(FingerCoordinate fingerCoordinate, boolean touchLock) {
        FingerFunctionType type = FingerFunctionType.NONE;
        int fingerCount = fingerCoordinate.getFingerCount();

        if(fingerCount == FingerFunctionType.TWO_FINGER.getNumber()) {
            double Finger_gapX[] = new double[fingerCount]; // 첫번째와 두번째 손가락의 downX 좌표와 upX좌표의 격차
            double Finger_gapY[] = new double[fingerCount]; // 첫번째와 두번째 손가락의 downY 좌표와 upY좌표의 격차
            int Drag_countX = 0; // 좌측 이동인지 우측 이동인지를 확인하기 위한 변수
            int Drag_countY = 0; // 뒤로가기인지 특수기능인지를 확인하기 위한 변수
            double DragSpace = Global.DisplayX * (0.2); // 화면전환 범위는 해상도 가로축의 20%
            int downX[] = fingerCoordinate.getDownX();
            int downY[] = fingerCoordinate.getDownY();
            int upX[] = fingerCoordinate.getUpX();
            int upY[] = fingerCoordinate.getUpY();

            for (int i = 0; i < fingerCount; i++) {
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

            if (Drag_countX == fingerCount || Drag_countX == fingerCount*(-1))
                DragX = true;
            else if (Drag_countY == fingerCount || Drag_countY == fingerCount*(-1))
                DragY = true;

            if (fingerCount == FingerFunctionType.TWO_FINGER.getNumber())
                type = getTwoFingerFunction(DragX, DragY, Drag_countX, Drag_countY, Finger_gapX, Finger_gapY);
        }


        if(mediaSoundManager.checkTTSPlaying() == true) {
            if(touchLock == false)
                type = FingerFunctionType.NONE;
        }

        if(type == FingerFunctionType.BACK)
            mediaSoundManager.start(type);
        else {
            if(mediaSoundManager.getMenuInfoPlaying() == false) {
                if(touchLock == false)
                    mediaSoundManager.start(type);
            }
        }

        return type;
    }


    /**
     * 손가락 2개에 대한 event를 분석하는 함수
     * @param DragX : 좌우 드래그 범위 충족(true : 충족, false : 비충족)
     * @param DragY : 상하 드래그 범위 충족(true : 충족, false : 비충족)
     * @param Drag_countX : 양수이면 오른쪽, 음수이면 왼쪽 드래그 의미
     * @param Drag_countY : 양수이면 위에서 아래로 드래그, 음수이면 아래서 위로 드래그 의미
     * @param Finger_gapX : 좌우 드래그의 이동거리
     * @param Finger_gapY : 상하 드래그의 이동거리
     * @return : 이벤트 type
     */
    private FingerFunctionType getTwoFingerFunction(boolean DragX, boolean DragY, int Drag_countX, int Drag_countY, double Finger_gapX[], double Finger_gapY[]){
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
                type = FingerFunctionType.BACK;
            else // 뒤로가기
                type = FingerFunctionType.SPECIAL;
        } else if (DragX == true && DragY == true) { // x축 화면전환 조건과 y축 화면전환 조건 모두 충족하였을 경우 이동거리로 구분
            double gapX = 0;
            double gapY = 0;

            for (int i = 0; i < Finger_gapX.length; i++) {
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
                    type = FingerFunctionType.BACK;
                else // 뒤로가기
                    type = FingerFunctionType.SPECIAL;
            }
        }

        return type;
    }
}
