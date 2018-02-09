package com.project.why.braillelearning.EnumConstant;

/**
 * Created by hyuck on 2017-09-24.
 */


/**
 * 기초단어장 db명 : BASIC
 * 숙련단어장 db명 : MASTER
 * 선생님의 단어장 db명 : COMMUNICATION
 */
public enum Database {
    BASIC("BASIC"), MASTER("MASTER"), COMMUNICATION("COMMUNICATION");

    private String name;

    Database(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}


