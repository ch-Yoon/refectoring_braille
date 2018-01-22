package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * Created by hyuck on 2017-09-21.
 */

/**
 * 종성 읽기 퀴즈 점자 정보 class
 */
public class FinalQuiz implements GettingInformation {
    @Override
    public Json getJsonFileName() {
        return Json.FINAL;
    }

    @Override
    public BrailleLearningType getBrailleLearningType() {
        return BrailleLearningType.QUIZ;
    }

    @Override
    public Database getDatabaseTableName() {
        return Database.BASIC;
    }
}