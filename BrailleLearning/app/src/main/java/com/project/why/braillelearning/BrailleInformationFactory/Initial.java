package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;


/**
 * Created by hyuck on 2017-09-20.
 */

/**
 * 초성 연습 점자 정보 class
 */
public class Initial extends LearningInformation implements GettingInformation {
    Initial(){
        setJsonFileName(Json.INITIAL);
        setBrailleLearningType(BrailleLearningType.BASIC);
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
