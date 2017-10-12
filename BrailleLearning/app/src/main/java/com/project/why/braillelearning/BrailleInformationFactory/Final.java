package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.ServerClient;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-21.
 */

public class Final extends LearningInformation implements GettingInformation {
    Final(){
        setBrailleLength(BrailleLength.SHORT);
        setJsonFileName(Json.FINAL);
        setBrailleLearningType(BrailleLearningType.BASIC);
        setDatabaseTableName(Database.BASIC);
    }

    @Override
    public BrailleLength gettBrailleLength() {
        return brailleLength;
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
    public void setBrailleLength(BrailleLength brailleLength) {
        this.brailleLength = brailleLength;
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
