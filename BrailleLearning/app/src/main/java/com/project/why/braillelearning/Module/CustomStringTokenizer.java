package com.project.why.braillelearning.Module;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-10-13.
 */

public class CustomStringTokenizer {
    ArrayList<String> separatorArray = new ArrayList<>();

    public ArrayList<String> getSeparatorText(String separator, String text){
        separatorArray.clear();
        separatorArray.add(separator);
        return getSeparatorText(text);
    }

    public ArrayList<String> getSeparatorText(ArrayList<String> separatorArray, String text){
        this.separatorArray.clear();
        this.separatorArray.addAll(separatorArray);
        return getSeparatorText(text);
    }

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

    public String deleteSpace(String text) {
        String realText = "";

        for(int i=0 ; i<text.length() ; i++) {
            char target = text.charAt(i);
            if(target != ' ')
                realText += target;
        }

        return realText;
    }
}
