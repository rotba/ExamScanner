package com.example.examscanner.components.scan_exam.detect_corners;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.R;


public class CornerDetectionCardFragment extends Fragment {
    private static final String TAG = "ExamScanner.CornerDetectionCardFragment";
    private CornerDetectionViewModel cornerDetectionViewModel;
//    private ProgressBarHandler progressBarHandler;
    private int captureId;
    private boolean inProgress = false;
    private ProgressBar pb;




    @SuppressLint("LongLogTag")
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, String.format("frag of %d onResume", captureId));
    }

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        captureId = getArguments().getInt(CornerDetectionCapturesAdapter.captureId());
        CDViewModelFactory factory = new CDViewModelFactory();
        cornerDetectionViewModel = new ViewModelProvider(getActivity(), factory).get(CornerDetectionViewModel.class);
        View root = inflater.inflate(R.layout.item_corner_detected_capture, container, false);
        Log.d(TAG,String.format("Frag of cap:%d", captureId));
        ((ImageView) root.findViewById(R.id.imageView2_corner_detected_capture)).setImageBitmap(
                cornerDetectionViewModel.getCDCById(captureId).getValue().getBitmap()
        );
        pb = ((ProgressBar) root.findViewById(R.id.progressBar2_scanning_answers));
        if(inProgress){
            pb.setVisibility(View.VISIBLE);
        }else{
            pb.setVisibility(View.INVISIBLE);
        }
        return root;
    }

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
    }

    public void onProcessingBegun() {
        inProgress = true;
        if(pb==null)return;
        pb.setVisibility(View.VISIBLE);
    }

    public void onProcessingFinished() {
        inProgress = false;
        if(pb==null)return;
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

}
