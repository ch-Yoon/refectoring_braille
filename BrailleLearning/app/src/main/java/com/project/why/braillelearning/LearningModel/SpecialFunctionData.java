package com.project.why.braillelearning.LearningModel;

import com.project.why.braillelearning.EnumConstant.SpecialFunctionType;

/**
 * Created by hyuck on 2018-02-06.
 */

public class SpecialFunctionData {
    private int drawableId;
    private int soundId;
    private SpecialFunctionType specialFunctionType;

    SpecialFunctionData(int drawableId, int soundId, SpecialFunctionType specialFunctionType){
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
