package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * Created by hyuck on 2017-09-21.
 */


/**
 * 점자번역 정보 class
 */
public class Translation implements GettingInformation {
    @Override
    public Json getJsonFileName() {
        return null;
    }

    @Override
    public BrailleLearningType getBrailleLearningType() {
        return BrailleLearningType.TRANSLATION;
    }

    @Override
    public Database getDatabaseTableName() {
        return Database.MASTER;
    }
}
