package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.os.Vibrator;

import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Vibrate;
import com.project.why.braillelearning.LearningModel.Dot;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;

/**
 * Created by User on 2017-10-09.
 */


/**
 * 점자 학습에 사용되는 손가락 1개 event 모듈
 */
public class BrailleSingleFinger implements SingleFingerFunction {
    private Vibrator vibrator;
    private MediaSoundManager mediaSoundManager;
    private int previous_i;
    private int previous_j;

    BrailleSingleFinger(Context context){
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mediaSoundManager = new MediaSoundManager(context);
        init();
    }

    /**
     * 손가락의 직전 좌표를 초기화하는 함수
     */
    @Override
    public void init(){
        previous_i = 0;
        previous_j = 0;
    }

    /**
     * 1개의 손가락으로 점자를 읽기위한 event함수
     * @param data : 현재 화면의 점자 data
     * @param fingerCoordinate : 손가락 1개의 좌표가 저장되어 있는 클래스
     * @return null;
     */
    @Override
    public FingerFunctionType oneFingerFunction(BrailleData data, FingerCoordinate fingerCoordinate){
        if(data != null) {
            Dot brailleMatrix[][] = data.getBrailleMatrix();
            int col = brailleMatrix.length;
            int row = brailleMatrix[0].length;
            float x = fingerCoordinate.getDownX()[0];
            float y = fingerCoordinate.getDownY()[0];

            if (data.checkCoordinateInside(x, y)) {
                for (int i = 0; i < col; i++) {
                    for (int j = 0; j < row; j++) {
                        Dot targetCoordinate = brailleMatrix[i][j];
                        if (targetCoordinate.checkSatisfyArea_Y(y)) {
                            if (targetCoordinate.checkSatisfyArea_X(x)) {
                                int dotType = targetCoordinate.getDotType();

                                if (targetCoordinate.getTarget()) {
                                    if (i != previous_i || j != previous_j) { //직전 좌표와 동일하지 않으면서 target일 경우
                                        mediaSoundManager.start(dotType, true); //남성 음성으로 번호 출력
                                        vibrator.vibrate(Vibrate.STRONG.getStrength()); // 강한 진동 발생
                                    }
                                } else {
                                    if (dotType == DotType.EXTERNAL_WALL.getNumber() || dotType == DotType.DEVISION_LINE.getNumber()) { // 경고음 또는 구분선일 경우
                                        mediaSoundManager.start(dotType, false); // 효과음 및 경고음 출력
                                        vibrator.vibrate(Vibrate.WEAKLY.getStrength()); // 약한 진동 발생
                                    }
                                    else {
                                        if (i != previous_i || j != previous_j) { // 직전 좌표와 동일하지 않으면서 target이 아닐 경우
                                            mediaSoundManager.start(dotType, false); // 여성음성으로 번호 출력
                                            vibrator.vibrate(Vibrate.WEAKLY.getStrength()); // 약한 진동 발생
                                        }
                                    }
                                }
                                previous_i = i; //현재 좌표를 직전 좌표로 setting
                                previous_j = j;
                                return null;
                            }
                        } else
                            break;
                    }
                }
            } else
                stopMediaPlayer();
        }

        return null;
    }

    /**
     * 손가락 1개의 음성 정지 함수
     */
    private void stopMediaPlayer(){
        mediaSoundManager.stop();
    }
}
