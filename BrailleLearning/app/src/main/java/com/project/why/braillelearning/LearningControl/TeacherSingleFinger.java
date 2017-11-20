package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Vibrate;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.Dot;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yeo on 2017-11-02.
 */

public class TeacherSingleFinger extends BrailleSingleFinger {
    private TimerTask mTask;
    private Timer mTimer;
    private int threadTimeCount = 0;
    private int check_i;
    private int check_j;

    TeacherSingleFinger(Context context) {
        super(context);
//        init(callBackMethmod);
    }
//
//    public void init(CallBack callBackMethod){
//        check_i = 0;
//        check_j = 0;
//        this.callBackMethod = callBackMethod;
//    }

    @Override
    public FingerFunctionType oneFingerFunction(Dot[][] brailleMatrix, FingerCoordinate fingerCoordinate){
        super.oneFingerFunction(brailleMatrix, fingerCoordinate);
        //checkCoordinate(data);
        return null;
    }
    public void checkCoordinate(BrailleData data){
        if(check_i == super.previous_i && check_j == previous_j) {
            if (super.dotType != DotType.EXTERNAL_WALL.getNumber() || super.dotType != DotType.DEVISION_LINE.getNumber()){
//                threadMaking();
//                if(threadTimeCount > 2) {
//                    data.resetBrailleMatrix(previous_i, previous_j);
//                    callBackMethod.objectCallBackMethod(data);
//                    if(data.getBrailleMatrix()[previous_i][previous_j].getTarget()) {
//                        mediaSoundManager.start(dotType, true); //남성 음성으로 번호 출력
//                        vibrator.vibrate(Vibrate.STRONG.getStrength()); // 강한 진동 발생
//                    }
//                    else {
//                        mediaSoundManager.start(dotType, false); // 여성음성으로 번호 출력
//                        vibrator.vibrate(Vibrate.WEAKLY.getStrength()); // 약한 진동 발생
//                    }
//                    threadStop();
//                }
            }else
                threadStop();
        } else if(mTask != null){
            threadStop();
        }
        check_i = previous_i;
        check_j = previous_j;
        //부모의 previous i,j를 확인
    }

    private void threadMaking(){
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
    private void threadStop(){
        mTask.cancel();
        mTask = null;
        threadTimeCount = 0;
    }
}
