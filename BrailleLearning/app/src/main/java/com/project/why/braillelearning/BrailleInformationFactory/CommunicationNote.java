package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;


/**
 * Created by hyuck on 2017-09-21.
 */


/**
 * 선생님의 단어장 정보 class
 */
public class CommunicationNote extends LearningInformation implements GettingInformation {
    CommunicationNote(){
        setJsonFileName(null);
        setBrailleLearningType(BrailleLearningType.MYNOTE);
        setDatabaseTableName(Database.COMMUNICATION);
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
