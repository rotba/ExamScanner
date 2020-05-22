package com.example.examscanner.persistence.sheetsAPI;

public class SheetsAPIFacadeFactory {

    public SheetsAPIFacadeFactory(){

    }

    public SheetsAPIFacade create() {
        return new GoogleSheetsAPI();
    }
}
