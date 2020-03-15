package com.example.examscanner.components.scan_exam.reslove_answers;

public class ScannedCapture {
    private int identified;
    private int unidentified;
    private Answers answers;

    public ScannedCapture(int identified, int unidentified, int[] answers) {
        this.identified = identified;
        this.unidentified = unidentified;
        this.answers = new Answers(answers);
    }

    private int getZeroBasedAnswer(int index){
        return answers.getZeroBasedAnswer(index);
    }
    private int getNaturalBasedAnswer(int index){
        return answers.getZeroBasedAnswer(index);
    }

    public int getIdentified() {
        return identified;
    }

    public int getUnidentified() {
        return unidentified;
    }

    private class Answers{
        private int[] answers;
        public Answers(int[] answers) {
            this.answers = answers;
        }
        private int getNaturalBasedAnswer(int index){
            return answers[index-1];
        }
        private int getZeroBasedAnswer(int index){
            return answers[index];
        }
    }
}
