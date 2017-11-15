package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * Created by hyuck on 2017-09-21.
 */


/**
 * 모음 쓰기 퀴즈 점자 정보 class
 */
public class VowelWritingQuiz extends LearningInformation implements GettingInformation {
    VowelWritingQuiz(){
        setJsonFileName(Json.VOWEL);
        setBrailleLearningType(BrailleLearningType.WRITING_QUIZ);
        setDatabaseTableName(Database.BASIC);
    }

    @Override
    public Json getJsonFileName() {
        return jsonFileName;
    }

    @Override
    public BrailleLearningType getBrailleLearningType() {
        return brailleLearningType;
    }

    @Override
    public Database getDatabaseTableName() {
        return databaseTableName;
    }

    @Override
    public void setJsonFileName(Json jsonFileName){
        if(jsonFileName != null)
            this.jsonFileName = jsonFileName;
        else
            this.jsonFileName = null;
    }

    @Override
    public void setBrailleLearningType(BrailleLearningType brailleLearningType) {
        this.brailleLearningType = brailleLearningType;
    }

    @Override
    public void setDatabaseTableName(Database databaseTableName) {
        this.databaseTableName = databaseTableName;
    }
}
