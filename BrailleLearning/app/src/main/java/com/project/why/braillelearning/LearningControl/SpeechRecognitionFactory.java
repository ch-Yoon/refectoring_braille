package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;

/**
 * Created by hyuck on 2017-10-31.
 */

/**
 * 음성인식을 활용하는 event 모듈을 return하는 class
 */
public class SpeechRecognitionFactory {
    private Context context;
    private BrailleLearningType brailleLearningType;

    SpeechRecognitionFactory(Context context, BrailleLearningType brailleLearningType){
        this.context = context;
        this.brailleLearningType = brailleLearningType;
    }

    /**
     * 음성인식 모듈을 return하는 함수
     * @return 점자 번역, 퀴즈, 선생님 모드 모듈 return
     */
    public SpeechRecognition getSpeechRecognitionModule() {
        switch(brailleLearningType) {
            case TRANSLATION:
                return new BrailleTranslation(context);
            default:
                return null;
        }
    }
}
