package com.project.why.braillelearning.Module;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-10-13.
 */

/**
 * 점자 번역에 이용될 문자 구분 class
 * 약자 및 약어인 것과 아닌것으로 구분
 */
public class CustomStringTokenizer {
    private ArrayList<String> separatorArray = new ArrayList<>();

    /**
     * 구분하고 싶은 문자열 등록 후, 등록된 문자열로 문장을 분해한 뒤, 분해된 문장을 arraylist 형태로 되돌려주는 함수
     * @param separatorArray : 구분하고 싶은 구분문자열 arraylist
     * @param text : 구분하고 싶은 문장
     * @return : 분해된 문장 arraylist
     */
    public ArrayList<String> getSeparatorText(ArrayList<String> separatorArray, String text){
        this.separatorArray.clear();
        this.separatorArray.addAll(separatorArray);
        return getSeparatorText(text);
    }


    /**
     * 문자열 구분 재귀 함수
     * 공백 제거 후, 등록된 구분 문자열과 아닌것으로 분해
     * 문장의 순서는 유지
     * @param targetText : 구분하기 위한 문자열
     * @return :  등록된 구분 문자열과 아닌것으로 분해된 arraylist
     */
    private ArrayList<String> getSeparatorText(String targetText) {
        targetText = deleteSpace(targetText);

        ArrayList<String> stringArray = new ArrayList<>();

        boolean check = false;
        int separatorArrayIndex = 0;
        int minimumIndex = targetText.length()-1;

        for(int i=0 ; i<separatorArray.size() ; i++) {
            String abbreviationText = separatorArray.get(i);

            if(targetText.contains(abbreviationText)) {
                int index = targetText.indexOf(abbreviationText);
                if(index < minimumIndex) {
                    minimumIndex = index;
                    separatorArrayIndex = i;
                    check = true;
                }
            }

            if(i == separatorArray.size()-1) {
                if(check == true) {
                    abbreviationText = separatorArray.get(separatorArrayIndex);

                    int length = abbreviationText.length();
                    int startIndex = targetText.indexOf(abbreviationText);
                    int endIndex = startIndex + length;

                    String targetTextArray[];
                    if(length == targetText.length()) {
                        stringArray.add(targetText);
                        break;
                    } else if(startIndex == 0) {
                        targetTextArray = new String[2];
                        targetTextArray[0] = targetText.substring(0, endIndex);
                        targetTextArray[1] = targetText.substring(endIndex);
                    } else if(endIndex == targetText.length()-1) {
                        targetTextArray = new String[2];
                        targetTextArray[0] = targetText.substring(0, startIndex);
                        targetTextArray[1] = targetText.substring(startIndex);
                    } else {
                        targetTextArray = new String[3];
                        targetTextArray[0] = targetText.substring(0, startIndex);
                        targetTextArray[1] = targetText.substring(startIndex, endIndex);
                        targetTextArray[2] = targetText.substring(endIndex);
                    }


                    for(int j=0 ; j<targetTextArray.length ; j++) {
                        ArrayList<String> tempArray = getSeparatorText(targetTextArray[j]);
                        stringArray.addAll(stringArray.size(), tempArray);
                    }

                    break;
                }
                if(stringArray.isEmpty()) {
                    for(int j=0 ; j<targetText.length() ; j++) {
                        char target = targetText.charAt(j);
                        String targetString = String.valueOf(target);
                        stringArray.add(targetString);
                    }
                }
            }
        }

        return stringArray;

    }

    /**
     * 문자열 공백 제거 함수
     * @param text : 공백을 제거하고 싶은 문자열
     * @return : 공백이 제거된 문자열
     */
     private String deleteSpace(String text) {
        String realText = "";

        for(int i=0 ; i<text.length() ; i++) {
            char target = text.charAt(i);
            if(target != ' ')
                realText += target;
        }

        return realText;
    }
}
