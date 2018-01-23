package com.project.why.braillelearning.LearningModel;

import com.project.why.braillelearning.Module.DataConversionModule;

/**
 * Created by hyuck on 2017-11-17.
 */

/**
 * 퀴즈를 위한 퀴즈 점자 데이터 저장 class
 */
public class QuizBrailleData extends BrailleData{
    private String quizLettername = "";
    private String quizRawId = "";

    public QuizBrailleData(BrailleData brailleData, int quizCount){
        super(brailleData.getLetterName(), brailleData.getStrBrailleMatrix(), brailleData.getBrailleMatrix(), brailleData.getAssistanceLetterName(), brailleData.getRawId());
        DataConversionModule conversionModule = new DataConversionModule();
        quizLettername = "?";
        quizRawId = conversionModule.getConversionQuizRawId(brailleData.getBrailleMatrix(), quizCount);
    }

    public String getQuizLettername(){
        return quizLettername;
    }

    public String getQuizRawId(){
        return quizRawId;
    }

}
