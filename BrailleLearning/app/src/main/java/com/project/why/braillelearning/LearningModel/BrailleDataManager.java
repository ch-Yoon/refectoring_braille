package com.project.why.braillelearning.LearningModel;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.MynoteDB.SingletonDB;

/**
 * Created by hyuck on 2017-09-25.
 */

/**
 * 점자 data를 불러오는 모듈들을 관리하는 manager class
 */
public class BrailleDataManager {
    private Context context;
    private Json jsonFileName;
    private Database databaseFileName;
    private BrailleLearningType brailleLearningType;

    public BrailleDataManager(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType){
        this.context = context;
        this.jsonFileName = jsonFileName;
        this.databaseFileName = databaseFileName;
        this.brailleLearningType = brailleLearningType; 
    }


    /**
     * brailleLearningType에 따라, 점자 data를 불러오는 class가 선택됨
     * @return json 점자 데이터를 읽어오는 class, database에서 점자 데이터를 읽어오는 class, server로부터 점자 데이터를 읽어오는 class
     */
    public GettingBraille getBrailleArrayList(){
        switch(brailleLearningType){
            case TUTORIAL:
                return null;
            case BASIC:
                return new JsonBrailleData(context, jsonFileName, brailleLearningType);
            case MASTER:
                return new JsonBrailleData(context, jsonFileName, brailleLearningType);
            case TRANSLATION:
                return null;
            case QUIZ:
                return new JsonBrailleData(context, jsonFileName, brailleLearningType);
            case MYNOTE:
                return SingletonDB.getInstance(context, databaseFileName);
            case TEACHER:
                return new JsonBrailleData(context, jsonFileName, brailleLearningType);
            case STUDENT:
                return null;
            default:
                return null;
        }
    }

}
