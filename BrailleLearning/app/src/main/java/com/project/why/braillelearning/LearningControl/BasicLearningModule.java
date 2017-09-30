package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.GettingBraille;
import com.project.why.braillelearning.LearningView.Observers;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BasicLearningModule implements FingerFunction {
    private ArrayList<Observers> observersArrayList;
    private ArrayList<BrailleData> brailleDataArrayList;

    private int pageNumber = 0;


    BasicLearningModule(ArrayList<GettingBraille> brailleArrayList){
        this.observersArrayList = new ArrayList<>();
        brailleDataArrayList = brailleArrayList.get(0).getBrailleDataArray();
    }

    @Override
    public void addObservers(Observers observers) {
        observersArrayList.add(observers);
    }

    @Override
    public void nodifyObservers() {
        for(int i=0 ; i<observersArrayList.size() ; i++){
            Observers observers = observersArrayList.get(i);
            observers.nodifyBraille(brailleDataArrayList.get(pageNumber));
        }
    }

    @Override
    public void oneFingerFunction() {

    }

    @Override
    public void twoFingerFunction() {

    }

    @Override
    public void threeFingerFunction() {

    }
}
