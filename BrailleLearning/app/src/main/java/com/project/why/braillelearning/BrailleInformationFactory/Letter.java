package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;


/**
 * Created by hyuck on 2017-09-21.
 */

/**
 * 글자 연습 점자 정보 class
 * json 파일명, 학습 타입, DB 타입
 */
public class Letter implements GettingInformation {
    @Override
    public Json getJsonFileName() {
        return Json.LETTER;
    }

    @Override
    public BrailleLearningType getBrailleLearningType() {
        return BrailleLearningType.MASTER;
    }

    @Override
    public Database getDatabaseTableName() {
        return Database.MASTER;
    }
}
