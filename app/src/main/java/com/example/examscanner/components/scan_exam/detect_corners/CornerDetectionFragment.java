package com.example.examscanner.components.scan_exam.detect_corners;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;

public class CornerDetectionFragment extends Fragment {
    private CornerDetectionViewModel cornerDetectionViewModel;
//    private ViewPagerAdapter viewPagerAdapter;
//    private CornerDetectionCapturesAdapter cornerDetectionCapturesAdapter;
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
        ViewPager2 viewPager = (ViewPager2)view.findViewById(R.id.viewPager2_corner_detected_captures);
//        viewPagerAdapter = new ViewPagerAdapter(getActivity(), cornerDetectionViewModel.getCornerDetectedCaptures(),viewPager);
        CornerDetectionCapturesAdapter cornerDetectionCapturesAdapter = new CornerDetectionCapturesAdapter(getActivity(), cornerDetectionViewModel.getCornerDetectedCaptures(), viewPager);
        viewPager.setAdapter(cornerDetectionCapturesAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        ((TextView)view.findViewById(R.id.textView_relative_cirrent_location)).setText(
                "1/"+cornerDetectionViewModel.getNumberOfCornerDetectedCaptures().getValue()
        );
        cornerDetectionCapturesAdapter.getPosition().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView)view.findViewById(R.id.textView_relative_cirrent_location)).setText(
                        integer+"/"+cornerDetectionViewModel.getNumberOfCornerDetectedCaptures().getValue()
                );
            }
        });
        ((TextView)view.findViewById(R.id.textView_detection_progress)).setText(
                "0/"+cornerDetectionViewModel.getNumberOfCornerDetectedCaptures().getValue()
        );
        cornerDetectionViewModel.getNumberOfAnswersScannedCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView)view.findViewById(R.id.textView_detection_progress)).setText(
                        integer+"/"+cornerDetectionViewModel.getNumberOfCornerDetectedCaptures().getValue()
                );
            }
        });
        ((Button)view.findViewById(R.id.button_nav_to_resolve_answers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).
                        navigate(R.id.action_cornerDetectionFragment_to_fragment_resolve_answers);
            }
        });
        ((Button)view.findViewById(R.id.button_approve_and_scan_answers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cornerDetectionViewModel.transformAndScanAnswers();
                cornerDetectionViewModel.postProcessTransformAndScanAnswers();
            }
        });
    }
}
