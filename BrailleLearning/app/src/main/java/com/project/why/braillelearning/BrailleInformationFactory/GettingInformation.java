package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;

/**
 * Created by hyuck on 2017-09-20.
 */

/**
 *  Factory pattern을 위한 interface
 *  getJsonFileName() : 점자 data를 저장하고 있는 Json file name을 얻어오는 함수
 *  getBrailleLearningType() : 점자의 학습 종류를 얻어오는 함수
 *  getDatabaseTableName() : 나만의 단어장에 저장되는 내장 database name을 얻어오는 함수
 */
public interface GettingInformation {
    Json getJsonFileName();
    BrailleLearningType getBrailleLearningType();
    Database getDatabaseTableName();
}
