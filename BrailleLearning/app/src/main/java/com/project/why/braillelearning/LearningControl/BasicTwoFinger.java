package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.Global;

/**
 * Created by User on 2017-10-09.
 */

public class BasicTwoFinger implements TwoFingerFunction {
    int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber();

    @Override
    public FingerFunctionType getTwoFingerFunctionType(int downX[], int downY[], int upX[], int upY[]) {
        double Finger_gapX[] = new double[TWO_FINGER]; // 첫번째와 두번째 손가락의 downX 좌표와 upX좌표의 격차
        double Finger_gapY[] = new double[TWO_FINGER]; // 첫번째와 두번째 손가락의 downY 좌표와 upY좌표의 격차
        int Drag_countX=0; // 좌측 이동인지 우측 이동인지를 확인하기 위한 변수
        int Drag_countY=0; // 뒤로가기인지 특수기능인지를 확인하기 위한 변수

        double DragSpace = Global.DisplayX*(0.2); // 화면전환 범위는 해상도 가로축의 20%

        for(int i=0 ; i<TWO_FINGER ; i++){
            Finger_gapX[i] = downX[i] - upX[i]; //손가락 2개의 X좌표 격차
            Finger_gapY[i] = downY[i] - upY[i]; //손가락 2개의 Y좌표 격차

            if(Finger_gapX[i]>DragSpace) // x격차가 양수이면서 화면전환 허용 범위 충족시 오른쪽 화면전환 변수 값 증가
                Drag_countX++;
            else if(Finger_gapX[i]<DragSpace*(-1)) // x격차가 음수이면서 화면전환 허용 범위 충족시 왼쪽 화면 전환 변수 값 증가
                Drag_countX--;

            if(Finger_gapY[i]<DragSpace*(-1)) // y격차가 음수이면서 화면전환 허용 범위 충족시 뒤로가기 전환 변수 값 증가
                Drag_countY++; // 왼쪽 화면전환 변수 값 증가
            else if(Finger_gapY[i]>DragSpace) // y격차가 양수이면서 화면전환 허용 범위 충족시 특수기능 변수 값 증가
                Drag_countY--; // 오른쪽 화면전환 변수 값 증가
        }

        FingerFunctionType Type = getTwofingerFunctionType(Drag_countX, Drag_countY, Finger_gapX, Finger_gapY);

        return Type;
    }

    public FingerFunctionType getTwofingerFunctionType(int Drag_countX, int Drag_countY, double Finger_gapX[], double Finger_gapY[]){
        FingerFunctionType Type = FingerFunctionType.NONE;
        boolean DragX = false; // 화면 전환을 충족했다면 true
        boolean DragY = false;

        if(Drag_countX == TWO_FINGER || Drag_countX == TWO_FINGER*(-1))
            DragX = true;
        else if(Drag_countY == TWO_FINGER || Drag_countY == TWO_FINGER*(-1))
            DragY = true;

        if(DragX == false && DragY == false){ // x과 y축 모두 화면전환 조건을 충족하지 못했을 경우
            Type = FingerFunctionType.NONE;
        } else if(DragX == true && DragY == false){ // x축만 화면전환 조건을 충족하였을 경우
            if(Drag_countX > 0) // 우측 페이지 전환
                Type = FingerFunctionType.NEXT;
            else // 좌측 페이지 전환
                Type = FingerFunctionType.PREVIOUS;
        } else if(DragX == false && DragY == true){ // y축만 화면전환 조건을 충족하였을 경우
            if(Drag_countY > 0) // 특수기능
                Type = FingerFunctionType.SPECIAL;
            else // 뒤로가기
                Type = FingerFunctionType.BACK;

        } else if(DragX == true && DragY == true){ // x축 화면전환 조건과 y축 화면전환 조건 모두 충족하였을 경우 이동거리로 구분
            double gapX=0;
            double gapY=0;

            for(int i=0 ; i<TWO_FINGER ; i++){
                gapX = gapX + Finger_gapX[i];
                gapY = gapY + Finger_gapY[i];
            }

            if(gapX >= gapY){ // x축의 이동거리가 클 경우
                if(Drag_countX > 0) // 우측 페이지 전환
                    Type = FingerFunctionType.NEXT;
                else // 좌측 페이지 전환
                    Type = FingerFunctionType.PREVIOUS;
            } else if(gapY > gapX){ // y축의 이동거리가 클 경우
                if(Drag_countY > 0) // 특수기능
                    Type = FingerFunctionType.SPECIAL;
                else // 뒤로가기
                    Type = FingerFunctionType.BACK;
            }
        }

        return Type;
    }

}
