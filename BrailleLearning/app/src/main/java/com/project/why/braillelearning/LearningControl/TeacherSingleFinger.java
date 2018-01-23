package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.EnumConstant.Vibrate;
import com.project.why.braillelearning.LearningModel.Dot;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yeo on 2017-11-02.
 */

/**
 * 선생님과의 대화 메뉴 중 선생님 모드 손가락 1개 함수 분석 모듈
 */
public class TeacherSingleFinger extends BasicSingleFinger {
    private TimerTask mTask;
    private Timer mTimer;
    private int threadTimeCount = 0;
    private int check_i;
    private int check_j;

    TeacherSingleFinger(Context context) {
        super(context);
        init();
    }

    @Override
    public void init(){
        super.init();
        check_i = 0;
        check_j = 0;
        threadStop();
    }


    /**
     * 손가락 1개 함수 정의
     * @param brailleMatrix : 현재 화면의 점자 data 좌표
     * @param fingerCoordinate : 손가락 1개의 좌표가 저장되어 있는 클래스
     * @return
     */
    @Override
    public Dot[][] oneFingerFunction(Dot[][] brailleMatrix, FingerCoordinate fingerCoordinate){
        super.oneFingerFunction(brailleMatrix, fingerCoordinate);
        return checkCoordinate(brailleMatrix);
    }


    /**
     * 동일한 장소에서 일정시간 대기 시 점자 돌출 유무 변경
     * @param brailleMatrix : 점자 행렬
     * @return : 변경된 점자 행렬
     */
    private Dot[][] checkCoordinate(Dot[][] brailleMatrix){
        threadMaking();
        int col = super.previous_i;
        int row = super.previous_j;
        if(check_i == col && check_j == row) {
            int dotType = brailleMatrix[col][row].getDotType();
            if (dotType != DotType.EXTERNAL_WALL.getNumber() && dotType != DotType.DEVISION_LINE.getNumber()) {
                if (threadTimeCount > 1) {
                    brailleMatrix[col][row].changeTarget();
                    boolean target = brailleMatrix[col][row].getTarget();
                    mediaSoundManager.start(dotType, target);
                    startVibrator(target);
                    init();
                    return brailleMatrix;
                }
            }
        } else
            threadStop();

        check_i = col;
        check_j = row;
        return null;
    }


    /**
     * 진동 발생 함수
     * @param target : 점자의 돌출 유무 true(돌출), false(비돌출)
     */
    private void startVibrator(boolean target){
        if (target)
            vibrator.vibrate(Vibrate.STRONG.getStrength()); // 강한 진동 발생
        else
            vibrator.vibrate(Vibrate.WEAKLY.getStrength()); // 약한 진동 발생
    }


    /**
     * 점자 입력을 위한 손가락이 다른 위치로 이동했는지 확인하는 쓰레드
     */
    private synchronized void threadMaking(){
        if(mTask == null) {
            mTask = new TimerTask() {
                @Override
                public void run() {
                    threadTimeCount++;
                }
            };
            mTimer = new Timer();
            mTimer.schedule(mTask, 1000, 1000); // 1초마다 생성
        }
    }


    /**
     * 점자 입력을 위한 손가락 위치 체크 쓰레드를 종료하는 함수
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
        threadTimeCount = 0;
    }
}
