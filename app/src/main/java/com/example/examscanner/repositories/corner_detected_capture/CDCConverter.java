package com.example.examscanner.repositories.corner_detected_capture;

import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.version.Version;

import org.json.JSONException;

public class CDCConverter implements Converter<SemiScannedCaptureEntityInterface, CornerDetectedCapture> {
    private Repository<Version> verRepo;
    public CDCConverter(Repository<Version> verRepo) {
        this.verRepo = verRepo;
    }

    @Override
    public CornerDetectedCapture convert(SemiScannedCaptureEntityInterface semiScannedCapture) {
        CornerDetectedCapture ans = new CornerDetectedCapture(
                semiScannedCapture.getBitmap(),
                semiScannedCapture.getLeftMostX(),
                semiScannedCapture.getUpperMostY(),
                semiScannedCapture.getRightMostX(),
                semiScannedCapture.getBottomMosty(),
                semiScannedCapture.getSessionId()
        );
        ans.setId(semiScannedCapture.getId());
        return ans;
    }
}
