package com.project.why.braillelearning.MynoteDB;
import android.content.Context;
import com.project.why.braillelearning.EnumConstant.Database;

/**
 * Created by Yeo on 2017-11-03.
 */

public class SingletonDB {
    private static volatile DBHelper _Instance;
     public static DBHelper getInstance(Context context, Database databaseFileName){
         if(_Instance == null){
             synchronized(DBHelper.class){
                 if(_Instance == null){
                     _Instance = new DBHelper(context, "Mynote.db", null, 1);
                 }
             }
         }
         _Instance.setContext(context);
         _Instance.setDatabaseName(databaseFileName);
         return _Instance;
     }
}
