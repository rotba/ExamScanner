package com.example.examscanner.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.examscanner.persistence.daos.ExamDao;
import com.example.examscanner.persistence.daos.ExamineeAnswerDao;
import com.example.examscanner.persistence.daos.ExamineeSolutionDao;
import com.example.examscanner.persistence.daos.QuestionDao;
import com.example.examscanner.persistence.daos.SemiScannedCaptureDao;
import com.example.examscanner.persistence.daos.SessionDao;
import com.example.examscanner.persistence.daos.VersionDao;
import com.example.examscanner.persistence.entities.Exam;
import com.example.examscanner.persistence.entities.ExamineeAnswer;
import com.example.examscanner.persistence.entities.ExamineeSolution;
import com.example.examscanner.persistence.entities.Question;
import com.example.examscanner.persistence.entities.SemiScannedCapture;
import com.example.examscanner.persistence.entities.Session;
import com.example.examscanner.persistence.entities.Version;

@Database(
        entities = {
                Exam.class,
                Version.class,
                Question.class,
                ExamineeSolution.class,
                ExamineeAnswer.class,
                Session.class,
                SemiScannedCapture.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExamDao getExamDao();
    public abstract SessionDao getSessionDao();
    public abstract SemiScannedCaptureDao getSemiScannedCaptureDao();
    public abstract ExamineeSolutionDao getExamineeSolutionDao();
    public abstract VersionDao getVersionDao();
    public abstract QuestionDao getQuestionDao();

    public abstract ExamineeAnswerDao getExamineeAnswerDao();
}
