package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.examscanner.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CornerDetectionCardFragment extends Fragment {
    private CornerDetectionViewModel cornerDetectionViewModel;
    private int captureId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        captureId = getArguments().getInt(CornerDetectionCapturesAdapter.positionKey());
        CornerDetectionViewModelFactory factory =new CornerDetectionViewModelFactory();
        cornerDetectionViewModel = new ViewModelProvider(getActivity(),factory).get(CornerDetectionViewModel.class);
        View root = inflater.inflate(R.layout.item_corner_detected_capture, container, false);
        ((ImageView) root.findViewById(R.id.imageView2_corner_detected_capture)).setImageBitmap(
                cornerDetectionViewModel.getCornerDetectedCaptureById(captureId).getValue().getBitmap()
        );
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView2_corner_detected_capture);
        Drawable drawable = imageView.getDrawable();
        Rect imageBounds = drawable.getBounds();
        ((ConstraintLayout) view.findViewById(R.id.constraintLayout_upper_left_container)).setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
        ((ConstraintLayout) view.findViewById(R.id.constraintLayout_upper_right_container)).setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
        ((ConstraintLayout) view.findViewById(R.id.constraintLayout_bottom_right_container)).setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
        ((ConstraintLayout) view.findViewById(R.id.constraintLayout_bottom_left_container)).setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
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
                    if(inBounaries(view.getX(), view.getY())){
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
