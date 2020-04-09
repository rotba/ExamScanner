package com.example.examscanner.components.scan_exam.capture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class CaptureFragment extends Fragment {
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "CaptureFragment::";
    private CameraManager cameraManager;
    private CaptureViewModel captureViewModel;
    private final CompositeDisposable processRequestDisposableContainer = new CompositeDisposable();
    private CameraOutputHander outputHander;
    private ProgressBar pb;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
//    private Msg2BitmapMapper m2bmMapper;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_capture, container, false);
        cameraManager = new CameraMangerFactory(
                getActivity(),
                root
        ).create();
        requestCamera();
        return root;
    }

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb = (ProgressBar) view.findViewById(R.id.progressBar_capture);
        pb.setVisibility(View.INVISIBLE);
        final Button nextStepButton = (Button) view.findViewById(R.id.button_move_to_detect_corners);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaptureFragmentDirections.ActionCaptureFragmentToCornerDetectionFragment action =
                        CaptureFragmentDirections.actionCaptureFragmentToCornerDetectionFragment();
                action.setExamId(CaptureFragmentArgs.fromBundle(getArguments()).getExamId());
                Navigation.findNavController(view).navigate(action);
            }
        });
        ((ProgressBar)view.findViewById(R.id.progressBar_capture)).setVisibility(View.VISIBLE);
        Completable.fromAction(this::createViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onViewModelCreated, this::onViewModelCreatedError);

    }

    private void onViewModelCreatedError(Throwable throwable) {
        Log.d(TAG,MSG_PREF);
        throwable.printStackTrace();
    }

    private void onViewModelCreated() {
        captureViewModel.getNumOfTotalCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalCaptures) {
                final TextView viewById = (TextView) getActivity().findViewById(R.id.capture_processing_progress);
                final Integer processedCaptures = captureViewModel.getNumOfProcessedCaptures().getValue();
                viewById.setText(processedCaptures + "/" + totalCaptures);
                viewById.setVisibility(totalCaptures > 0 ? View.VISIBLE : View.INVISIBLE);
                ((Button) getActivity().findViewById(R.id.button_move_to_detect_corners))
                        .setVisibility(totalCaptures > 0 && processedCaptures>0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
        captureViewModel.getNumOfProcessedCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer processedCaptures) {
                final TextView viewById = (TextView) getActivity().findViewById(R.id.capture_processing_progress);
                viewById.setText(processedCaptures + "/" + captureViewModel.getNumOfTotalCaptures().getValue());
                ((Button) getActivity().findViewById(R.id.button_move_to_detect_corners))
                        .setVisibility(processedCaptures>0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
        outputHander = new CameraOutputHandlerImpl(captureViewModel, processRequestDisposableContainer);
        ((ImageButton) getActivity().findViewById(R.id.capture_image_button))
                .setOnClickListener(cameraManager.createCaptureClickListener(outputHander));
        ((ProgressBar)getActivity().findViewById(R.id.progressBar_capture)).setVisibility(View.INVISIBLE);
    }

    private void createViewModel() {
        CaptureViewModelFactory factory = new CaptureViewModelFactory(
                getActivity(),
                CaptureFragmentArgs.fromBundle(getArguments()).getSessionId(),
                CaptureFragmentArgs.fromBundle(getArguments()).getExamId()
        );
        captureViewModel = ViewModelProviders.of(this, factory).get(CaptureViewModel.class);
    }

    private void requestCamera() {
        // Permission is not granted
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            cameraManager.setUp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraManager.setUp();
        }
        int x = 1;
        x+=1;
        System.out.println(x);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraManager.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraManager.onPause();
    }

    private class ProgressBarOnClickListenerDecorator implements View.OnClickListener {
        private ProgressBar pb;
        private View.OnClickListener complicatedCalculation;

        public ProgressBarOnClickListenerDecorator(ProgressBar pb, View.OnClickListener complicatedCalculation) {
            this.pb = pb;
            this.complicatedCalculation = complicatedCalculation;
        }

        @Override
        public void onClick(View v) {
            complicatedCalculation.onClick(v);
        }
    }
}
