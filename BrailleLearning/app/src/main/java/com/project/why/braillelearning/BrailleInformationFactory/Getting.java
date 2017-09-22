package com.project.why.braillelearning.BrailleInformationFactory;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-20.
 */

public interface Getting {
    int gettBrailleLength();
    int getBrailleDataType();
    ArrayList<String> getJsonFileNameArray();
    int getBrailleLearningType();
    String getDatabaseTableName();
}
