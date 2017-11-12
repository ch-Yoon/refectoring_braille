package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * Created by hyuck on 2017-09-20.
 */


/**
 * 각 메뉴의 학습 정보를 담고있는 class들의 abstract class
 * setJsonFileName() : 점자 data를 저장하고 있는 Json file name을 setting하는 함수
 * setBrailleLearningType() : 점자의 학습 종류를 setting하는 함수
 * setDatabaseTableName() : 나만의 단어장에 저장되는 내장 database name을 setting하는 함수
 */
public abstract class LearningInformation {
    protected Json jsonFileName;
    protected BrailleLearningType brailleLearningType; // Tutorial, Basic, Translation, reading quiz, writing quiz, Mynote, teacher mode, student mode
    protected Database databaseTableName; // basic, master, communication

    public abstract void setJsonFileName(Json jsonFileName);
    public abstract void setBrailleLearningType(BrailleLearningType brailleLearningType);
    public abstract void setDatabaseTableName(Database databaseTableName);
}
