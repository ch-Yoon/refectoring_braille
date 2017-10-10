package com.project.why.braillelearning.LearningView;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BrailleLearningViewManager{
    private Context context;
    private BrailleLearningType brailleLearningType;

    public BrailleLearningViewManager(Context context, BrailleLearningType brailleLearningType){
        this.context = context;
        this.brailleLearningType = brailleLearningType;
    }

    public ViewObservers getView(){
        switch(brailleLearningType){
            case BASIC:
                return new BasicView(context);
            default:
                return null;
        }
    }
}