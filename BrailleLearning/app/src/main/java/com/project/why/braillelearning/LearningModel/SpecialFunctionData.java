package com.project.why.braillelearning.LearningModel;

import com.project.why.braillelearning.EnumConstant.SpecialFunctionType;

/**
 * Created by hyuck on 2018-02-06.
 */


/**
 * 특수기능 정보 class
 * 화면에 보여질 이미지 id와 음성파일 id, 특수기능 타입 정보를 갖고있음
 */
public class SpecialFunctionData {
    private int drawableId;
    private int soundId;
    private SpecialFunctionType specialFunctionType;

    public SpecialFunctionData(int drawableId, int soundId, SpecialFunctionType specialFunctionType){
        this.drawableId = drawableId;
        this.soundId = soundId;
        this.specialFunctionType = specialFunctionType;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getSoundId() {
        return soundId;
    }

    public SpecialFunctionType getSpecialFunctionType(){
        return specialFunctionType;
    }
}
