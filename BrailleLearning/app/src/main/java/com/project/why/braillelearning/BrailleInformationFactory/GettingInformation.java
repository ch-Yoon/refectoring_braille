package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.ServerClient;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-20.
 */

public interface GettingInformation {
    BrailleLength gettBrailleLength();
    Json getJsonFileName();
    BrailleLearningType getBrailleLearningType();
    Database getDatabaseTableName();
}
