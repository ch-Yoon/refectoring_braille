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
     * 화면 새로고침 함수
     * db에 저장되어 있는 점자가 없다면 종료
     */
    @Override
    public void refreshData() {
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
     * 현재화면의 점자를 삭제하는 함수
     * 삭제하고자 하는 점자 정보를 dbManager로 전달
     * 삭제 후 화면 새로고침
     */
    @Override
    public void onDeleteMynote() {
        brailleDataArrayList = dbManager.deleteMyNote(data.getStrBrailleMatrix());
        while(brailleDataArrayList.size() <= pageNumber)
            pageNumber = brailleDataArrayList.size() - 1;
        nodifyObservers();
    }
}