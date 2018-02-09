package com.project.why.braillelearning.MynoteDB;

import android.content.Context;
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


    /**
     * dbHelper로 저장하려는 점자 data를 전달하는 함수
     * @param letterName :  점자를 의미하는 글자
     * @param StrBrailleMatrix : 점자행렬정보가 string형태로 구성되어 있는 점자 정보 string
     * @param assistLetterName : 퀴즈메뉴를 위한 보조 글자
     * @param rawID : 음성파일 정보
     */
    public void saveMyNote(String letterName, String StrBrailleMatrix, String assistLetterName, String rawID){
        dbHelper.insert(letterName, StrBrailleMatrix, assistLetterName, rawID);
    }


    /**
     * db에 저장되어 있는 점자 data를 삭제하기 위해 dbHelper로 현재 화면 점자 정보를 전달하는 함수
     * @param StrBrailleMatrix : 현재화면 점자 정보 string
     * @return : 삭제 완료 후 새로고침된 점자 data arrayList
     */
    public ArrayList<BrailleData> deleteMyNote(String StrBrailleMatrix){
        ArrayList<BrailleData> brailleDataArrayList = dbHelper.delete(StrBrailleMatrix);
        return brailleDataArrayList;
    }
}
