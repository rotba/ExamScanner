package com.example.examscanner.persistence.sheetsAPI;

import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.security.GeneralSecurityException;


public interface sheetsAPIFacade {

    Sheets getSheetsService() throws IOException, GeneralSecurityException;

    void insertNewGrade(String spreadSheetID, int studentID1, int studentID2, int version, int[] markedAnswers, double grade) throws IOException;

    String createNewExamTable(String title, int numOfQuestions) throws IOException;

    boolean updateGrade(String spreadSheetID, int studentID1, int studentID2, int version, int[] markedAnswers, double grade);

    boolean deleteGrade(String spreadSheetID, int studentID1, int studentID2, int version, int[] markedAnswers, double grade);


}
