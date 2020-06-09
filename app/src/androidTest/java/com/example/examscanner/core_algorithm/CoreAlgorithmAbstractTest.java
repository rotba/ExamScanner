package com.example.examscanner.core_algorithm;

import android.Manifest;
import android.graphics.Bitmap;

import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.components.scan_exam.capture.Capture;
import com.example.examscanner.components.scan_exam.capture.CaptureViewModel;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.scanned_capture.Answer;
import com.example.examscanner.repositories.scanned_capture.ResolvedAnswer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.stubs.FilesManagerStub;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class CoreAlgorithmAbstractTest extends AbstractComponentInstrumentedTest {

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    private CornerDetectionContext2Setuper useCaseContext;
    private CaptureViewModel cvm;

    @Before
    public void setUp() {
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
        super.setUp();
        useCaseContext = getUseCaseContext();
        useCaseContext.setup();
        cvm = new CaptureViewModel(
                useCaseContext.getSCRepo(),
                useCaseContext.getImageProcessor(),
//                useCaseContext.getCDCRepo(),
                -1,
                useCaseContext.getTheExam()
        );
    }

    @NotNull
    protected CornerDetectionContext2Setuper getUseCaseContext() {
        return new CornerDetectionContext2Setuper();
    }

    @Override
    public void tearDown() throws Exception {
        useCaseContext.tearDown();
        super.tearDown();
    }

    //    @Test
    public void scanAnswersByPositions() {

        cvm.consumeCapture(useCaseContext.getCapture());
        cvm.processCapture();
//        useCaseContext.getCDC().setBitmap(Bitmap.createScaledBitmap(useCaseContext.getCDC().getBitmap(), useCaseContext.getOrigVersionImage().getWidth(), useCaseContext.getOrigVersionImage().getHeight(), false));
//        out.scanAnswers(useCaseContext.getCDC());
        ArrayList<Integer> realAnswers = new ArrayList<Integer>(
                Arrays.asList(5, 4, 3, 3, 3, 3, 5, 2, 4, 1, 1, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3,
                        4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4));
        List<ScannedCapture> sclst = useCaseContext.getSCRepo().get(x -> true);
        assert sclst.size() == 1;
        ScannedCapture sc = sclst.get(0);
        assertEquals(50, sc.getAnswers().size());
        int numOfCorrextDetections = 0;
        int resolved = 0;
        int correctResolved = 0;
        int wrongResolved = 0;

        for (int i = 0; i < useCaseContext.getTheExam().getNumOfQuestions(); i++) {
            Answer ans = sc.getAnswerByNum(i + 1);
            if (ans.isResolved()) {
                resolved++;
                ResolvedAnswer rAns = (ResolvedAnswer) ans;
                if (realAnswers.get(i) == rAns.getSelection()) {
                    correctResolved++;
                } else {
                    wrongResolved++;
                    System.out.println(String.format("Q#%d:\t EXP:%d, ACT:%d\n", i + 1, realAnswers.get(i), rAns.getSelection()));
                }
            }
        }
        System.out.println(
                String.format(
                        "RESOLVED#%d:\t CORRECTLY_RESOLVED:%d, WRONG_RESOLVED:%d\n",
                        resolved, correctResolved, wrongResolved
                )
        );
        assertEquals(50, resolved);
        assertEquals(50, correctResolved);
        assertEquals(0, wrongResolved);
    }
}
