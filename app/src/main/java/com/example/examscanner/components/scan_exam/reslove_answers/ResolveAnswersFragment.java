package com.example.examscanner.components.scan_exam.reslove_answers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;

public class ResolveAnswersFragment extends Fragment {
    public static final String TAG ="ResolveAnswersFragment";
    private ResolveAnswersViewModel resolveAnswersViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resolveAnswersViewModel = new ViewModelProvider(
                this.getActivity(),
                new ResolveAnswersViewModelFactory(getActivity())
        ).get(ResolveAnswersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_resolve_answers, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager2 viewPager = (ViewPager2)view.findViewById(R.id.viewPager2_scanned_captures);
        ScannedCapturesAdapter scannedCapturesAdapter = new ScannedCapturesAdapter(getActivity(), resolveAnswersViewModel.getScannedCaptures(), viewPager);
        viewPager.setAdapter(scannedCapturesAdapter);
        scannedCapturesAdapter.getPosition().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView)view.findViewById(R.id.textView_ra_progress_feedback)).setText(
                        integer+"/"+resolveAnswersViewModel.getScannedCaptures().size()
                );
            }
        });
    }
}
