package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.R;

/**
 * Created by hyuck on 2017-11-16.
 */

public class MynoteControl extends BasicControl implements Control {
    MynoteControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        super(context, jsonFileName, databaseFileName, brailleLearningType, controlListener);
    }

    /**
     * data를 새로고침하는 함수.
     * 나만의 단어장의 경우, 데이터가 없으면 접속되지 않고 바로 학습이 종료됨
     * pageNumber에 따라 점자 data를 선택함
     */
    @Override
    public void refreshData(){
        if(brailleDataArrayList.isEmpty()){
            exit();
        } else {
            if(0 <= pageNumber && pageNumber < brailleDataArrayList.size()){
                data = brailleDataArrayList.get(pageNumber);
                mediaSoundManager.start(data.getRawId());
            }
        }
    }

    /**
     * 손가락 3개 함수 재정의
     * 나만의 단어장 삭제 기능 변경
     */
    @Override
    public void threeFingerFunction() {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
        switch (type) {
            case SPEECH:
                mediaSoundManager.start(R.raw.impossble_function);
                break;
            case MYNOTE:
                brailleDataArrayList = dbManager.deleteMyNote(data.getStrBrailleMatrix());
                while(brailleDataArrayList.size() <= pageNumber)
                    pageNumber = brailleDataArrayList.size() - 1;
                nodifyObservers();
                break;
            default:
                break;
        }
    }
}