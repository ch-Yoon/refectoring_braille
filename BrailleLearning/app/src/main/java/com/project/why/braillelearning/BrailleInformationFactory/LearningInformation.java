package com.project.why.braillelearning.BrailleInformationFactory;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-20.
 */

public abstract class LearningInformation {
    protected int brailleLength; // short:0  long:1  mix:2
    protected int brailleDataType; // client:0  server:1
    protected ArrayList<String> jsonFileNameArray = new ArrayList<>();
    protected int brailleLearningType; // Tutorial, Basic, Translation, reading quiz, writing quiz, Mynote, teacher mode, student mode
    protected String databaseTableName; // braiile1 braille2 braille3

    public abstract void setBrailleLength(int length);
    public abstract void setBrailleDataType(int type);
    public abstract void setJsonFileNameArray(String jsonFileName);
    public abstract void setBrailleLearningType(int type);
    public abstract void setDatabaseTableName(String tableName);
}
