package com.example.examscanner.components.scan_exam.detect_corners;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CornerDetectionCardFragment extends Fragment {
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "CornerDetectionCardFragment";
    private CornerDetectionViewModel cornerDetectionViewModel;
    //    private ProgressBarHandler progressBarHandler;
    private long captureId;
    private boolean inProgress = false;
    private ProgressBar pb;
    private View root;

    public CornerDetectionCardFragment(long id) {
        this.captureId = id;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cornerDetectionViewModel = new ViewModelProvider(requireActivity()).get(CornerDetectionViewModel.class);
        root = inflater.inflate(R.layout.item_corner_detected_capture, container, false);
        ((ImageView) root.findViewById(R.id.imageView2_corner_detected_capture)).setImageBitmap(
                cornerDetectionViewModel.getCDCById(captureId).getValue().getBitmap()
        );

        pb = ((ProgressBar) root.findViewById(R.id.progressBar2_scanning_answers));
        if (inProgress) {
            pb.setVisibility(View.VISIBLE);
        } else {
            pb.setVisibility(View.INVISIBLE);
        }
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView2_corner_detected_capture);
        Drawable drawable = imageView.getDrawable();
        Rect imageBounds = drawable.getBounds();
        ((ConstraintLayout) view.findViewById(R.id.constraintLayout_upper_left_container)).setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
        ((ConstraintLayout) view.findViewById(R.id.constraintLayout_upper_right_container)).setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
        ((ConstraintLayout) view.findViewById(R.id.constraintLayout_bottom_right_container)).setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
        ((ConstraintLayout) view.findViewById(R.id.constraintLayout_bottom_left_container)).setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
        Observable.fromCallable(() -> cornerDetectionViewModel.getVersionNumbers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onVersionNumbersRetrived, this::onVersionNumbersRetrivedError);
    }

    private void onVersionNumbersRetrived(int[] versionNumbers) {
        String[] versionStrings = new String[versionNumbers.length + 1];
        String theEmptyChoice = getActivity().getString(R.string.detect_corners_the_empty_version_choice);
        versionStrings[0] = theEmptyChoice;
        for (int i = 1; i < versionNumbers.length+1; i++) {
            versionStrings[i] = new String(Integer.toString(versionNumbers[i-1]));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, versionStrings);

        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_detect_corners_version_num);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = (String) parent.getSelectedItem();
                if (choice.equals(theEmptyChoice))
                    return;
                Integer intChoice = Integer.parseInt(choice);
                cornerDetectionViewModel.setVersion(captureId, intChoice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        new VersionNumberWatcher() {
//            @Override
//            public void onVersionReceived(int verNum) {
//
//            }
//        });
    }

    private void onVersionNumbersRetrivedError(Throwable throwable) {
        Log.d(TAG, MSG_PREF + " onVersionNumbersRetrivedError");
        throwable.printStackTrace();
    }


    public void onProcessingBegun() {
        inProgress = true;
        if (pb == null) return;
        pb.setVisibility(View.VISIBLE);
    }

    public void onProcessingFinished() {
        inProgress = false;
        if (pb == null) return;
        pb.setVisibility(View.INVISIBLE);
    }

    private class CornerPointOnTouchListener implements View.OnTouchListener {
        float dX, dY;
        private int absoluteLeft;
        private int absoluteRight;
        private int absoluteTop;
        private int absoluteBottom;

        public CornerPointOnTouchListener(int absoluteLeft, int absoluteRight, int absoluteTop, int absoluteBottom) {
            this.absoluteLeft = absoluteLeft;
            this.absoluteRight = absoluteRight;
            this.absoluteTop = absoluteTop;
            this.absoluteBottom = absoluteBottom;
        }

        @SuppressLint("LongLogTag")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = motionEvent.getRawX();
                    dY = motionEvent.getRawY();
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "x: " + motionEvent.getRawX() + " y: " + view.getY());
                    if (inBounaries(view.getX(), view.getY())) {
                        view.animate()
                                .x(view.getX() + (motionEvent.getRawX() - dX))
                                .y(view.getY() + (motionEvent.getRawY() - dY))
                                .setDuration(0)
                                .start();
                    }
                    dX = motionEvent.getRawX();
                    dY = motionEvent.getRawY();
                    break;
                default:
                    return false;
            }
            return true;
        }

        private boolean inBounaries(float rawX, float rawY) {
            return true;
//            Log.d(TAG, "L: "+absoluteLeft+" rawX: "+rawX+" R:"+absoluteRight+" T: "+absoluteTop+" rawY: "+" B: "+absoluteBottom);
//            return absoluteLeft <= rawX && rawX<= absoluteRight && absoluteTop<= rawY && rawY <=absoluteBottom;
        }
    }

    private abstract class VersionNumberWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onVersionReceived(Integer.valueOf(s.toString()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        public abstract void onVersionReceived(int verNum);
    }
}
