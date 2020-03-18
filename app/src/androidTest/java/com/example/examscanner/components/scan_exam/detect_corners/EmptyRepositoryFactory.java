package com.example.examscanner.components.scan_exam.detect_corners;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EmptyRepositoryFactory {
    public static Repository<CornerDetectedCapture> create(){
        return new Repository<CornerDetectedCapture>() {
            private List<CornerDetectedCapture> data = new ArrayList<>();
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public CornerDetectedCapture get(int id) {
                return null;
            }

            @Override
            public List<CornerDetectedCapture> get(Predicate<CornerDetectedCapture> criteria) {
                List<CornerDetectedCapture> ans = new ArrayList<>();
                for (CornerDetectedCapture c:data) {
                    if (criteria.test(c))ans.add(c);
                }
                return ans;
            }

            @Override
            public void create(CornerDetectedCapture cornerDetectedCapture) {
                data.add(cornerDetectedCapture);
            }

            @Override
            public void update(CornerDetectedCapture cornerDetectedCapture) {
            }

            @Override
            public void delete(int id) {

            }
        };
    }
}
