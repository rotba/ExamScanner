package com.example.examscanner.persistence.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.Version;

import java.util.List;

public class VersionWithExamineeSolutions {
    @Embedded private Version version;

    public VersionWithExamineeSolutions(Version version, List<ExamineeSolution> examineeSolutions) {
        this.version = version;
        this.examineeSolutions = examineeSolutions;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public List<ExamineeSolution> getExamineeSolutions() {
        return examineeSolutions;
    }

    public void setExamineeSolutions(List<ExamineeSolution> examineeSolutions) {
        this.examineeSolutions = examineeSolutions;
    }

    @Relation(
            parentColumn = "versionId",
            entityColumn = "examineeSolutionId"
    )
    private List<ExamineeSolution> examineeSolutions;
}
