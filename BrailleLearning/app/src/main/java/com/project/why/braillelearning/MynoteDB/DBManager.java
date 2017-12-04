package com.project.why.braillelearning.MynoteDB;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.LearningModel.BrailleData;

import java.util.ArrayList;

/**
 * Created by Yeo on 2017-11-08.
 */

public class DBManager {
    private DBHelper dbHelper;

    public DBManager(Context context, Database databaseFileName){
        dbHelper = SingletonDB.getInstance(context, databaseFileName);
        dbHelper.setDatabaseName(databaseFileName);
    }

    public void saveMyNote(String letterName, String StrBrailleMatrix, String assistLetterName, String rawID){
        dbHelper.insert(letterName, StrBrailleMatrix, assistLetterName, rawID);
    }

    public ArrayList<BrailleData> deleteMyNote(String StrBrailleMatrix){
        ArrayList<BrailleData> brailleDataArrayList = dbHelper.delete(StrBrailleMatrix);
        return brailleDataArrayList;
    }
}
