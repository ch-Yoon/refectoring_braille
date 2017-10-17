package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by hyuck on 2017-10-16.
 */

public class TranslationThreeFinger implements ThreeFIngerFunction{
    private BrailleTranslationLearning brailleTranslationLearning;

    TranslationThreeFinger(Context context){
        brailleTranslationLearning = new BrailleTranslationLearning(context);
    }

    @Override
    public BrailleData getThreeFingerFunctionType(int[] downX, int[] downY, int[] upX, int[] upY) {
        String letterName = "연습";
        int brailleMatrix[][] = brailleTranslationLearning.translation(letterName);
        String ttsText = brailleTranslationLearning.getTextToSpeachText(letterName, brailleMatrix);
        BrailleData brailleData = new BrailleData(letterName, brailleMatrix, null, ttsText);

        return brailleData;
    }
}
