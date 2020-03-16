package com.example.examscanner.components.scan_exam.detect_corners;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CornerDetectionCardFragment extends Fragment {
    private CornerDetectionViewModel cornerDetectionViewModel;
    float dX,dY ;
    private int captureId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        captureId = getArguments().getInt(CornerDetectionCapturesAdapter.positionKey());
        cornerDetectionViewModel = new ViewModelProvider(
                getActivity(),
                new CornerDetectionViewModelFactory(getActivity())
        ).get(CornerDetectionViewModel.class);
        View root = inflater.inflate(R.layout.item_corner_detected_capture, container, false);
        ((ImageView)root.findViewById(R.id.imageView2_corner_detected_capture)).setImageBitmap(
                cornerDetectionViewModel.getCornerDetectedCaptureById(captureId).getValue().getBitmap()
        );
        ((ImageView)root.findViewById(R.id.imageView_upper_left)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "upper left touched");
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "DOWN ex: "+motionEvent.getRawX()+" ey: "+ motionEvent.getRawY() + "vx: "+view.getX()+" vy: "+ view.getY());
//                        dX = view.getX() - motionEvent.getRawX();
//                        dY = view.getY() - motionEvent.getRawY();
                        dX = motionEvent.getRawX();
                        dY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "MOVE ex: "+motionEvent.getRawX()+" ey: "+ motionEvent.getRawY() + "vx: "+view.getX()+" vy: "+ view.getY());
                        view.animate()
                                .x(view.getX() + (motionEvent.getRawX()- dX) )
                                .y(view.getY() + (motionEvent.getRawY() - dY ) )
                                .setDuration(0)
                            .start();
                        dX = motionEvent.getRawX();
                        dY = motionEvent.getRawY();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        return root;
    }


}
