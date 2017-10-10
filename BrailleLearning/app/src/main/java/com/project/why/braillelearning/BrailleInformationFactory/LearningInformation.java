package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.ServerClient;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-20.
 */

public abstract class LearningInformation {
    protected BrailleLength brailleLength; // short:0  long:1  mix:2
    protected ServerClient serverClientType; // client:0  server:1
    protected ArrayList<String> jsonFileNameArray = new ArrayList<>();
    protected BrailleLearningType brailleLearningType; // Tutorial, Basic, Translation, reading quiz, writing quiz, Mynote, teacher mode, student mode
    protected String databaseTableName; // basic, master, communication

    public abstract void setBrailleLength(BrailleLength brailleLength);
    public abstract void setServerClientType(ServerClient serverClientType);
    public abstract void setJsonFileNameArray(Json jsonFileName);
    public abstract void setBrailleLearningType(BrailleLearningType brailleLearningType);
    public abstract void setDatabaseTableName(Database databaseTableName);
}
