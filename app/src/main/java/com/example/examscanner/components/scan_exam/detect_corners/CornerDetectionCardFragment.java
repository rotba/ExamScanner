package com.example.examscanner.components.scan_exam.detect_corners;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.R;

public class CornerDetectionCardFragment extends Fragment {
    private CornerDetectionViewModel cornerDetectionViewModel;
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
        return root;
    }
}
