package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.Module.BrailleTranslationModule;
import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-17.
 */

public class TranslationControl extends BasicControl implements SpeechRecognitionListener {
    private BrailleTranslationModule brailleTranslationModule;
    private SpeechRecognitionMoudle speechRecognitionMoudle;

    TranslationControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        super(context, jsonFileName, databaseFileName, brailleLearningType, controlListener);
        brailleTranslationModule = new BrailleTranslationModule(context);
        speechRecognitionMoudle = new SpeechRecognitionMoudle(context, this);
    }

    /**
     * 손가락 3개에 대한 event 함수
     * SPEECH(음성인식), MYNOTE(나만의 단어장 저장 및 삭제)
     */
    @Override
    public void threeFingerFunction() {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
        switch (type) {
            case SPEECH:
                speechRecognitionMoudle.start();
                break;
            case MYNOTE:
                dbManager.saveMyNote(data.getLetterName(), data.getStrBrailleMatrix(), data.getAssistanceLetterName(), data.getRawId());
                break;
            default:
                break;
        }
    }

    /**
     * 음성인식에 대한 callback method
     * 음성인식에 대한 ArrayList가 전달됨
     */
    @Override
    public void speechRecogntionResult(ArrayList<String> text) {
        startBrailleTranslation(text.get(0));
    }

    /**
     * 점자 번역을 위한 함수
     * 전달된 글자를 번역함
     * @param letterName : 음성인식 후 전달된 글자
     */
    public void startBrailleTranslation(String letterName){
        BrailleData translationBrailleData = brailleTranslationModule.translation(letterName);
        if(translationBrailleData != null) {
            brailleDataArrayList.add(translationBrailleData);
            nodifyObservers();
        }
        else
            mediaSoundManager.start("brailletranslation_fail");
    }
}
