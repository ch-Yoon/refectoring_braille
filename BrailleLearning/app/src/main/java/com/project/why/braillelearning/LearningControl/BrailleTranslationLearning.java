package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.JsonBrailleData;
import com.project.why.braillelearning.Module.CustomStringTokenizer;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by hyuck on 2017-10-11.
 */

public class BrailleTranslationLearning {
    private final int INITIAL = 0;
    private final int VOWEL = 1;
    private final int FINAL = 2;
    private final int ABBREVIATION = 3;



    private final String chosung[] = {"ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ","ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
    private final String moum[] ={"ㅏ","ㅐ","ㅑ","ㅒ","ㅓ","ㅔ","ㅕ","ㅖ","ㅗ","ㅘ","ㅙ","ㅚ","ㅛ","ㅜ","ㅝ","ㅞ","ㅟ","ㅠ","ㅡ","ㅢ","ㅣ"};
    private final String jongsung[] ={"","ㄱ","ㄲ","ㄳ","ㄴ","ㄵ","ㄶ","ㄷ","ㄹ","ㄺ","ㄻ","ㄼ","ㄽ","ㄾ","ㄿ","ㅀ","ㅁ","ㅂ","ㅄ","ㅅ","ㅆ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅎ"};

    private ArrayList<BrailleData> initialBrailleDataArray = new ArrayList<>();
    private ArrayList<BrailleData> vowelBrailleDataArray = new ArrayList<>();
    private ArrayList<BrailleData> finalBrailleDataArray = new ArrayList<>();
    private ArrayList<BrailleData> abbreviationBrailleDataArray = new ArrayList<>();
    private ArrayList<String> separateArray = new ArrayList<>();
    private ArrayList<int[][]> tempMatrixArray = new ArrayList<>();
    private CustomStringTokenizer customStringTokenizer = new CustomStringTokenizer();



    BrailleTranslationLearning(Context context){
        initialBrailleDataArray = getJsonBrailleData(context, Json.INITIAL);
        vowelBrailleDataArray = getJsonBrailleData(context, Json.VOWEL);
        finalBrailleDataArray = getJsonBrailleData(context, Json.FINAL);
        abbreviationBrailleDataArray = getJsonBrailleData(context, Json.ABBREVIATION);
        initSeparateArray();
    }

    private ArrayList<BrailleData> getJsonBrailleData(Context context, Json jsonFileName){
        JsonBrailleData jsonBrailleData = new JsonBrailleData(context, jsonFileName);
        return jsonBrailleData.getBrailleDataArray();
    }

    private void initSeparateArray(){
        separateArray.clear();
        for(int i=0 ; i<abbreviationBrailleDataArray.size() ; i++){
            BrailleData brailleData = abbreviationBrailleDataArray.get(i);
            String letterName = brailleData.getLetterName();
            if(letterName.length() > 1){
                separateArray.add(letterName);
            }
        }
    }

    public int[][] translation(String text){
        tempMatrixArray.clear();
        boolean result = false;

        ArrayList<String> textArray = new ArrayList<>();
        textArray.addAll(customStringTokenizer.getSeparatorText(separateArray,text));

        for(int i=0 ; i<textArray.size() ; i++){
            String targetText = textArray.get(i);
            result = startTextToBraille(targetText);
            if(result == false)
                return null;
        }

        if(result == true)
            return setTranslationMatrix();
        else
            return null;
    }

    private boolean startTextToBraille(String targetText){
        boolean result = false;

        if(targetText.length() <= 1){
            char target = targetText.charAt(0);
            if(target>=0xAC00 && target<=0xD7A3) {       //가~힣 사이에 글자일 경우
                result = searchBrailleData(targetText, ABBREVIATION); // 한 글자 약어 탐색
                if(result == false) {
                    int finalCode = get_final(target);                                   //종성의 배열 인덱스를 산출
                    int vowelCode = get_vowel(target, finalCode);                      //모음의 배열 인덱스를 산출
                    int initCode = get_initial(target, vowelCode, finalCode);          //초성의 배열 인덱스를 산출

                    result = searchInitialAndVowel(target, finalCode); // 초성 + 모음 탐색
                    if(result == true)
                        result = searchFianal(jongsung[finalCode]); // 종성 탐색
                    else {
                        result = searchInitial(chosung[initCode]); // 초성 탐색
                        if(result == true) {
                            result = searchVowelAndFinal(finalCode, vowelCode); // o + 모음 + 종성 탐색
                            if(result == false) {
                                result = searchVowel(moum[vowelCode]);
                                if (result == true)
                                    result = searchFianal(jongsung[finalCode]);
                            }
                        }
                    }
                }
            }
        } else
            result = searchBrailleData(targetText, ABBREVIATION);

        return result;
    }

    //초성 탐색 함수
    private boolean searchInitial(String text){
        if(text == "ㅇ" || text.equals("ㅇ")) // 초성이 ㅇ일 경우는 pass
            return true;
        else
            return searchBrailleData(text, INITIAL);
    }

    //모음 탐색 함수
    private boolean searchVowel(String text){
        return searchBrailleData(text, VOWEL);
    }

    //종성 탐색 함수
    private boolean searchFianal(String text){
        int length = text.length();
        if(length == 0)
            return true;
        else
            return searchBrailleData(text, FINAL);
    }

    //초성 + 모음 탐색 함수
    private boolean searchInitialAndVowel(char target, int finalCode){
        String initialAndVowel = get_initial_vowel(target, finalCode);
        return searchBrailleData(initialAndVowel, ABBREVIATION);
    }

    // ㅇ + 모음 + 종성 탐색 함수
    private boolean searchVowelAndFinal(int finalCode, int vowelCode){
        String vowelAndFinal = get_vowel_final(finalCode, vowelCode);
        return searchBrailleData(vowelAndFinal, ABBREVIATION);
    }

    private boolean searchBrailleData(String text, int brailleType){
        ArrayList<BrailleData> brailleDataArrayList = new ArrayList<>();

        switch(brailleType){
            case INITIAL:
                brailleDataArrayList.addAll(initialBrailleDataArray);
                break;
            case VOWEL:
                brailleDataArrayList.addAll(vowelBrailleDataArray);
                break;
            case FINAL:
                brailleDataArrayList.addAll(finalBrailleDataArray);
                break;
            case ABBREVIATION:
                brailleDataArrayList.addAll(abbreviationBrailleDataArray);
                break;
            default:
                return false;
        }

        for(int i=0 ; i<brailleDataArrayList.size() ; i++){
            BrailleData brailleData = brailleDataArrayList.get(i);
            String letterName = brailleData.getLetterName();

            if(text.equals(letterName) || text==letterName) {
                tempMatrixArray.add(brailleData.getBrailleMatrix());
                return true;
            }
        }
        return false;
    }

    private int[][] setTranslationMatrix() {
        final int MAX_COL = 4;
        final int MAX_ROW = 21; // 7칸

        if(!tempMatrixArray.isEmpty()){
            int tempMatrix[][] = new int[MAX_COL][MAX_ROW];
            int progressRowIndex = 0;

            for(int i=0 ; i<tempMatrixArray.size() ; i++){
                int brailleMatrix[][] = tempMatrixArray.get(i);
                int col = brailleMatrix.length;
                int row = brailleMatrix[0].length;

                for(int j=0 ; j<row ; j++){
                    for(int k=0; k<col ; k++)
                        tempMatrix[k][progressRowIndex] = brailleMatrix[k][j];
                    progressRowIndex++;
                    if(progressRowIndex >= MAX_ROW)
                        return null;
                }
            }

            int realTranslationmatrix[][] = new int[MAX_COL][progressRowIndex];

            for(int i=0 ; i<MAX_COL ; i++){
                for(int j=0 ; j<progressRowIndex ; j++) {
                    realTranslationmatrix[i][j] = tempMatrix[i][j];
                    if ((0 < i) && (j < progressRowIndex - 1) && (realTranslationmatrix[i][j] == 7)) // 내부 구분선 지정
                        realTranslationmatrix[i][j] = 8;
                }
            }

            return realTranslationmatrix;

        } else
            return null;
    }

    public String getTextToSpeachText(String text, int brailleMatrix[][]){
        String ttsText = "번역된 단어, "+text+", ";

        int dotCount = brailleMatrix[0].length/3;
        switch(dotCount){
            case 1:
                ttsText += "한칸, ";
                break;
            case 2:
                ttsText += "두칸, ";
                break;
            case 3:
                ttsText += "세칸, ";
                break;
            case 4:
                ttsText += "네칸, ";
                break;
            case 5:
                ttsText += "다섯칸, ";
                break;
            case 6:
                ttsText += "여섯칸, ";
                break;
            case 7:
                ttsText += "일곱칸, ";
                break;
        }

        int col = brailleMatrix.length;
        int row = brailleMatrix[0].length;
        for(int i=1 ; i<row ; i++){
            for(int j=0 ; j<col ; j++){
                if(brailleMatrix[j][i] == DotType.PROJECTED.getNumber()) {
                    switch (j) {
                        case 0:
                            if (i % 3 == 0)
                                ttsText += "1 ";
                            else
                                ttsText += "4 ";
                            break;
                        case 1:
                            if (i % 3 == 0)
                                ttsText += "2 ";
                            else
                                ttsText += "5 ";
                            break;
                        case 2:
                            if (i % 3 == 0)
                                ttsText += "3 ";
                            else
                                ttsText += "6 ";
                            break;
                    }
                }
            }

            if(i % 3 == 2)
                ttsText += "점, ";

        }

        return ttsText;
    }

    //초성을 얻어오는 함수
    private int get_initial(char target, int vowel_code, int final_code){
        String uniCode = Integer.toHexString(((((target & 0xFFFF)- final_code - 0xAC00) / 28) - vowel_code) / 21);  //유니코드 변환 (초성)
        int index = decimalConversion(uniCode);
        return index;
    }

    //모음을 얻어오는 함수
    private int get_vowel(char target, int final_code){
        String uniCode = Integer.toHexString((((target & 0xFFFF)- final_code - 0xAC00) / 28) % 21);     //유니코드로 변환해줌 (모음)
        int index = decimalConversion(uniCode);
        return index;
    }

    //종성을 얻어오는 함수
    private int get_final(char target){
        String uniCode = Integer.toHexString(((target & 0xFFFF)-0xAC00) % 28);   //유니코드로 변환해줌 (종성)
        int index = decimalConversion(uniCode);
        return index;
    }

    //초음 + 모음을 얻어오는 함수
    private String get_initial_vowel(char target, int final_code){
        String uniCode = Integer.toHexString((target & 0xFFFF) - final_code);
        String text = getUnicodeToText(uniCode);
        return text;
    }

    //ㅇ + 모음 + 종성을 구하는 함수
    private String get_vowel_final(int finalcode, int vowelcode){
        String uniCode = Integer.toHexString(0xAC00+((11*21)+vowelcode)*28+finalcode);  //유니코드 변환 (초성)
        String text = getUnicodeToText(uniCode);
        return text;
    }

    //유니코드를 글자로 바꿔주는 함수
    private String getUnicodeToText(String unicode){
        String Kor="";
        StringTokenizer str1 = new StringTokenizer(unicode,"\\u");
        while(str1.hasMoreTokens()){
            String str2 = str1.nextToken();
            int j = Integer.parseInt(str2,16);
            Kor += (char)j;
        }
        return Kor;
    }

    //유니코드를 10진수로 바꾸어주는 함수
    private int decimalConversion(String uniCode){
        return Integer.parseInt(uniCode,16);
    }
}
