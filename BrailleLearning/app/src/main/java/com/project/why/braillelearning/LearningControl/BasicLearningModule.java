package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;

import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.GettingBraille;
import com.project.why.braillelearning.LearningView.ViewObservers;
import com.project.why.braillelearning.Module.MediaPlayerModule;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BasicLearningModule implements FingerFunction {
    private final int NONE = -1; // 해당 안됨
    private final int NEXT = 0; // 다음페이지
    private final int PREVIOUS = 1; // 이전페이지
    private final int ENTER = 2; // 페이지 접속
    private final int BACK = 3; // 뒤로가기
    private final int SPECIALFUNCTION = 4;
    private final int ONE_FINGER = 1; //손가락 1개
    private final int TWO_FINGER = 2; // 손가락 2개
    private final int THREE_FINGER = 3; // 손가락 3개
    private final int MAX_FINGER = THREE_FINGER;
    private int Finger_downX[] = new int[MAX_FINGER]; // 손가락 2개 down 좌표
    private int Finger_downY[] = new int[MAX_FINGER];
    private int Finger_upX[] = new int[MAX_FINGER]; // 손가락 2개 up 좌표
    private int Finger_upY[] = new int[MAX_FINGER];
    boolean multiTouch = false;

    private Context mContext;
    private MediaPlayerModule mediaPlayerModule;
    private ViewObservers viewObservers;
    private ArrayList<BrailleData> brailleDataArrayList;
    private int pageNumber = 0;
    private BrailleMatrixTranslationModule brailleMatrixTranslationModule;
    private Data data;

    BasicLearningModule(Context context,ArrayList<GettingBraille> brailleArrayList, BrailleLength brailleLength){
        mContext = context;
        brailleDataArrayList = brailleArrayList.get(0).getBrailleDataArray();
        brailleMatrixTranslationModule = new BrailleMatrixTranslationModule(brailleLength);
        mediaPlayerModule = new MediaPlayerModule(context);
    }

    @Override
    public void addObservers(ViewObservers observers) {
        viewObservers = observers;
        initObservers();
        nodifyObservers();
    }

    @Override
    public void initObservers() {
        float bigCircle = brailleMatrixTranslationModule.getBigCircleRadius();
        float miniCircle = brailleMatrixTranslationModule.getMiniCircleRadius();

        viewObservers.initCircle(bigCircle, miniCircle);
    }

    @Override
    public void nodifyObservers() {
        if(pageNumber < brailleDataArrayList.size()) {
            brailleMatrixTranslationModule.translationBrailleMatrix(brailleDataArrayList.get(pageNumber));
            data = brailleMatrixTranslationModule.getData();
            viewObservers.nodifyBraille(data);
        }
    }

    @Override
    public boolean touchEvent(MotionEvent event) {
        int pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수

        if(pointer_Count > THREE_FINGER) // 발생된 터치 이벤트가 3개를 초과하여도 3개까지만 인식
            pointer_Count = THREE_FINGER;

        if(pointer_Count == ONE_FINGER) {
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // 손가락 1개를 화면에 터치하였을 때 발생되는 Event
                    for (int i = 0; i < pointer_Count; i++) {
                        Finger_downX[i] = (int) event.getX(i);
                        Finger_downY[i] = (int) event.getY(i);
                    }

                    oneFinegerFunction();
                    break;
                case MotionEvent.ACTION_MOVE:
                    for (int i = 0; i < pointer_Count; i++) {
                        Finger_downX[i] = (int) event.getX(i);
                        Finger_downY[i] = (int) event.getY(i);
                    }

                   // oneFinegerFunction();
                    break;
                case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                    multiTouch = false;
                    break;
            }
        } else if(pointer_Count > ONE_FINGER) {
            multiTouch = true;
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    for (int i = 0; i < pointer_Count; i++) {
                        Finger_downX[i] = (int) event.getX(i);
                        Finger_downY[i] = (int) event.getY(i);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    for(int i=0; i<pointer_Count ; i++) { // 멀티 터치 up 좌표 셋팅
                        Finger_upX[i] = (int)event.getX(i);
                        Finger_upY[i] = (int)event.getY(i);
                    }

                    if(pointer_Count == TWO_FINGER){
                        if(twoFingerFunction() == true)
                            return true;
                        else
                            nodifyObservers();
                    } else if(pointer_Count == THREE_FINGER){

                    }
                    break;
            }

        }
        return false;
    }

    @Override
    public boolean oneFinegerFunction() {
        if(multiTouch == false) {
            Coordinate coordinateXY[][] = data.getCoordinate_XY();

            int col = 3;
            int row = coordinateXY[0].length;

            float x = Finger_downX[0];
            float y = Finger_downY[0];

            if(data.checkCoordinateInside(x, y)){
                for (int i = 0; i < col; i++) {
                    for (int j = 0; j < row; j++) {
                        Coordinate targetCoordinate = coordinateXY[i][j];

                        if (targetCoordinate.checkSatisfyArea_Y(y)) {
                            if (targetCoordinate.checkSatisfyArea_X(x)) {
                                Toast.makeText(mContext, targetCoordinate.getDotType() + "", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            break;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean twoFingerFunction() {
        int Type = 0;

        double Finger_gapX[] = new double[TWO_FINGER]; // 첫번째와 두번째 손가락의 downX 좌표와 upX좌표의 격차
        double Finger_gapY[] = new double[TWO_FINGER]; // 첫번째와 두번째 손가락의 downY 좌표와 upY좌표의 격차
        int Drag_countX=0; // 좌측 이동인지 우측 이동인지를 확인하기 위한 변수
        int Drag_countY=0; // 뒤로가기인지 특수기능인지를 확인하기 위한 변수

        double DragSpace = Global.DisplayX*(0.2); // 화면전환 범위는 해상도 가로축의 20%

        for(int i=0 ; i<TWO_FINGER ; i++){
            Finger_gapX[i] = Finger_downX[i] - Finger_upX[i]; //손가락 2개의 x좌표 격차
            Finger_gapY[i] = Finger_downY[i] - Finger_upY[i]; //손가락 2개의 Y좌표 격차

            if(Finger_gapX[i]>DragSpace) // x격차가 양수이면서 화면전환 허용 범위 충족시 오른쪽 화면전환 변수 값 증가
                Drag_countX++;
            else if(Finger_gapX[i]<DragSpace*(-1)) // x격차가 음수이면서 화면전환 허용 범위 충족시 왼쪽 화면 전환 변수 값 증가
                Drag_countX--;

            if(Finger_gapY[i]<DragSpace*(-1)) // y격차가 음수이면서 화면전환 허용 범위 충족시 뒤로가기 전환 변수 값 증가
                Drag_countY++; // 왼쪽 화면전환 변수 값 증가
            else if(Finger_gapY[i]>DragSpace) // y격차가 양수이면서 화면전환 허용 범위 충족시 특수기능 변수 값 증가
                Drag_countY--; // 오른쪽 화면전환 변수 값 증가
        }

        Type = getTwofingerFunctionType(Drag_countX, Drag_countY, Finger_gapX, Finger_gapY);

        switch(Type){
            case BACK: // 상위 메뉴
                return true;
            case NEXT: // 오른쪽 메뉴
                if(pageNumber+1 < brailleDataArrayList.size()) {
                    pageNumber++;
                    mediaPlayerModule.SoundPlay(Type, NONE);
                    return false;
                } else
                    return true;
            case PREVIOUS: // 왼쪽 메뉴
                if(0 <= pageNumber-1) {
                    pageNumber--;
                    mediaPlayerModule.SoundPlay(Type, NONE);
                }
                return false;
            case SPECIALFUNCTION: // 특수기능
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean threeFingerFunction() {
        return false;
    }

    public int getTwofingerFunctionType(int Drag_countX, int Drag_countY, double Finger_gapX[], double Finger_gapY[]){
        int Type=0;
        boolean DragX = false; // 화면 전환을 충족했다면 true
        boolean DragY = false;

        if(Drag_countX == TWO_FINGER || Drag_countX == TWO_FINGER*(-1))
            DragX = true;
        else if(Drag_countY == TWO_FINGER || Drag_countY == TWO_FINGER*(-1))
            DragY = true;

        if(DragX == false && DragY == false){ // x과 y축 모두 화면전환 조건을 충족하지 못했을 경우
            Type = NONE;
        } else if(DragX == true && DragY == false){ // x축만 화면전환 조건을 충족하였을 경우
            if(Drag_countX > 0) // 우측 페이지 전환
                Type = NEXT;
            else // 좌측 페이지 전환
                Type = PREVIOUS;
        } else if(DragX == false && DragY == true){ // y축만 화면전환 조건을 충족하였을 경우
            if(Drag_countY > 0) // 특수기능
                Type = SPECIALFUNCTION;
            else // 뒤로가기
                Type = BACK;
        } else if(DragX == true && DragY == true){ // x축 화면전환 조건과 y축 화면전환 조건 모두 충족하였을 경우 이동거리로 구분
            double gapX=0;
            double gapY=0;

            for(int i=0 ; i<TWO_FINGER ; i++){
                gapX = gapX + Finger_gapX[i];
                gapY = gapY + Finger_gapY[i];
            }

            if(gapX >= gapY){ // x축의 이동거리가 클 경우
                if(Drag_countX > 0) // 우측 페이지 전환
                    Type = NEXT;
                else // 좌측 페이지 전환
                    Type = PREVIOUS;
            } else if(gapY > gapX){ // y축의 이동거리가 클 경우
                if(Drag_countY > 0) // 특수기능
                    Type = SPECIALFUNCTION;
                else // 뒤로가기
                    Type = BACK;
            }
        }

        return Type;
    }
}
