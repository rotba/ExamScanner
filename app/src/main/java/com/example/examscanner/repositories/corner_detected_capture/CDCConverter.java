package com.example.examscanner.repositories.corner_detected_capture;

import com.example.examscanner.communication.entities.SemiScannedCaptureEntityInterface;
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
    public CornerDetectedCapture convert(SemiScannedCaptureEntityInterface semiScannedCapture) throws JSONException {
        CornerDetectedCapture ans = new CornerDetectedCapture(
                semiScannedCapture.getBitmap(),
                semiScannedCapture.getLeftMostX(),
                semiScannedCapture.getUpperMostY(),
                semiScannedCapture.getRightMostX(),
                semiScannedCapture.getBottomMosty(),
                verRepo.get((int)semiScannedCapture.getVesrionId()),
                semiScannedCapture.getSessionId()
        );
        ans.setId(semiScannedCapture.getId());
        return ans;
    }
}
