package com.project.why.braillelearning.EnumConstant;

/**
 * Created by hyuck on 2017-09-24.
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


