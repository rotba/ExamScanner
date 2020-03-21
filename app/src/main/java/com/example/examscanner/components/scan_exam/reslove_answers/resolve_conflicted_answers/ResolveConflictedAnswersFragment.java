package com.example.examscanner.components.scan_exam.reslove_answers.resolve_conflicted_answers;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersViewModel;
import com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ResolveConflictedAnswersFragment extends Fragment {
    private ResolveAnswersViewModel resolveAnswersViewModel;
    private int scanId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        scanId = ResolveConflictedAnswersFragmentArgs.fromBundle(getArguments()).getScanId();
        ResolveAnswersViewModelFactory factory = new ResolveAnswersViewModelFactory(getActivity());
        View root = inflater.inflate(R.layout.fragment_resolve_one_scane, container, false);
        resolveAnswersViewModel = new ViewModelProvider(getActivity(), factory).get(ResolveAnswersViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.viewPager2_conflicted_answers);
        ConflictedAnswersAdapter conflictedAnswersAdapter =
                new ConflictedAnswersAdapter(getActivity(), resolveAnswersViewModel.getScannedCapture(scanId), viewPager);
        viewPager.setAdapter(conflictedAnswersAdapter);
        conflictedAnswersAdapter.getPosition().observe(getActivity(), new Observer<Integer>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(Integer integer) {
                ((TextView) view.findViewById(R.id.textView_ca_current_position_feedback)).setText(
                        integer + "/" + resolveAnswersViewModel.getScannedCapture(scanId).getValue().getConflictedAmount()
                );
            }
        });
        for (Button b : getChoiceButtons(view)) {
            b.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    Choice c = getChoice(b);
                    conflictedAnswersAdapter.getCurrentCAResolutionSubscriber().onResolution(c);
                    waitABitAndSwipeLeft(viewPager, conflictedAnswersAdapter);
                }
            });
        }
    }

    @SuppressLint("CheckResult")
    private void waitABitAndSwipeLeft(ViewPager2 viewPager, ConflictedAnswersAdapter conflictedAnswersAdapter) {
        Observable.fromCallable(() -> {
                    Thread.sleep(100);
                    return "done";
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        viewPager.setCurrentItem(conflictedAnswersAdapter.getPosition().getValue());
                    }
                });
    }


    @Override
    public void onDestroy() {
        resolveAnswersViewModel.commitResolutions(scanId);
        super.onDestroy();
    }

    private Choice getChoice(Button b) {
        switch (b.getId()) {
            case R.id.button_answer_1:
                return Choice.ONE;
            case R.id.button_answer_2:
                return Choice.TWO;
            case R.id.button_answer_3:
                return Choice.THREE;
            case R.id.button_answer_4:
                return Choice.FOUR;
            case R.id.button_answer_5:
                return Choice.FIVE;
            case R.id.button_answer_6:
                return Choice.SIX;
            case R.id.button_answer_7:
                return Choice.SEVEN;
            case R.id.button_no_answer:
                return Choice.NO_ANSWER;
            default:
                return Choice.NO_ANSWER;
        }
    }

    private List<Button> getChoiceButtons(View view) {
        List<Button> ans = new ArrayList<>();
        ans.add(view.findViewById(R.id.button_answer_1));
        ans.add(view.findViewById(R.id.button_answer_2));
        ans.add(view.findViewById(R.id.button_answer_3));
        ans.add(view.findViewById(R.id.button_answer_4));
        ans.add(view.findViewById(R.id.button_answer_5));
        ans.add(view.findViewById(R.id.button_answer_6));
        ans.add(view.findViewById(R.id.button_answer_7));
        ans.add(view.findViewById(R.id.button_no_answer));
        return ans;

    }
}
