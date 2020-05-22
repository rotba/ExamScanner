package com.example.examscanner.persistence.sheetsAPI;

//import com.android.volley.Request;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.model.Request;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleSheetsAPI implements SheetsAPI {

    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "Google Sheets Example";
    private static String SPREADSHEET_ID = "1zNCz1i1grTw7Jnml1iJlAD81f-NC5mrUhjwh4wyqzDs";
    private static final Integer SHEET_ID = 0;


    public GoogleSheetsAPI() throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
    }

    private static com.google.api.client.auth.oauth2.Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = GoogleSheetsAPI.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in)
        );

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();

        com.google.api.client.auth.oauth2.Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("user");

        return credential;

    }

    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static void readUsersTable() throws IOException {

        String range = "AllUsers";

        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();

        List<List<Object>> values = response.getValues();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found!");
        } else {
            System.out.println("-----------------USERS DATA:-----------------");
            int i = 0;
            for (List row : values) {
                if (i == 0) {
                    i++;
                } else
                    System.out.printf("Registration request for the %s %s is %s\n", row.get(3), row.get(0), row.get(5));
            }
            System.out.println("---------------------------------------------");
        }
    }

    private static void insertToUsersTable() throws IOException {

        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("This", "was", "added", "from", "code", "!")
                ));

        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, "AllUsers", appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();
    }

    private static void updateUsersTable() throws IOException {

        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("updated")
                ));

        UpdateValuesResponse updateResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "C4", body)
                .setValueInputOption("RAW")
                .execute();
    }

    private static void deleteFromUsersTable() throws IOException {

        DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
                .setRange(
                        new DimensionRange()
                                .setSheetId(SHEET_ID)
                                .setDimension("ROWS")
                                .setStartIndex(3)
                );

        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setDeleteDimension(deleteRequest));

        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();
    }


    public void insertNewGrade(String spreadSheetID, int studentID1, int studentID2, int version, int[] markedAnswers, double grade) throws IOException {

        List newRow = new ArrayList();
        newRow.add(String.valueOf(studentID1).concat("/").concat(String.valueOf(studentID2)));
        newRow.add(version);
        for (int i = 1; i <= markedAnswers.length; i++) {
            newRow.add(markedAnswers[i - 1]);
        }
        newRow.add(grade);

        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(
                        newRow
                ));

        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(spreadSheetID, "Grades", appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();
    }

    public String createNewExamTable(String title, int numOfQuestions) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle(title));
        spreadsheet = sheetsService.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();

        String spreadSheetId = spreadsheet.getSpreadsheetId();
        System.out.println("Spreadsheet ID: " + spreadSheetId);

        List sheetsHeader = new ArrayList();
        sheetsHeader.add("Exam ID");
        sheetsHeader.add("Version");
        for (int i = 1; i <= numOfQuestions; i++) {
            sheetsHeader.add(String.valueOf(i));
        }
        sheetsHeader.add("Grade");
        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(sheetsHeader
                ));

        //todo define sheet name
        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, "Grades", appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();

        return spreadSheetId;
    }

    @Override
    public boolean updateGrade(String spreadSheetID, int studentID1, int studentID2, int version, int[] markedAnswers, double grade) {
        return false;
    }

    @Override
    public boolean deleteGrade(String spreadSheetID, int studentID1, int studentID2, int version, int[] markedAnswers, double grade) {
        return false;
    }


}
