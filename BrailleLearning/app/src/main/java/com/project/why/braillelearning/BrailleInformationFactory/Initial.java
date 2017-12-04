package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;


/**
 * Created by hyuck on 2017-09-20.
 */

/**
 * 초성 연습 점자 정보 class
 */
public class Initial implements GettingInformation {
    @Override
    public Json getJsonFileName() {
        return Json.INITIAL;
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
