package com.project.why.braillelearning.MynoteDB;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.GettingBraille;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.R;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yeo on 2017-10-11.
 */

public class DBHelper extends SQLiteOpenHelper implements GettingBraille {
    private static final int LETTERNAME = 1, BRAILLEMATRIX = 2, ASSISTANCENAME = 3, RAWID = 4;
    private HashMap<String, BrailleData> mapDB = new HashMap<>();
    private Database databaseName ;
    private MediaSoundManager mediaSoundManager;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE BASIC (_id INTEGER PRIMARY KEY AUTOINCREMENT, LetterName TEXT, BrailleMatrix TEXT, AssistanceName TEXT, RAWID TEXT);");
        db.execSQL("CREATE TABLE MASTER (_id INTEGER PRIMARY KEY AUTOINCREMENT, LetterName TEXT, BrailleMatrix TEXT, AssistanceName TEXT, RAWID TEXT);");
        db.execSQL("CREATE TABLE COMMUNICATION (_id INTEGER PRIMARY KEY AUTOINCREMENT, LetterName TEXT, BrailleMatrix TEXT, AssistanceName TEXT, RAWID TEXT);");
    }

    public void setContext(Context context){
        mediaSoundManager = new MediaSoundManager(context);
    }


    public void setDatabaseName(Database databaseName){
        this.databaseName = databaseName;
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    /**
     * db에 data를 저장하는 함수
     * @param letterName : 점자를 의미하는 글자
     * @param brailleMatrix : 점자정보 행렬
     * @param assistanceName :  퀴즈를 위한 글자
     * @param rawId : 음성파일 정보
     */
    public void insert(String letterName, String brailleMatrix, String assistanceName, String rawId) {
        // 읽고 쓰기가 가능하게 DB 열기
        try {
            SQLiteDatabase db = getWritableDatabase();
            addHashMap(getResult()); // 해쉬맵 세팅
            String tableName = databaseName.getName();
            if (mapDB.containsKey(brailleMatrix) == true) {
                db.execSQL("DELETE FROM  " + tableName + "  WHERE BrailleMatrix ='" + brailleMatrix + "';");
                switch (tableName){
                    case "BASIC":
                        mediaSoundManager.start(R.raw.basic_resave);
                        break;
                    case "MASTER":
                        mediaSoundManager.start(R.raw.master_resave);
                        break;
                    case "COMMUNICATION":
                        mediaSoundManager.start(R.raw.communication_resave);
                        break;
                }
            } else {
                switch (tableName){
                    case "BASIC":
                        mediaSoundManager.start(R.raw.basicsave);
                        break;
                    case "MASTER":
                        mediaSoundManager.start(R.raw.mastersave);
                        break;
                    case "COMMUNICATION":
                        mediaSoundManager.start(R.raw.communicationsave);
                        break;
                }
            }

            db.execSQL("INSERT INTO " + tableName + " VALUES(null, '" + letterName + "' , '" + brailleMatrix + "' , '" + assistanceName + "', '" + rawId + "');");// DB에 입력한 값으로 행 추가
            db.close();
        } catch(Exception e){
        }
    }


    /**
     * db에 저장되어 있는 점자 삭제 함수
     * 점자 행렬을 key 값으로 찾아 삭제함
     * @param brailleMatrix : 점자 정보 행렬
     * @return : 삭제 완료 후 새로고침 된 점자 arrayList
     */
    public ArrayList<BrailleData> delete(String brailleMatrix) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            // 입력한 항목과 일치하는 행 삭제
            db.execSQL("DELETE FROM  " + databaseName.getName() + "  WHERE BrailleMatrix ='" + brailleMatrix + "';");

            db.close();
        } catch (Exception e){
        }

        ArrayList<BrailleData> brailleDataArrayList = getResult();

        if(!brailleDataArrayList.isEmpty())
            mediaSoundManager.start(R.raw.delete); //일반삭제
        else
            mediaSoundManager.start(R.raw.alldelete); //모두삭제되었을 때

        return brailleDataArrayList;
    }


    /**
     * db안에 저장되어 있는 점자들을 hashmap에 저장.
     * hashMap을 통해 이미 저장되어있는 점자인지 판별
     * @param allData
     */
    private void addHashMap(ArrayList<BrailleData> allData){ // 해쉬맵에 모든 점자정보를 저장
        mapDB.clear();

        for(int i = 0; i < allData.size(); i++)
            mapDB.put(allData.get(i).getStrBrailleMatrix(), allData.get(i));
    }


    /**
     * db에 저장되어 있는 점자 데이터들을 불러오는 함수
     * 나중에 저장된 점자들을 가장 앞쪽부터 출력될 수 있도록 거꾸로 가져옴
     * @return : 거꾸로 가져온 점자 데이터 arrayList
     */
    public ArrayList<BrailleData> getResult(){
        // 읽기가 가능하게 DB 열기
        ArrayList<BrailleData> brailleDataArrayList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM  "+ databaseName.getName(), null);

        while (cursor.moveToNext()) {
            BrailleData data = new BrailleData(cursor.getString(LETTERNAME), cursor.getString(BRAILLEMATRIX), cursor.getString(ASSISTANCENAME), cursor.getString(RAWID), databaseName);
            brailleDataArrayList.add(data);
        }

        //db 가장 아래에 있는 점자를 가장 앞쪽으로
        ArrayList<BrailleData> realDataArrayList = new ArrayList<>();
        for(int i=brailleDataArrayList.size()-1 ; 0<=i ; i--){
            realDataArrayList.add(brailleDataArrayList.get(i));
        }


        return realDataArrayList;
    }


    @Override
    public ArrayList<BrailleData> getBrailleDataArray(){
        ArrayList<BrailleData> brailleDataArrayList = getResult();
        if(brailleDataArrayList.isEmpty()) {
            mediaSoundManager.allStop();
            mediaSoundManager.start(R.raw.empty);
        }

        return brailleDataArrayList;
    }
}