package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * Created by hyuck on 2017-09-21.
 */


/**
 * 문장부호 연습 점자 정보 class
 */
public class Sentence implements GettingInformation {
    @Override
    public Json getJsonFileName() {
        return Json.SENTENCE;
    }

    @Override
    public BrailleLearningType getBrailleLearningType() {
        return BrailleLearningType.BASIC;
    }

    @Override
    public Database getDatabaseTableName() {
        return Database.BASIC;
    }
}