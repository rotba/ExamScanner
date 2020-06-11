package com.example.examscanner.components.scan_exam.detect_corners;

import android.Manifest;
import android.graphics.PointF;

import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.R;
import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.capture.CameraManagerStub;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.ScannedCapturesInstancesFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.local.entities.Exam;
import com.example.examscanner.persistence.local.entities.ExamCreationSession;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.repositories.session.SESessionProviderFactory;
import com.example.examscanner.stubs.ExamRepositoryStub;
import com.example.examscanner.stubs.ExamStubFactory;
import com.example.examscanner.stubs.FilesManagerStub;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.ImageProcessorsGenerator.halfFakeIP;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static org.hamcrest.Matchers.containsString;



public class ResolveConflictsTest extends StateFullTest {
    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private ImageProcessingFacade imageProcessor;
    private Repository<ScannedCapture> repo;
//    private Repository<CornerDetectedCapture> cdcRepo;
    private long examId;
    private long scanExamSession;
    private int theDevilVersionNumber = 666;
    private com.example.examscanner.repositories.exam.Exam exam;
    private CornerDetectionContext2Setuper context;
    private String currentUserId;

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule camera = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    @Before
    @Override
    public void setUp() {
        AppDatabaseFactory.setTestMode();
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(halfFakeIP(getApplicationContext()));
        CornerDetectionContext2Setuper.setIOStub(new ImageProcessingFactory().create());
        setupCallback = () -> {
            FirebaseDatabaseFactory.setTestMode();
            TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
                @Override
                public void onNext(FirebaseAuth firebaseAuth) {
                    currentUserId = firebaseAuth.getUid();
                }
            };
            AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
            observer.awaitCount(1);
            context = getContext();
            context.setup();
        };
        super.setUp();

//        repo = new ScannedCaptureRepositoryFactory().create();
//        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
//        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
//        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), new DetectCornersConsumer() {
//            @Override
//            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
//            }
//        });
//        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), new DetectCornersConsumer() {
//            @Override
//            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
//            }
//        });
//        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
//            @Override
//            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
//            }
//        });
        navFromHomeToDetecteCornersUnderTestExam();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        context.tearDown();
        super.tearDown();
    }

    @NotNull
    private CornerDetectionContext2Setuper getContext() {
        return new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_1());
    }
    private void navFromHomeToDetecteCornersUnderTestExam() {
        Utils.sleepSwipingTime();
        Utils.sleepSwipingTime();
        Utils.sleepSwipingTime();
        Utils.sleepSwipingTime();
        onView(withText(containsString(context.getTheExam().getCourseName()))).perform(click());
        Utils.sleepAlertPoppingTime();
        captureASolution();
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
//        onView(withId(R.id.spinner_detect_corners_version_num)).perform(click());
//        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
    }
    private void captureASolution() {
        onView(withId(R.id.spinner_capture_version_num)).perform(click());
        onView(withText(Integer.toString(context.getSomeVersion()))).perform(click());
        onView(withId(R.id.editText_capture_examineeId)).perform(replaceText(context.getSomeExamineeId()));
        onView(withId(R.id.capture_image_button)).perform(click());
    }

    @Test
    public void testConflictAndCheckedAMountUpdatesUponResolution() {
        onView(withText("Resolve")).perform(click());
        resolveAndSwipe("4");
        Utils.sleepSwipingTime();
        resolveAndSwipe("5");
        Utils.sleepSwipingTime();
    }

    private void resolveAndSwipe(String s) {
        onView(withText(s)).perform(click());

    }
}
