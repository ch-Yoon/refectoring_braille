package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * 약자 및 약어 연습 점자 정보 클래스
 */


/**
 * 약자 및 약어 읽기 퀴즈 점자 정보 class
 * json 파일명, 학습 타입, DB 타입
 */
public class AbbreviationQuiz implements GettingInformation {
    @Override
    public Json getJsonFileName() {
        return Json.ABBREVIATION;
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
