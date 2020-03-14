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
import androidx.lifecycle.ViewModelProviders;

import com.example.examscanner.R;

public class ResolveAnswersFragment extends Fragment {
    private ResolveAnswersViewModel resolveAnswersViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resolveAnswersViewModel = ViewModelProviders.of(
                this,
                new ResolveAnswersViewModelFactory(getActivity())
        ).get(ResolveAnswersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_resolve_answers, container, false);
        resolveAnswersViewModel.getNumOfIdentified().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView)root.findViewById(R.id.textView_num_of_identified)).setText(
                        "Identified: "+ resolveAnswersViewModel.getNumOfIdentified().getValue()
                );
            }
        });
        resolveAnswersViewModel.getNumOfUnidentified().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView)root.findViewById(R.id.textView_num_of_unidentified)).setText(
                        "Unidentified: "+ resolveAnswersViewModel.getNumOfUnidentified().getValue()
                );
            }
        });
        return inflater.inflate(R.layout.fragment_resolve_answers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextView)view.findViewById(R.id.textView_num_of_identified)).setText(
                "Identified: "+ resolveAnswersViewModel.getNumOfIdentified().getValue()
        );
        ((TextView)view.findViewById(R.id.textView_num_of_unidentified)).setText(
                "Unidentified: "+ resolveAnswersViewModel.getNumOfUnidentified().getValue()
        );
    }
}
