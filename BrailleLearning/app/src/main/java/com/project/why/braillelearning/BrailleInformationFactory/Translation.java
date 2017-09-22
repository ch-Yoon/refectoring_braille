package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.Practice.Constant;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-21.
 */

public class Translation extends LearningInformation implements Getting {
    Translation(){
        setBrailleLength(Constant.BRAILLE_LENGTH_LONG);
        setBrailleDataType(Constant.CLIENT);
        setJsonFileNameArray(Constant.INITIAL_JSON);
        setJsonFileNameArray(Constant.VOWEL_JSON);
        setJsonFileNameArray(Constant.FINAL_JSON);
        setJsonFileNameArray(Constant.ABBREVIATION_JSON);
        setJsonFileNameArray(Constant.NUMBER_JSON);
        setJsonFileNameArray(Constant.ALPHABET_JSON);
        setBrailleLearningType(Constant.LEARNING_TRANSLATION);
        setDatabaseTableName(Constant.MASTER_DB);
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
