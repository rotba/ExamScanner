package com.example.examscanner.components.scan_exam.detect_corners;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.VISIBLE;

public class CornerDetectionFragment extends Fragment {
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "CornerDetectionFragment::";
    private CornerDetectionViewModel cornerDetectionViewModel;
    private CornerDetectionCapturesAdapter cornerDetectionCapturesAdapter;
    private ViewPager2 viewPager;
    private final CompositeDisposable processRequestDisposableContainer = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_corner_detection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ProgressBar)view.findViewById(R.id.progressBar_detect_corners)).setVisibility(VISIBLE);
        Completable.fromAction(this::createViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onViewModelCreated, this::onViewModelCreatedFail);
        viewPager = (ViewPager2) view.findViewById(R.id.viewPager2_corner_detected_captures);
        ((Button) view.findViewById(R.id.button_cd_nav_to_resolve_answers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action =CornerDetectionFragmentDirections.actionCornerDetectionFragmentToFragmentResolveAnswers();
                Navigation.findNavController(view).navigate(action);
            }
        });
        final Button approveAndScannButton = (Button) view.findViewById(R.id.button_cd_approve_and_scan_answers);
        approveAndScannButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterBasedOPosition = cornerDetectionCapturesAdapter.getPosition().getValue();
                long cdcId = cornerDetectionCapturesAdapter.getCDCaptureIdInPosition(adapterBasedOPosition);
                CornerDetectedCapture cdc = cornerDetectionViewModel.getCDCById(cdcId).getValue();
                if (!cdc.hasVersion()){
                    askToChooseVersion();
                    return;
                }
                cornerDetectionCapturesAdapter.notifyProcessBegun(adapterBasedOPosition);
                processRequestDisposableContainer.add(generateCaptureScanningCompletable(cdc));
                waitABitAndSwipeLeft(viewPager, cornerDetectionCapturesAdapter);
            }
        });
    }

    private void askToChooseVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.detect_corners_please_choose_version)
                .setPositiveButton(R.string.detect_corners_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onViewModelCreated() {
        cornerDetectionCapturesAdapter =
                new CornerDetectionCapturesAdapter(
                        getActivity().getSupportFragmentManager(),
                        getActivity().getLifecycle(),
                        cornerDetectionViewModel.getPreProcessedCDCs(), viewPager);
        viewPager.setAdapter(cornerDetectionCapturesAdapter);

        cornerDetectionCapturesAdapter.getPosition().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) getActivity().findViewById(R.id.textView_cd_current_position)).setText(
                        (integer+1) + "/" + cornerDetectionCapturesAdapter.getmItemCount().getValue()
                );
            }
        });
        cornerDetectionCapturesAdapter.getmItemCount().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) getActivity().findViewById(R.id.textView_cd_current_position)).setText(
                        (cornerDetectionCapturesAdapter.getPosition().getValue()+1) + "/" + integer
                );
            }
        });

        cornerDetectionViewModel.getNumberOfScannedCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                final TextView processProgress = (TextView) getActivity().findViewById(R.id.textView_cd_processing_progress);
                final Integer inScanning = cornerDetectionViewModel.getNumberOfCDCaptures().getValue();
                processProgress.setText(
                        integer + "/" + inScanning
                );
                processProgress.setVisibility(inScanning > 0 ? VISIBLE : View.INVISIBLE);
                ((Button) getActivity().findViewById(R.id.button_cd_nav_to_resolve_answers))
                        .setVisibility(inScanning > 0 && integer>0 ? VISIBLE : View.INVISIBLE);
            }
        });
//        for (MutableLiveData<CornerDetectedCapture> cdc:cornerDetectionViewModel.getCDCs()) {
//            cdc.observe(getActivity(), new Observer<CornerDetectedCapture>() {
//                @Override
//                public void onChanged(CornerDetectedCapture cdc) {
//                    ((Button) getActivity().findViewById(R.id.button_cd_approve_and_scan_answers))
//                            .setEnabled(currentCardIsReadyForProcessing(cdc));
//                }
//            });
//        }
        ((ProgressBar)getActivity().findViewById(R.id.progressBar_detect_corners)).setVisibility(View.INVISIBLE);

    }

    private boolean currentCardIsReadyForProcessing(CornerDetectedCapture cdc) {
        return cdc.hasVersion() && cornerDetectionCapturesAdapter.getItemId(cornerDetectionCapturesAdapter.getPosition().getValue()) ==cdc.getId();
    }

    private void onViewModelCreatedFail(Throwable throwable) {
        Log.d(TAG, MSG_PREF);
        throwable.printStackTrace();
    }

    private void createViewModel() {
        CDViewModelFactory factory =
                new CDViewModelFactory(
                        getActivity(),
                        CornerDetectionFragmentArgs.fromBundle(getArguments()).getExamId()
                );
        cornerDetectionViewModel = new ViewModelProvider(requireActivity(), factory).get(CornerDetectionViewModel.class);
    }

    @NotNull
    private DisposableCompletableObserver generateCaptureScanningCompletable(CornerDetectedCapture cdc) {
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
    @SuppressLint("CheckResult")
    private void waitABitAndSwipeLeft(ViewPager2 viewPager, CornerDetectionCapturesAdapter adapter) {
        Observable.fromCallable(() -> {
                    Thread.sleep(200);
                    return "done";
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        viewPager.setCurrentItem(adapter.getPosition().getValue()+1);
                    }
                });
    }


}
