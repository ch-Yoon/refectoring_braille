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

public class SentenceWritingQuiz extends LearningInformation implements GettingInformation {
    SentenceWritingQuiz(){
        setBrailleLength(BrailleLength.SHORT);
        setServerClientType(ServerClient.CLIENT);
        setJsonFileNameArray(Json.SENTENCE);
        setBrailleLearningType(BrailleLearningType.WRITING_QUIZ);
        setDatabaseTableName(Database.BASIC);
    }

    @Override
    public BrailleLength gettBrailleLength() {
        return brailleLength;
    }

    @Override
    public ServerClient getServerClientType() {
        return serverClientType;
    }

    @Override
    public ArrayList<String> getJsonFileNameArray() {
        return jsonFileNameArray;
    }

    @Override
    public BrailleLearningType getBrailleLearningType() {
        return brailleLearningType;
    }

    @Override
    public String getDatabaseTableName() {
        return databaseTableName;
    }

    @Override
    public void setBrailleLength(BrailleLength brailleLength) {
        this.brailleLength = brailleLength;
    }

    @Override
    public void setServerClientType(ServerClient serverClientType) {
        this.serverClientType = serverClientType;
    }

    @Override
    public void setJsonFileNameArray(Json jsonFileName) {
        if(jsonFileName != null)
            jsonFileNameArray.add(jsonFileName.getName());
        else
            jsonFileNameArray = null;
    }

    @Override
    public void setBrailleLearningType(BrailleLearningType brailleLearningType) {
        this.brailleLearningType = brailleLearningType;
    }

    @Override
    public void setDatabaseTableName(Database databaseTableName) {
        this.databaseTableName = databaseTableName.getName();
    }
}
