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

/**
 * 선생님과의 대화 중 학생 모드 class
 */
public class StudentControl extends BasicControl implements SpeechRecognitionListener{
    private SpeechRecognitionMoudle speechRecognitionMoudle;
    private String roomNumber = "";
    private boolean checkTask = false;
    private boolean checkNewData = false;

    StudentControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        super(context, jsonFileName, databaseFileName, brailleLearningType, controlListener);
        mediaSoundManager.start(R.raw.studentmode_guide);
        speechRecognitionMoudle = new SpeechRecognitionMoudle(context, this);
    }


    /**
     * 일시정지 되었을 때 함수
     */
    @Override
    public void onPause() {
        touchLock = false;
        mediaSoundManager.stop();
        speechRecognitionMoudle.pause();
        pauseTouchEvent();
    }

    /**
     * 손가락 2개 함수 재정의
     * BACK : 뒤로가기
     * RIGHT : 오른쪽 페이지 이동
     * LEFT : 왼쪽 페이지 이동
     * REFRESH : 화면 새로고침
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onTwoFingerFunction(FingerCoordinate fingerCoordinate) {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate, touchLock);
        if(type == FingerFunctionType.BACK)
            exit();
        else{
            if(touchLock == false){
                switch (type) {
                    case RIGHT:
                        if (pageNumber < brailleDataArrayList.size() - 1) {
                            pageNumber++;
                            nodifyObservers();
                        } else {
                            checkNewData = true;
                            ReceiveServerTask task = new ReceiveServerTask();
                            task.execute();
                        }
                        break;
                    case LEFT:
                        if (0 < pageNumber) {
                            pageNumber--;
                            nodifyObservers();
                        } else {
                            mediaSoundManager.allStop();
                            mediaSoundManager.start(R.raw.first_page);
                        }
                        break;
                    case REFRESH:
                        nodifyObservers();
                        break;
                }
            }
        }
    }


    /**
     * 손가락 3개 함수 재정의
     * SPEECH : 음성인식
     * MYNOTE : 나만의 단어장 저장
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onThreeFingerFunction(FingerCoordinate fingerCoordinate) {
        if(touchLock == false) {
            FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
            switch (type) {
                case SPEECH:
                    speechRecognitionMoudle.start();
                    touchLock = true;
                    break;
                case MYNOTE:
                    if (data != null)
                        dbManager.saveMyNote(data.getLetterName(), data.getStrBrailleMatrix(), data.getAssistanceLetterName(), data.getRawId());
                    else
                        mediaSoundManager.start(R.raw.impossble_save);

                    break;
                default:
                    break;
            }
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
                    if(roomNumber.equals(room))
                        mediaSoundManager.start(R.raw.sameroom);
                    else {
                        checkTask = true;
                        brailleDataArrayList.clear();
                        roomNumber = room;
                        ReceiveServerTask task = new ReceiveServerTask();
                        task.execute();
                    }
                } else
                    mediaSoundManager.start(R.raw.speechrecognition_fail);
            } else {
                mediaSoundManager.start(R.raw.no_room);
            }
        } else {
            mediaSoundManager.start(R.raw.speechrecognition_fail);
            mediaSoundManager.start(R.raw.retry);
        }

        touchLock = false;
    }


    /**
     * 음성인식 된 결과 중 방번호를 가져오는 함수
     * 음성인식 결과로 날라온 ArrayList중 가장 처음 등장하는 숫자를 방번호로 간주
     * @param text : 음성인식 결과 arraylist
     * @return : 방 번호
     */
    private String getRoomNumber(ArrayList<String> text){
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


    /**
     * server로 접근한 뒤, 방 번호를 이용하여 점자 데이터를 받아오는 AsyncTask
     */
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
                String line = "";

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
            touchLock = false;
            if(result.equals("error") || result == "error")
                mediaSoundManager.start(R.raw.sendfail);
            else {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray jsonArray = jsonObj.getJSONArray("result");   //  mysql에서 불러온값을 저장.

                    if(jsonArray.length() == 0)
                        mediaSoundManager.start(R.raw.no_room);
                    else {
                        if (brailleDataArrayList.size() < jsonArray.length()) {
                            int startIndex = brailleDataArrayList.size();

                            for (int i = startIndex; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                String letterName = c.getString("LetterName");
                                String brailleText = c.getString("brailleText");
                                addBrailleDataArray(letterName, brailleText);
                            }

                            if (checkNewData == true) {
                                pageNumber++;
                                checkNewData = false;
                            }

                            nodifyObservers();
                        } else if (checkNewData == true) {
                            mediaSoundManager.allStop();
                            mediaSoundManager.start(R.raw.last_page);
                            checkNewData = false;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            checkTask = false;
        }
    }


    /**
     * server로 부터 수신된 점자 데이터를 저장하는 함수
     * @param letterName : 글자 이름
     * @param brailleMatrix : 점자 행렬을 의미하는 string
     */
    private void addBrailleDataArray(String letterName, String brailleMatrix){
        BrailleData data = new BrailleData(letterName, brailleMatrix, "", BrailleLearningType.TEACHER);
        data.refreshData();
        brailleDataArrayList.add(data);
    }


    /**
     * 학습화면 종료 함수
     */
    @Override
    public void exit(){
        speechRecognitionMoudle.stop();
        controlListener.exit();
    }
}
