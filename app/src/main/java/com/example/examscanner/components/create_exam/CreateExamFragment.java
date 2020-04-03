package com.example.examscanner.components.create_exam;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.examscanner.R;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CreateExamFragment extends Fragment {
    private static String ExamScannerTAG = "TAG_ExamScanner::";
    private static String TAG = "CreateExamFragment";
    private CreateExamModelView viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_exam, container, false);
        return root;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgressBar pb = ((ProgressBar) view.findViewById(R.id.progressBar_create_exam));
        pb.setVisibility(View.VISIBLE);
        Completable.fromAction(this::createModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onModelCreated, error -> error.printStackTrace());
        ((Button) view.findViewById(R.id.button_create_exam_create)).setOnClickListener(new CreateClickListener());
    }


    private void createModel() {
        CEModelViewFactory factory = new CEModelViewFactory(this.getActivity());
        viewModel = new ViewModelProvider(this, factory).get(CreateExamModelView.class);
    }

    private void onModelCreated() {
        ProgressBar pb = getActivity().findViewById(R.id.progressBar_create_exam);
        pb.setVisibility(View.INVISIBLE);
        viewModel.getAddedVersions().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) getActivity().findViewById(R.id.textView_number_of_versions_added)).setText(integer.toString());
            }
        });
    }

    public void onExamCreated() {
        getActivity().findViewById(R.id.progressBar_create_exam).setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.create_exam_dialog_exam_created)
                .setPositiveButton(R.string.create_exam_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavHostFragment.findNavController(CreateExamFragment.this).popBackStack();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onError(Throwable throwable) {
        Log.d(TAG, String.format("%s Error commiting to exam creation", ExamScannerTAG));
        throwable.printStackTrace();
    }

    private class CreateClickListener implements View.OnClickListener {
        private final EditText courseName;
        private final RadioGroup semester;
        private final RadioGroup term;
        private final EditText year;
        private ProgressBar pb;

        public CreateClickListener() {
            this.courseName = getActivity().findViewById(R.id.editText_create_exam_course_name);
            this.semester = getActivity().findViewById(R.id.radioGroup_semester);
            this.term = getActivity().findViewById(R.id.radioGroup_term);
            this.year = getActivity().findViewById(R.id.editText_create_exam_year);
            pb = getActivity().findViewById(R.id.progressBar_create_exam);
        }


        @SuppressLint("CheckResult")
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            String termString = getSelectedRadioButtonString(term);
            String semesterString = getSelectedRadioButtonString(semester);
            pb.setVisibility(View.VISIBLE);
            Completable.fromAction(() -> create(courseName.getText().toString(), termString, semesterString, year.getText().toString()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(CreateExamFragment.this::onExamCreated, CreateExamFragment.this::onError);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void create(String courseName, String term, String semester, String year) {
            viewModel.create(courseName, term, semester, year);
        }

        @NotNull
        private String getSelectedRadioButtonString(RadioGroup rg) {
            return ((RadioButton) CreateExamFragment.this.getActivity().findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        }
    }
}
