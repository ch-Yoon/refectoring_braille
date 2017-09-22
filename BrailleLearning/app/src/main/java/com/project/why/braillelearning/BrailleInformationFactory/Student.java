package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.Practice.Constant;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-21.
 */

public class Student extends LearningInformation implements Getting {
    Student(){
        setBrailleLength(Constant.BRAILLE_LENGTH_LONG);
        setBrailleDataType(Constant.SERVER);
        setJsonFileNameArray(null);
        setBrailleLearningType(Constant.LEARNING_STUDENT);
        setDatabaseTableName(Constant.COMMUNICATION_DB);
    }

    @Override
    public void setBrailleLength(int brailleLength) {
        super.brailleLength = brailleLength;
    }

    @Override
    public void setBrailleDataType(int brailleDataType) {
        super.brailleDataType = brailleDataType;
    }

    @Override
    public void setJsonFileNameArray(String jsonFileName) {
        if(jsonFileName != null)
            jsonFileNameArray.add(jsonFileName);
        else
            jsonFileNameArray = null;
    }

    @Override
    public void setBrailleLearningType(int brailleLearningType) {
        super.brailleLearningType = brailleLearningType;
    }

    @Override
    public void setDatabaseTableName(String databaseTableName) {
        super.databaseTableName = databaseTableName;
    }

    @Override
    public int gettBrailleLength() {
        return super.brailleLength;
    }

    @Override
    public int getBrailleDataType() {
        return super.brailleDataType;
    }

    @Override
    public ArrayList<String> getJsonFileNameArray() {
        return jsonFileNameArray;
    }

    @Override
    public int getBrailleLearningType() {
        return brailleLearningType;
    }

    @Override
    public String getDatabaseTableName() {
        return databaseTableName;
    }
}