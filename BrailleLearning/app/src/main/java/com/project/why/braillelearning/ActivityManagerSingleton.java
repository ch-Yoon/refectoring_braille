package com.project.why.braillelearning;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by hyuck on 2018-01-30.
 */

public class ActivityManagerSingleton {
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private static final ActivityManagerSingleton ourInstance = new ActivityManagerSingleton();

    public static ActivityManagerSingleton getInstance() {
        return ourInstance;
    }

    private ActivityManagerSingleton() {
    }

    public void addArrayList(Activity activity){
        activityArrayList.add(activity);
    }

    public void removeArrayList(){
        activityArrayList.remove(activityArrayList.size()-1);
    }

    public void allActivityFinish(){
        int size = activityArrayList.size()-1;
        for(int i=size ; 0<=i ; i--){
            Activity activity = activityArrayList.get(i);
            activity.finish();
            removeArrayList();
        }
    }
}
