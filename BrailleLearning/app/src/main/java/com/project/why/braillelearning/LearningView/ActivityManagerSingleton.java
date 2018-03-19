package com.project.why.braillelearning.LearningView;

import android.app.Activity;
import java.util.ArrayList;

/**
 * Created by hyuck on 2018-01-30.
 */


/**
 * activity 정보들을 유지하는 activityManagerSingleton
 */
public class ActivityManagerSingleton {
    private ArrayList<Activity> activityArrayList = new ArrayList<>();
    private static final ActivityManagerSingleton ourInstance = new ActivityManagerSingleton();

    public static ActivityManagerSingleton getInstance() {
        return ourInstance;
    }

    private ActivityManagerSingleton() {
    }


    /**
     * arraylist에 새로운 activity를 추가
     * @param activity : 새롭게 생성된 activity
     */
    public void addArrayList(Activity activity){
        activityArrayList.add(activity);
    }


    /**
     * 가장 마지막 화면의 activity정보를 arraylist에서 삭제한다.
     */
    private void removeLast(){
        activityArrayList.remove(activityArrayList.size()-1);
    }


    /**
     * 현재 화면의 activity를 종료한다.
     * 종료 후 arraylist에서 activity정보 삭제
     */
    public void nowActivityFinish(){
        Activity activity = activityArrayList.get(activityArrayList.size()-1);
        activity.finish();
        removeLast();
    }


    /**
     * 모든 activity를 종료한다.
     */
    public void allActivityFinish(){
        int size = activityArrayList.size()-1;
        for(int i=size ; 0<=i ; i--){
            Activity activity = activityArrayList.get(i);
            activity.finish();
            removeLast();
        }
    }


    /**
     * 현재 activity정보를 get하는 함수
     * @return : 현재 activity
     */
    public Activity getNowActivity(){
        return activityArrayList.get(activityArrayList.size()-1);
    }
}
