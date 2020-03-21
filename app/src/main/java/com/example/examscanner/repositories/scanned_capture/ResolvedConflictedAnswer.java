package com.example.examscanner.repositories.scanned_capture;

import com.example.examscanner.components.scan_exam.reslove_answers.resolve_conflicted_answers.Choice;

import java.util.List;

class ResolvedConflictedAnswer extends ConflictedAnswer {
    Choice choice;
    public ResolvedConflictedAnswer(ConflictedAnswer conflictedAnswer, Choice c) {
        super(conflictedAnswer.getId(),conflictedAnswer.getUpperLeft(), conflictedAnswer.getBottomRight());
        this.choice = c;
    }
    @Override
    public boolean isResolvedConflictedMissing() {
        return true;
    }


    @Override
    public Answer commitResolution() {
        return new CheckedAnswer(getId(),getUpperLeft(),getBottomRight(), choice.value);
    }
}
