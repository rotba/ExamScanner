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

import com.example.examscanner.R;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
/*
//TODO not in use maybe delete
 */
public class ScannedCaptureCardFragment extends Fragment {
    private int captureId;
    private ResolveAnswersViewModel resolveAnswersViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        captureId = getArguments().getInt(ScannedCapturesAdapter.positionKey());
        resolveAnswersViewModel = new ViewModelProvider(
                getActivity(),
                new ResolveAnswersViewModelFactory(getActivity())
        ).get(ResolveAnswersViewModel.class);
        View root = inflater.inflate(R.layout.item_scanned_capture, container, false);
        resolveAnswersViewModel.getScannedCapture(captureId).observe(getActivity(), new Observer<ScannedCapture>() {
            @Override
            public void onChanged(ScannedCapture scannedCapture) {
                ((TextView) root.findViewById(R.id.textView_identified)).setText(
                        "Identified: "+resolveAnswersViewModel.getScannedCapture(captureId).getValue().getIdentified()
                );
                ((TextView) root.findViewById(R.id.textView_unidentified)).setText(
                        "Unidentified: "+resolveAnswersViewModel.getScannedCapture(captureId).getValue().getUnidentified()
                );
            }
        });
        return root;
    }
}
