package com.example.examscanner.components.scan_exam.detect_corners;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.examscanner.R;

public class CornerDetectionFragment extends Fragment {
    private CornerDetectionViewModel cornerDetectionViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cornerDetectionViewModel =
                ViewModelProviders.of(
                        this,
                        new CornerDetectionViewModelFactory(getActivity())
                ).get(CornerDetectionViewModel.class);
        View root =inflater.inflate(R.layout.fragment_corner_detection, container, false);
        ((TextView)root.findViewById(R.id.textView_relative_cirrent_location)).setText(
                "1/"+cornerDetectionViewModel.getNumberOfCornerDetectedCaptures().getValue()
        );
        return inflater.inflate(R.layout.fragment_corner_detection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView)view.findViewById(R.id.textView_relative_cirrent_location)).setText(
                "1/"+cornerDetectionViewModel.getNumberOfCornerDetectedCaptures().getValue()
        );
    }
}
