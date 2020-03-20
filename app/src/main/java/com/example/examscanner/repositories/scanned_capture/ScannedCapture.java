package com.example.examscanner.repositories.scanned_capture;

import android.graphics.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ScannedCapture {
    private int identified;
    private int unidentified;
    private List<Answer> answers;


    public ScannedCapture(int numOfTotalAnswers, int numOfAnswersDetected, int[] answersIds, int[] lefts, int[] tops, int[] rights, int[] bottoms, int[] selections) {
        this.identified=numOfAnswersDetected;
        this.unidentified=numOfTotalAnswers - numOfAnswersDetected;
        this.answers = new ArrayList<>();
        for (int i = 0; i <numOfAnswersDetected ; i++) {
            if(selections[i] >0){
                answers.add(new ResolvedAnswer(answersIds[i], new Point(lefts[i],tops[i]), new Point(rights[i],bottoms[i]), selections[i]));
            }else{
                answers.add(new UnresolvedFramedAnswer(answersIds[i] , new Point(lefts[i],tops[i]), new Point(rights[i],bottoms[i])));
            }
        }

        for (int ansId = 1; ansId <= numOfTotalAnswers && !in(ansId,answersIds); ansId++) {
            answers.add(new UnresolvedAnswer(ansId));
        }
    }

    private boolean in(int item, int[] arr){
        for (int i = 0; i <arr.length ; i++) {
            if (arr[0]==i)return true;
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

    public int getIdentified() {
        return identified;
    }

    public int getUnidentified() {
        return unidentified;
    }

    private abstract class Answer{

        private final int id;

        public Answer(int id) {
            this.id = id;
        }
        public int getId(){return id;}
    }
    private class ResolvedAnswer extends Answer{

        private final Point upperLeft;
        private final Point bottomRight;
        private final int selection;

        public ResolvedAnswer(int id, Point upperLeft, Point bottomRight, int selection) {
            super(id);
            this.upperLeft = upperLeft;
            this.bottomRight = bottomRight;
            this.selection = selection;
        }
    }
    private class UnresolvedFramedAnswer extends Answer{
        private final Point upperLeft;
        private final Point bottomRight;

        public UnresolvedFramedAnswer(int id, Point upperLeft, Point bottomRight) {
            super(id);
            this.upperLeft = upperLeft;
            this.bottomRight = bottomRight;
        }
    }
    private class UnresolvedAnswer extends Answer{

        public UnresolvedAnswer(int id) {
            super(id);
        }
    }
    private class NoSuchAnswerException extends Exception{
        public NoSuchAnswerException(String message) {
            super(message);
        }
    }
}
