package com.example.examscanner.components.scan_exam.reslove_answers;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EmptyRepositoryFactory {
    public static Repository<ScannedCapture> create(){
        return new Repository<ScannedCapture>() {
            private List<ScannedCapture> data = new ArrayList<>();
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public ScannedCapture get(int id) {
                return null;
            }

            @Override
            public List<ScannedCapture> get(Predicate<ScannedCapture> criteria) {
                List<ScannedCapture> ans = new ArrayList<>();
                for (ScannedCapture sc:data) {
                    if(criteria.test(sc)) ans.add(sc);
                }
                return ans;
            }

            @Override
            public void create(ScannedCapture scannedCapture) {
                data.add(scannedCapture);
            }

            @Override
            public void update(ScannedCapture scannedCapture) {

            }

            @Override
            public void delete(int id) {

            }
        };
    }
}
