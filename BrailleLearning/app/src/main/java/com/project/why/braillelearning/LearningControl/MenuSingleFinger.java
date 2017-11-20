package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.Dot;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;

/**
 * Created by hyuck on 2017-11-03.
 */

/**
 * Menu에서 발생되는 손가락 1개 event 모듈
 */
public class MenuSingleFinger implements SingleFingerFunction {
    private MediaSoundManager mediaSoundManager;
    private double serviceArea;

    MenuSingleFinger(Context context){
        mediaSoundManager = new MediaSoundManager(context);
        serviceArea = Global.DisplayX * (0.1); // 메뉴선택 허용 범위는 해상도 가로축의 10%
    }

    @Override
    public FingerFunctionType oneFingerFunction(Dot[][] brailleMatrix, FingerCoordinate fingerCoordinate) {
        FingerFunctionType type = FingerFunctionType.NONE;

        int downX = fingerCoordinate.getDownX()[0];
        int downY = fingerCoordinate.getDownY()[0];
        int upX = fingerCoordinate.getUpX()[0];
        int upY = fingerCoordinate.getUpY()[0];

        // 해상도 가로축의 10%길이를 한 변으로 갖는 사각형이 선택 영역
        if(downX-serviceArea <= upX && upX <= downX+serviceArea && downY-serviceArea <= upY && upY <= downY+serviceArea){
            type = FingerFunctionType.ENTER;
        }

        mediaSoundManager.start(type);

        return type;
    }
}
