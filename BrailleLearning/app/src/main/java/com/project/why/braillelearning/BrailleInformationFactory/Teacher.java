package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;


/**
 * Created by hyuck on 2017-09-21.
 */


/**
 * 선생님모드 정보 class
 * json 파일명, 학습 타입, DB 타입
 */
public class Teacher implements GettingInformation {
    @Override
    public Json getJsonFileName() {
        return Json.TEACHER;
    }

    @Override
    public BrailleLearningType getBrailleLearningType() {
        return BrailleLearningType.TEACHER;
    }

    @Override
    public Database getDatabaseTableName() {
        return Database.COMMUNICATION;
    }
}
