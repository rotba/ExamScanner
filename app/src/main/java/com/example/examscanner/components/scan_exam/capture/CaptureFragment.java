package com.example.examscanner.components.scan_exam.capture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.example.examscanner.repositories.exam.Version;

import io.reactivex.Completable;
import io.reactivex.Observable;
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
    private View.OnClickListener captureClickListener;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        captureClickListener = cameraManager.createCaptureClickListener(outputHander);
        ImageButton imageButton = (ImageButton) getActivity().findViewById(R.id.capture_image_button);
        imageButton.setOnClickListener(captureClickListener);
        imageButton.setEnabled(isValidExamineeIdAndVersion());
        captureViewModel.getCurrentExamineeId().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                imageButton.setEnabled(isValidExamineeIdAndVersion());
            }
        });
        captureViewModel.getCurrentVersion().observe(getActivity(), new Observer<Version>() {
            @Override
            public void onChanged(Version v) {
                imageButton.setEnabled(isValidExamineeIdAndVersion());
            }
        });
        Observable.fromCallable(() -> captureViewModel.getVersionNumbers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onVersionNumbersRetrived, this::onVersionNumbersRetrivedError);
        ((EditText)getActivity().findViewById(R.id.editText_capture_examineeId)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                captureViewModel.setExamineeId(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        ((ProgressBar)getActivity().findViewById(R.id.progressBar_capture)).setVisibility(View.INVISIBLE);
    }

    private boolean isValidExamineeIdAndVersion() {
        return captureViewModel.isValidVersion() &&captureViewModel.isValidExamineeId();
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
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

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

    private void onVersionNumbersRetrived(int[] versionNumbers) {
        String[] versionStrings = new String[versionNumbers.length + 1];
        String theEmptyChoice = getActivity().getString(R.string.capture_the_empty_version_choice);
        versionStrings[0] = theEmptyChoice;
        for (int i = 1; i < versionNumbers.length + 1; i++) {
            versionStrings[i] = new String(Integer.toString(versionNumbers[i - 1]));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, versionStrings);

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner_capture_version_num);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = (String) parent.getSelectedItem();
                if (choice.equals(theEmptyChoice))
                    return;
                Integer intChoice = Integer.parseInt(choice);
                captureViewModel.setVersion(intChoice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void onVersionNumbersRetrivedError(Throwable throwable) {
        Log.d(TAG, MSG_PREF + " onVersionNumbersRetrivedError", throwable);
        throwable.printStackTrace();
    }
}
