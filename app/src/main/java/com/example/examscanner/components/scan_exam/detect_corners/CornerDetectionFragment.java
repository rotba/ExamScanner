package com.example.examscanner.components.scan_exam.detect_corners;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.CaptureFragmentArgs;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class CornerDetectionFragment extends Fragment {
    private static final String TAG = "ExamScanner";
    private CornerDetectionViewModel cornerDetectionViewModel;
    private CornerDetectionCapturesAdapter cornerDetectionCapturesAdapter;
    private final CompositeDisposable processRequestDisposableContainer = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CornerDetectionViewModelFactory factory =
                new CornerDetectionViewModelFactory(
                        getActivity(),
                        CaptureFragmentArgs.fromBundle(getArguments()).getExamId()
                );
        cornerDetectionViewModel = new ViewModelProvider(this, factory).get(CornerDetectionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_corner_detection, container, false);
        return inflater.inflate(R.layout.fragment_corner_detection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.viewPager2_corner_detected_captures);
        cornerDetectionCapturesAdapter =
                new CornerDetectionCapturesAdapter(
                        getActivity().getSupportFragmentManager(),
                        getActivity().getLifecycle(),
                        cornerDetectionViewModel.getPreProcessedCornerDetectedCaptures(), viewPager);
        viewPager.setAdapter(cornerDetectionCapturesAdapter);

        cornerDetectionCapturesAdapter.getPosition().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) view.findViewById(R.id.textView_cd_current_position)).setText(
                        (integer+1) + "/" + cornerDetectionCapturesAdapter.getmItemCount().getValue()
                );
            }
        });
        cornerDetectionCapturesAdapter.getmItemCount().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) view.findViewById(R.id.textView_cd_current_position)).setText(
                        (cornerDetectionCapturesAdapter.getPosition().getValue()+1) + "/" + integer
                );
            }
        });

        cornerDetectionViewModel.getNumberOfAnswersScannedCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) view.findViewById(R.id.textView_cd_processing_progress)).setText(
                        integer + "/" + cornerDetectionViewModel.getNumberOfCornerDetectedCaptures().getValue()
                );
            }
        });
        ((Button) view.findViewById(R.id.button_cd_nav_to_resolve_answers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action =CornerDetectionFragmentDirections.actionCornerDetectionFragmentToFragmentResolveAnswers();
                Navigation.findNavController(view).navigate(action);
            }
        });
        ((Button) view.findViewById(R.id.button_approve_and_scan_answers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterBasedOPosition = cornerDetectionCapturesAdapter.getPosition().getValue();
                int cdcId = cornerDetectionCapturesAdapter.getCDCaptureIdInPosition(adapterBasedOPosition);
                cornerDetectionCapturesAdapter.notifyProcessBegun(adapterBasedOPosition);
                CornerDetectedCapture cdc = cornerDetectionViewModel.getCornerDetectedCaptureById(cdcId).getValue();
                processRequestDisposableContainer.add(generateCaptureScanningCompletable(cdc,adapterBasedOPosition));
            }
        });

    }

    @NotNull
    private DisposableCompletableObserver generateCaptureScanningCompletable(CornerDetectedCapture cdc, int adapterBasedOPosition) {
        return Completable.fromCallable(() -> {
            cornerDetectionViewModel.transformToRectangle(cdc);
            cornerDetectionViewModel.scanAnswers(cdc);
            return "Done";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        cornerDetectionViewModel.postProcessTransformAndScanAnswers(cdc.getId());
                        cornerDetectionCapturesAdapter.handleProcessFinish(cdc.getId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "generateCaptureScanningCompletable");
                        e.printStackTrace();
                    }
                });
    }


}
