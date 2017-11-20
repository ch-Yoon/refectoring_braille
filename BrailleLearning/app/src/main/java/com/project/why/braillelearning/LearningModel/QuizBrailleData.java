package com.project.why.braillelearning.LearningModel;

import com.project.why.braillelearning.Module.DataConversionModule;

/**
 * Created by hyuck on 2017-11-17.
 */

public class QuizBrailleData extends BrailleData{
    private int quizCount = 0;
    private String quizLettername = "";
    private String quizRawId = "";

    public QuizBrailleData(BrailleData brailleData, int quizCount){
        super(brailleData.getLetterName(), brailleData.getStrBrailleMatrix(), brailleData.getBrailleMatrix(), brailleData.getAssistanceLetterName(), brailleData.getRawId());
        this.quizCount = quizCount;
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
