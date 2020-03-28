package com.example.examscanner.persistence.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.entities.CorrectAnswer;
import com.example.examscanner.persistence.entities.Version;

import java.util.List;

public class VersionWithCorrectAnswers {
    @Embedded
    private Version version;
    @Relation(
            parentColumn = Version.pkName,
            entityColumn = CorrectAnswer.fkVer
    )
    private List<CorrectAnswer> correctAnswers;

    public VersionWithCorrectAnswers(Version version, List<CorrectAnswer> correctAnswers) {
        this.version = version;
        this.correctAnswers = correctAnswers;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public List<CorrectAnswer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<CorrectAnswer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
