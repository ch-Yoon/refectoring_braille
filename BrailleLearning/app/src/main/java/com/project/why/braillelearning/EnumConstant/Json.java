package com.project.why.braillelearning.EnumConstant;

/**
 * Created by hyuck on 2017-09-24.
 */

public enum Json {
    INITIAL("InitialBrailleData"), VOWEL("VowelBrailleData"), FINAL("FinalBrailleData"), NUMBER("NumberBrailleData"), ALPHABET("AlphabetBrailleData"),
    SENTENCE("SentenceBrailleData"), ABBREVIATION("AbbreviationBrailleData"), LETTER("LetterBrailleData"), WORD("WordBrailleData"), TEACHER("TeacherBrailleData");

    private String name;
    Json(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
