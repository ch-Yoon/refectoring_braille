package com.project.why.braillelearning.Module;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.LearningModel.Dot;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.JsonBrailleData;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by hyuck on 2017-10-11.
 */

/**
 * 점자 번역 모듈 class
 * 음성인식을 통해 입력된 text를 점자로 번역하는 모듈
 * 최대 7칸까지 번역되도록 제한함
 */
public class BrailleTranslationModule {
    private final int INITIAL = 0;
    private final int VOWEL = 1;
    private final int FINAL = 2;
    private final int ABBREVIATION = 3;

    private final String chosung[] = {"ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ","ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"}; //초성
    private final String moum[] ={"ㅏ","ㅐ","ㅑ","ㅒ","ㅓ","ㅔ","ㅕ","ㅖ","ㅗ","ㅘ","ㅙ","ㅚ","ㅛ","ㅜ","ㅝ","ㅞ","ㅟ","ㅠ","ㅡ","ㅢ","ㅣ"}; //모음
    private final String jongsung[] ={"","ㄱ","ㄲ","ㄳ","ㄴ","ㄵ","ㄶ","ㄷ","ㄹ","ㄺ","ㄻ","ㄼ","ㄽ","ㄾ","ㄿ","ㅀ","ㅁ","ㅂ","ㅄ","ㅅ","ㅆ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅎ"}; //종성

    private ArrayList<BrailleData> initialBrailleDataArray = new ArrayList<>(); //초성 점자 정보 arraylist
    private ArrayList<BrailleData> vowelBrailleDataArray = new ArrayList<>(); //모음 점자 정보 arraylist
    private ArrayList<BrailleData> finalBrailleDataArray = new ArrayList<>(); //종성 모음 점자 정보 arraylist
    private ArrayList<BrailleData> abbreviationBrailleDataArray = new ArrayList<>(); // 약자 및 약어 점자 정보 arraylist
    private ArrayList<String> separateArray = new ArrayList<>(); // 문자열 분해 시, 구분자로 사용될 String 정보들을 저장하고 있는 arraylist
    private ArrayList<Dot[][]> tempMatrixArray = new ArrayList<>(); // 점자 번역 시 이용되는 임시 arraylist
    private CustomStringTokenizer customStringTokenizer = new CustomStringTokenizer(); // 문자열 분해를 위한 customTokenizer

    public BrailleTranslationModule(Context context) {
        initialBrailleDataArray = getJsonBrailleData(context, Json.INITIAL);
        vowelBrailleDataArray = getJsonBrailleData(context, Json.VOWEL);
        finalBrailleDataArray = getJsonBrailleData(context, Json.FINAL);
        abbreviationBrailleDataArray = getJsonBrailleData(context, Json.ABBREVIATION);
        initSeparateArray();
    }

    private ArrayList<BrailleData> getJsonBrailleData(Context context, Json jsonFileName) {
        JsonBrailleData jsonBrailleData = new JsonBrailleData(context, jsonFileName, BrailleLearningType.TRANSLATION);
        return jsonBrailleData.getBrailleDataArray();
    }

    /**
     * 문자열 분해를 위한 arraylist를 초기화하는 함수
     * 점자 번역의 경우, 약자 및 약어 점자 중, 글자의 길이가 2글자 이상인 글자들을 구분자로 사용함
     */
    private void initSeparateArray() {
        separateArray.clear();
        for(int i=0 ; i<abbreviationBrailleDataArray.size() ; i++){
            BrailleData brailleData = abbreviationBrailleDataArray.get(i);
            String letterName = brailleData.getLetterName();
            if(letterName.length() > 1){
                separateArray.add(letterName);
            }
        }
    }

    /**
     * 점자 번역 후, BrailleData class로 가공하여 return함
     * @param text : 음성인식을 통해 입력된 text
     * @return 번역된 점자가 담긴 BrailleData class
     */
    public BrailleData translation(String text){
        tempMatrixArray.clear(); // 점자 번역 임시 저장 array 초기화
        boolean result = false;

        ArrayList<String> textArray = new ArrayList<>();
        textArray.addAll(customStringTokenizer.getSeparatorText(separateArray,text)); // separateArray에 저장되어 있는 data를 구분자로 문자열 분해

        for(int i=0 ; i<textArray.size() ; i++){
            String targetText = textArray.get(i);
            result = startTextToBraille(targetText);
            if(result == false) // 번역 실패
                return null;
        }

        if(result == true) // 번역 성공
            return setTranslationMatrix(text); //brailledata에 점자 정보 가공
        else
            return null;
    }

    private boolean startTextToBraille(String targetText){
        boolean result = false;

        if(targetText.length() <= 1){ // 문자열의 길이가 1일 경우
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
        } else //문자열의 길이가 1보다 클 경우
            result = searchBrailleData(targetText, ABBREVIATION);

        return result;
    }

    /**
     * 초성 탐색 함수
     * 초성이 o일 경우, 탐색 성공이라고 return함
     * 모음에서 o를 포함하고 있기 때문
     */
    private boolean searchInitial(String text){
        if(text == "ㅇ" || text.equals("ㅇ")) // 초성이 ㅇ일 경우는 pass
            return true;
        else
            return searchBrailleData(text, INITIAL);
    }

    /**
     * 모음 탐색 함수
     */
    private boolean searchVowel(String text){
        return searchBrailleData(text, VOWEL);
    }

    /**
     *종성 탐색 함수
     */
    private boolean searchFianal(String text){
        int length = text.length();
        if(length == 0)
            return true;
        else
            return searchBrailleData(text, FINAL);
    }

    /**
     * 초성 + 모음 탐색 함수
     */
    private boolean searchInitialAndVowel(char target, int finalCode){
        String initialAndVowel = get_initial_vowel(target, finalCode);
        return searchBrailleData(initialAndVowel, ABBREVIATION);
    }

    /**
     * ㅇ + 모음 + 종성 탐색 함수
     */
    private boolean searchVowelAndFinal(int finalCode, int vowelCode){
        String vowelAndFinal = get_vowel_final(finalCode, vowelCode);
        return searchBrailleData(vowelAndFinal, ABBREVIATION);
    }

    /**
     * 점자 search 함수
     * @param text : 탐색을 위한 목표 text
     * @param brailleType : 탐색하려는 글자의 type
     * @return true(탐색 성공), false(탐색 실패)
     */
    private boolean searchBrailleData(String text, int brailleType){
        ArrayList<BrailleData> brailleDataArrayList;

        switch(brailleType){
            case INITIAL:
                brailleDataArrayList = initialBrailleDataArray;
                break;
            case VOWEL:
                brailleDataArrayList = vowelBrailleDataArray;
                break;
            case FINAL:
                brailleDataArrayList = finalBrailleDataArray;
                break;
            case ABBREVIATION:
                brailleDataArrayList = abbreviationBrailleDataArray;
                break;
            default:
                return false;
        }

        for(int i=0 ; i<brailleDataArrayList.size() ; i++){
            BrailleData brailleData = brailleDataArrayList.get(i);
            String letterName = brailleData.getLetterName();

            if(text.equals(letterName) || text==letterName) {
                tempMatrixArray.add(brailleData.getBrailleMatrix()); //점자 발견 시, 점자 행렬정보를 임시 array에 저장
                return true;
            }
        }
        return false;
    }

    /**
     * 점자 정보를 brailleData에 가공하여 return하는 함수
     * 최대 7칸의 점자 정보를 갖고 있음.
     * 7칸이 넘을 경우, 점자 번역 실패로 처리함
     * @param letterName : 번역성공한 글자
     * @return : 가공된 점자 정보가 담겨잇는 brailleData class
     */
    private BrailleData setTranslationMatrix(String letterName) {
        final int MAX_COL = 4;
        final int MAX_ROW = 21; // 7칸

        if(!tempMatrixArray.isEmpty()){
            Dot tempMatrix[][] = new Dot[MAX_COL][MAX_ROW];
            int progressRowIndex = 0;

            for(int i=0 ; i<tempMatrixArray.size() ; i++){
                Dot brailleMatrix[][] = tempMatrixArray.get(i);

                int col = brailleMatrix.length;
                int row = brailleMatrix[0].length;

                for(int j=0 ; j<row ; j++){
                    for(int k=0; k<col ; k++)
                        tempMatrix[k][progressRowIndex] = brailleMatrix[k][j];

                    progressRowIndex++;
                    if(MAX_ROW <= progressRowIndex){ // 7칸에 다다랐을 경우
                        if(i == tempMatrixArray.size()-1) // 반복문이 끝이라면, 7칸을 넘지 않는다는것을 의미
                            break;
                        else // 7칸을 넘을경우 점자 번역 실패로 처리
                            return null;
                    }
                }
            }

            Dot realTranslationmatrix[][] = new Dot[MAX_COL][progressRowIndex]; //번역된 칸 수 만큼 배열 생성
            DataConversionModule conversionModule = new DataConversionModule(BrailleLearningType.TRANSLATION); //점자 data를 가공하는 모듈 생성
            for(int i=0 ; i<MAX_COL ; i++){
                for(int j=0 ; j<progressRowIndex ; j++) {
                    realTranslationmatrix[i][j] = new Dot(tempMatrix[i][j]); // 임시 행렬을 깊은복사로 가져옴
                    realTranslationmatrix[i][j].setX(conversionModule.getCoordinate_X(j)); // x좌표를 다시 setting
                    realTranslationmatrix[i][j].setY(conversionModule.getCoordinate_Y(MAX_COL, i)); // y좌표를 다시 setting

                    if ((0 < i) && (j < progressRowIndex - 1) && (realTranslationmatrix[i][j].getDotType() == 7)) // 내부 구분선 지정
                        realTranslationmatrix[i][j].setDotType(8);
                }
            }

            String rawId = conversionModule.getConversionRawId(realTranslationmatrix, letterName);
            String brailleText = conversionModule.getConversionBrailleText(realTranslationmatrix);
            BrailleData translationBrailleData = new BrailleData(letterName, brailleText, realTranslationmatrix, null, rawId); // 가공된 brailleData생성


            return translationBrailleData;

        } else
            return null;
    }


    /**
     * 초성을 얻어오는 함수
     */
    private int get_initial(char target, int vowel_code, int final_code){
        String uniCode = Integer.toHexString(((((target & 0xFFFF)- final_code - 0xAC00) / 28) - vowel_code) / 21);  //유니코드 변환 (초성)
        int index = decimalConversion(uniCode);
        return index;
    }

    /**
     * 모음을 얻어오는 함수
     */
    private int get_vowel(char target, int final_code){
        String uniCode = Integer.toHexString((((target & 0xFFFF)- final_code - 0xAC00) / 28) % 21);     //유니코드로 변환해줌 (모음)
        int index = decimalConversion(uniCode);
        return index;
    }

    /**
     *종성을 얻어오는 함수
     */
    private int get_final(char target){
        String uniCode = Integer.toHexString(((target & 0xFFFF)-0xAC00) % 28);   //유니코드로 변환해줌 (종성)
        int index = decimalConversion(uniCode);
        return index;
    }

    /**
     * 종성을 얻어오는 함수
     */
    private String get_initial_vowel(char target, int final_code){
        String uniCode = Integer.toHexString((target & 0xFFFF) - final_code);
        String text = getUnicodeToText(uniCode);
        return text;
    }

    /**
     * ㅇ + 모음 + 종성을 구하는 함수
     */
    private String get_vowel_final(int finalcode, int vowelcode){
        String uniCode = Integer.toHexString(0xAC00+((11*21)+vowelcode)*28+finalcode);  //유니코드 변환 (초성)
        String text = getUnicodeToText(uniCode);
        return text;
    }

    /**
     * 유니코드를 글자로 바꿔주는 함수
     */
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

    /**
     * 유니코드를 10진수로 바꾸어주는 함수
     */
    private int decimalConversion(String uniCode){
        return Integer.parseInt(uniCode,16);
    }
}
