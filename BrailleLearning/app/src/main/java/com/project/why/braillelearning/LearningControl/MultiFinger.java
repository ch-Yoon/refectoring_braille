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
     * 만약 x축과 y축의 이동거리가 동일하다면 다시시도 처리
     * @param fingerCoordinate : 좌표값
     * @return : 이벤트 type
     */
    public FingerFunctionType getFingerFunctionType(FingerCoordinate fingerCoordinate, boolean touchLock) {
        FingerFunctionType type = FingerFunctionType.NONE;
        int fingerCount = fingerCoordinate.getFingerCount();

        if(fingerCount == FingerFunctionType.TWO_FINGER.getNumber()) {
            double finger_gapX[] = new double[fingerCount]; // 첫번째와 두번째 손가락의 downX 좌표와 upX좌표의 격차
            double finger_gapY[] = new double[fingerCount]; // 첫번째와 두번째 손가락의 downY 좌표와 upY좌표의 격차
            int drag_countX = 0; // 좌측 이동인지 우측 이동인지를 확인하기 위한 변수
            int drag_countY = 0; // 뒤로가기인지 특수기능인지를 확인하기 위한 변수
            double dragSpace = Global.displayX * (0.1); // 화면전환 범위는 해상도 가로축의 10%
            int downX[] = fingerCoordinate.getDownX();
            int downY[] = fingerCoordinate.getDownY();
            int upX[] = fingerCoordinate.getUpX();
            int upY[] = fingerCoordinate.getUpY();

            for (int i = 0; i < fingerCount; i++) {
                finger_gapX[i] = downX[i] - upX[i]; //손가락 2개의 X좌표 격차
                finger_gapY[i] = downY[i] - upY[i]; //손가락 2개의 Y좌표 격차

                if (finger_gapX[i] > dragSpace) // x격차가 양수이면서 화면전환 허용 범위 충족시 오른쪽 화면전환 변수 값 증가
                    drag_countX++;
                else if (finger_gapX[i] < dragSpace * (-1)) // x격차가 음수이면서 화면전환 허용 범위 충족시 왼쪽 화면 전환 변수 값 증가
                    drag_countX--;

                if (finger_gapY[i] < dragSpace * (-1)) // y격차가 음수이면서 화면전환 허용 범위 충족시 뒤로가기 전환 변수 값 증가
                    drag_countY++; // 왼쪽 화면전환 변수 값 증가
                else if (finger_gapY[i] > dragSpace) // y격차가 양수이면서 화면전환 허용 범위 충족시 특수기능 변수 값 증가
                    drag_countY--; // 오른쪽 화면전환 변수 값 증가
            }

            boolean DragX = false; // 화면 전환을 충족했다면 true
            boolean DragY = false;

            if (drag_countX == fingerCount || drag_countX == fingerCount*(-1))
                DragX = true;
            else if (drag_countY == fingerCount || drag_countY == fingerCount*(-1))
                DragY = true;

            if (fingerCount == FingerFunctionType.TWO_FINGER.getNumber()) {
                if (DragX == false && DragY == false) { // x과 y축 모두 화면전환 조건을 충족하지 못했을 경우
                    type = FingerFunctionType.NONE;
                } else if (DragX == true && DragY == false) { // x축만 화면전환 조건을 충족하였을 경우
                    if (drag_countX > 0) // 우측 페이지 전환
                        type = FingerFunctionType.RIGHT;
                    else // 좌측 페이지 전환
                        type = FingerFunctionType.LEFT;
                } else if (DragX == false && DragY == true) { // y축만 화면전환 조건을 충족하였을 경우
                    if (drag_countY > 0) // 특수기능
                        type = FingerFunctionType.BACK;
                    else // 뒤로가기
                        type = FingerFunctionType.SPECIAL;
                } else if (DragX == true && DragY == true) { // x축 화면전환 조건과 y축 화면전환 조건 모두 충족하였을 경우 이동거리로 구분
                    double gapX = 0;
                    double gapY = 0;

                    for (int i = 0; i < finger_gapX.length; i++) {
                        gapX = gapX + finger_gapX[i];
                        gapY = gapY + finger_gapY[i];
                    }

                    if (gapX > gapY) { // x축의 이동거리가 클 경우
                        if (drag_countX > 0) // 우측 페이지 전환
                            type = FingerFunctionType.RIGHT;
                        else // 좌측 페이지 전환
                            type = FingerFunctionType.LEFT;
                    } else if (gapY > gapX) { // y축의 이동거리가 클 경우
                        if (drag_countY > 0) // 특수기능
                            type = FingerFunctionType.BACK;
                        else // 뒤로가기
                            type = FingerFunctionType.SPECIAL;
                    } else
                        type = FingerFunctionType.NONE;
                }
            }
        }


        if(mediaSoundManager.checkTTSPlaying() == true)
           type = FingerFunctionType.NONE;
        else {
            if (type == FingerFunctionType.BACK)
                mediaSoundManager.start(type);
            else {
                if (mediaSoundManager.getMenuInfoPlaying() == false) {
                    if (touchLock == false)
                        mediaSoundManager.start(type);
                }
            }
        }

        return type;
    }
}
