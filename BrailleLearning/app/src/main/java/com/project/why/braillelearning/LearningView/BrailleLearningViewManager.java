package com.project.why.braillelearning.LearningView;

import android.content.Context;
import android.view.View;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleLearningViewManager{
    private Context context;
    private BrailleLength brailleLength;
    private BrailleLearningType brailleLearningType;
    private View view;

    public BrailleLearningViewManager(Context context, BrailleLength brailleLength, BrailleLearningType brailleLearningType){
        this.context = context;
        this.brailleLength = brailleLength;
        this.brailleLearningType = brailleLearningType;
    }

    public Observers getView(){
        switch(brailleLearningType){
            case BASIC:
                return new BasicView(context, brailleLength);
            default:
                return null;
        }
    }


}
