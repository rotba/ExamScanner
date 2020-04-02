package com.example.examscanner.components.scan_exam.capture;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;
import com.example.examscanner.components.scan_exam.capture.camera.CameraPermissionRequester;

import io.reactivex.disposables.CompositeDisposable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class CaptureFragment extends Fragment  {
    private CameraManager cameraManager;
    private CameraPermissionRequester permissionRequester;
    private CaptureViewModel captureViewModel;
    private final CompositeDisposable processRequestDisposableContainer = new CompositeDisposable();
    private CameraOutputHander outputHander;
//    private Msg2BitmapMapper m2bmMapper;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        m2bmMapper = new Msg2BitmapFactory(getActivity()).create();
        View root = inflater.inflate(R.layout.fragment_capture, container, false);
        CaptureViewModelFactory factory = new CaptureViewModelFactory(
                getActivity(),
                CaptureFragmentArgs.fromBundle(getArguments()).getVersionId(),
                CaptureFragmentArgs.fromBundle(getArguments()).getSessionId()
        );
        captureViewModel =ViewModelProviders.of(this,factory).get(CaptureViewModel.class);
        cameraManager = new CameraMangerFactory(
                getActivity(),
                root
        ).create();

        permissionRequester = new CameraPermissionRequester(
                ()-> cameraManager.setUp(),
                getActivity()
        );
        permissionRequester.request();
        outputHander= new CameraOutputHandlerImpl(captureViewModel,processRequestDisposableContainer);
        return root;
    }

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout container = (ConstraintLayout)view;
//        View v = View.inflate(requireContext(), R.layout.camera_ui_container, container);
        ((ImageButton)view.findViewById(R.id.capture_image_button))
                .setOnClickListener(cameraManager.createCaptureClickListener(outputHander));
        ((Button)view.findViewById(R.id.button_move_to_detect_corners)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaptureFragmentDirections.ActionCaptureFragmentToCornerDetectionFragment action =
                        CaptureFragmentDirections.actionCaptureFragmentToCornerDetectionFragment();
                action.setExamId((int)captureViewModel.getVersionId());
                Navigation.findNavController(view).navigate(action);
            }
        });
        captureViewModel.getNumOfTotalCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalCaptures) {
                ((TextView)view.findViewById(R.id.capture_processing_progress))
                        .setText(
                                captureViewModel.getNumOfProcessedCaptures().getValue()+"/"+totalCaptures
                        );
            }
        });
        captureViewModel.getNumOfProcessedCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer processedCaptures) {
                ((TextView)view.findViewById(R.id.capture_processing_progress))
                        .setText(
                                processedCaptures+"/"+captureViewModel.getNumOfTotalCaptures().getValue()
                        );
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        permissionRequester.onRequestPermissionsResult(requestCode,permissions, grantResults);
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

}
