package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.os.AsyncTask;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-27.
 */

public class StudentControl extends BasicControl implements SpeechRecognitionListener{
    private SpeechRecognitionMoudle speechRecognitionMoudle;
    private String roomNumber = "";
    private boolean checkTask = false;

    StudentControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        super(context, jsonFileName, databaseFileName, brailleLearningType, controlListener);
        mediaSoundManager.start(R.raw.studentmode_guide);
        speechRecognitionMoudle = new SpeechRecognitionMoudle(context, this);
    }

    /**
     * 손가락 3개에 대한 event 함수
     * SPEECH(음성인식), MYNOTE(나만의 단어장 저장 및 삭제)
     */
    @Override
    public void threeFingerFunction() {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
        switch (type) {
            case SPEECH:
                speechRecognitionMoudle.start();
                break;
            case MYNOTE:
                if(data != null)
                    dbManager.saveMyNote(data.getLetterName(), data.getStrBrailleMatrix(), data.getAssistanceLetterName(), data.getRawId());
                else
                    mediaSoundManager.start(R.raw.impossble_save);
                break;
            default:
                break;
        }
    }



    /**
     * 음성인식에 대한 callback method
     * 음성인식에 대한 ArrayList가 전달됨
     */
    @Override
    public void speechRecogntionResult(ArrayList<String> text) {
        if(text != null){
            String room = getRoomNumber(text);
            if(room != null) {
                if(checkTask == false) {
                    checkTask = true;
                    roomNumber = room;
                    ReceiveServerTask task = new ReceiveServerTask();
                    task.execute();
                } else
                    mediaSoundManager.start(R.raw.speechrecognition_fail);
            }
        }
    }

    public String getRoomNumber(ArrayList<String> text){
        boolean number = false;
        for(int i=0 ; i<text.size() ; i++){
            String target = text.get(i);
            for(int j=0 ; j<target.length() ; j++){
                char checkCharacter = target.charAt(j);
                if(48 <= checkCharacter && checkCharacter <= 57)
                    number = true;
                else {
                    number = false;
                    break;
                }
            }

            if(number == true)
                return target;
        }

        return null;
    }

    class ReceiveServerTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                String link = Global.studentServerURL;
                String inputData  = URLEncoder.encode("room", "UTF-8") + "=" + URLEncoder.encode(roomNumber, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(inputData);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            }
            catch(Exception e){
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("error") || result == "error") {
                mediaSoundManager.start(R.raw.sendfail);
            } else {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray jsonArray = jsonObj.getJSONArray("result");   //  mysql에서 불러온값을 저장.

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject c = jsonArray.getJSONObject(i);
                        String letterName = c.getString("LetterName");
                        String brailleText = c.getString("brailleText");
                        String rawId = c.getString("rawID");
                        addBrailleDataArray(letterName, brailleText, rawId);
                    }

                    nodifyObservers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            checkTask = false;
        }
    }

    private void addBrailleDataArray(String letterName, String brailleMatrix, String rawId){
        BrailleData data = new BrailleData(letterName, brailleMatrix, rawId, BrailleLearningType.TEACHER);
        brailleDataArrayList.add(data);
    }
}
