package com.example.examscanner.repositories.scanned_capture;

import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class ScannedCapture {
    private int identified;
    private int unidentified;
    private List<Answer> answers;


    public ScannedCapture(int numOfTotalAnswers, int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
        this.identified=numOfAnswersDetected;
        this.unidentified=numOfTotalAnswers - numOfAnswersDetected;
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
    }

    private boolean in(int item, int[] arr){
        for (int i = 0; i <arr.length ; i++) {
            if (arr[i]==item)return true;
        }
        return false;
    }

    private Answer getZeroBasedAnswer(int index){
        return answers.get(index);
    }
    private Answer getNaturalBasedAnswer(int id) throws NoSuchAnswerException {
        for (Answer a:answers) {
            if (a.getId()==id) return a;
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

    private abstract class Answer{

        private final int id;

        public Answer(int id) {
            this.id = id;
        }
        public int getId(){return id;}
        public boolean isChecked(){
            return false;
        }
        public boolean isConflicted(){
            return false;
        }
        public boolean isMissing(){
            return false;
        }
    }
    private class CheckedAnswer extends Answer{

        private final PointF upperLeft;
        private final PointF bottomRight;
        private final int selection;

        public CheckedAnswer(int id, PointF upperLeft, PointF bottomRight, int selection) {
            super(id);
            this.upperLeft = upperLeft;
            this.bottomRight = bottomRight;
            this.selection = selection;
        }

        @Override
        public boolean isChecked() {
            return true;
        }
    }
    private class ConflictedAnswer extends Answer{
        private final PointF upperLeft;
        private final PointF bottomRight;

        public ConflictedAnswer(int id, PointF upperLeft, PointF bottomRight) {
            super(id);
            this.upperLeft = upperLeft;
            this.bottomRight = bottomRight;
        }

        @Override
        public boolean isConflicted() {
            return true;
        }
    }
    private class MissingAnswer extends Answer{
        public MissingAnswer(int id) {
            super(id);
        }

        @Override
        public boolean isMissing() {
            return true;
        }
    }
    private class NoSuchAnswerException extends Exception{
        public NoSuchAnswerException(String message) {
            super(message);
        }
    }
}
