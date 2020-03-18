package com.example.examscanner.components.scan_exam.reslove_answers;

import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

public class NullScannedCapture extends ScannedCapture {
    public NullScannedCapture() {
        super(-1, -1, null);
    }
}
