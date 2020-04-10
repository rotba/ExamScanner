package com.example.examscanner.repositories.scanned_capture;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class ScannedCapture {
    private List<Answer> answers;
    private Bitmap bm;
    private int id;


    public ScannedCapture(int id, Bitmap bm,int numOfTotalAnswers, int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
        this.bm=bm;
        this.answers = new ArrayList<>();
        for (int i = 0; i <numOfAnswersDetected ; i++) {
            if(selections[i] >0){
                answers.add(new CheckedAnswer(answersIds[i], new PointF(lefts[i],tops[i]), new PointF(rights[i],bottoms[i]), selections[i]));
            }else{
                answers.add(new ConflictedAnswer(answersIds[i] , new PointF(lefts[i],tops[i]), new PointF(rights[i],bottoms[i])));
            }
        }

        for (int ansId = 1; ansId <= numOfTotalAnswers; ansId++) {
            if(!in(ansId,answersIds)){
                answers.add(new MissingAnswer(ansId));
            }
        }
        this.id = id;
    }

    private boolean in(int item, int[] arr){
        for (int i = 0; i <arr.length ; i++) {
            if (arr[i]==item)return true;
        }
        return false;
    }

    public List<ConflictedAnswer> getConflictedAnswers(){
        List<ConflictedAnswer> ans = new ArrayList<>();
        for (Answer a: answers) {
            a.addMe(ans);
        }
        return ans;
    }

    private Answer getZeroBasedAnswer(int index){
        return answers.get(index);
    }
    private Answer getNaturalBasedAnswer(int id) throws NoSuchAnswerException {
        for (Answer a:answers) {
            if (a.getAnsNum()==id) return a;
        }
        throw new NoSuchAnswerException("Asked for: "+ id+" but there are only "+answers.size()+" answers");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public int amountOf(Predicate<Answer> pred){
        int ans = 0;
        for (Answer a: answers)
            if(pred.test(a))ans++;
        return ans;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getCheckedAmount() {
        return amountOf(a -> a.isChecked());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getConflictedAmount() {
        return amountOf(a -> a.isConflicted());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getMissingAmount() {
        return amountOf(a -> a.isMissing());
    }

    public Bitmap getBm() {
        return bm;
    }

    public int getId() {
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void resolve(ConflictedAnswer ca, ResolvedConflictedAnswer resolve) {
        answers.removeIf((answer -> answer.getAnsNum()==ca.getAnsNum()));
        answers.add(resolve);
    }

    public void commitResolutions() {
        List<Answer> newAnswers = new ArrayList<>();
        for (Answer a:answers) {
            newAnswers.add(a.commitResolution());
        }
        setAnswers(newAnswers);
    }

    private void setAnswers(List<Answer> newAnswers) {
        answers=newAnswers;
    }


    private class NoSuchAnswerException extends Exception{
        public NoSuchAnswerException(String message) {
            super(message);
        }
    }
}
