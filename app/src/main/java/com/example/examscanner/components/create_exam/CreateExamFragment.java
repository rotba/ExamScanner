package com.example.examscanner.components.create_exam;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.examscanner.R;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetter;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CreateExamFragment extends Fragment {
    public static final int PICKFILE_REQUEST_CODE = 0;
    private static String MSG_PREF = "CreateExamFragment::";
    private static String TAG = "ExamScanner";
    private CreateExamModelView viewModel;
    private VersionImageGetter versionImageGetter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_exam, container, false);
        versionImageGetter = new VersionImageGetterFactory().create();
        return root;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgressBar pb = ((ProgressBar) view.findViewById(R.id.progressBar_create_exam));
        pb.setVisibility(View.VISIBLE);
        ((Button) getActivity().findViewById(R.id.button_create_exam_add_version)).setEnabled(false);
        Completable.fromAction(this::createModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onModelCreated, this::onModelCreatedError);

        final Button createExamButton = (Button) view.findViewById(R.id.button_create_exam_create);
        createExamButton.setEnabled(false);
        createExamButton.setOnClickListener(new CreateClickListener());
        ((Button) view.findViewById(R.id.button_create_exam_upload_version_image)).setOnClickListener(this::onChooseVersionPdfClick);
        ((EditText) view.findViewById(R.id.editText_create_exam_version_number)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.holdVersionNumber(Integer.valueOf(s.toString()));
                refreshAddVersionButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ((Button) view.findViewById(R.id.button_create_exam_add_version)).setOnClickListener(this::onAddVersion);
    }

    private void refreshCreateExamButton() {
        ((Button) getActivity().findViewById(R.id.button_create_exam_create)).setEnabled(
                viewModel.getAddedVersions().getValue() > 0 &&
                        ((TextView) getActivity().findViewById(R.id.editText_create_exam_course_name)).getText() != null &&
                        ((TextView) getActivity().findViewById(R.id.editText_create_exam_year)).getText() != null &&
                        ((RadioGroup) getActivity().findViewById(R.id.radioGroup_semester)).getCheckedRadioButtonId() != -1 &&
                        ((RadioGroup) getActivity().findViewById(R.id.radioGroup_term)).getCheckedRadioButtonId() != -1

        );
    }

    private void refreshAddVersionButton() {
        ((Button) getActivity().findViewById(R.id.button_create_exam_add_version)).setEnabled(
                viewModel.getCurrentVersionNumber() != null && viewModel.getCurrentVersionBitmap() != null
        );
    }

    public void onChooseVersionPdfClick(View v) {
        versionImageGetter.get(this, PICKFILE_REQUEST_CODE);
    }

    public void onAddVersion(View v) {
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.VISIBLE);
        Completable.fromAction(() -> viewModel.addVersion())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onVersionAdded, this::onVersionAddedFailed);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE) {
            //create pdf document object from bytes
            ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.VISIBLE);
            Bitmap bitmap = versionImageGetter.accessBitmap(data, getActivity());
            viewModel.holdVersionBitmap(bitmap);
            ((ImageView) getActivity().findViewById(R.id.imageView_create_exam_curr_version_img)).setImageBitmap(bitmap);
            ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.INVISIBLE);
            refreshAddVersionButton();
        }
    }

    private void onVersionAddedFailed(Throwable throwable) {
        Log.d(TAG, MSG_PREF);
        throwable.printStackTrace();
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.INVISIBLE);
    }

    private void onVersionAdded() {
        ((ImageView) getActivity().findViewById(R.id.imageView_create_exam_curr_version_img)).clearAnimation();
        ((TextView) getActivity().findViewById(R.id.textView_create_exam_version_num)).clearComposingText();
        viewModel.holdVersionNumber(null);
        viewModel.holdVersionBitmap(null);
        ((ImageView) getActivity().findViewById(R.id.imageView_create_exam_curr_version_img)).setImageResource(0);
        refreshAddVersionButton();
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.INVISIBLE);
        viewModel.incNumOfVersions();
        refreshCreateExamButton();
    }

    private void onModelCreatedError(Throwable throwable) {
        Log.d(TAG, MSG_PREF + "onModelCreatedError");
        throwable.printStackTrace();
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
        Log.d(TAG, MSG_PREF + "onError");
        throwable.printStackTrace();
    }

    private class CreateClickListener implements View.OnClickListener {
        private final EditText courseName;
        private final RadioGroup semester;
        private final RadioGroup term;
        private final EditText year;
        private ProgressBar pb;
        private TextWatcher textWatcher;
        private RadioGroup.OnCheckedChangeListener radioButtonListner;

        public CreateClickListener() {
            this.courseName = getActivity().findViewById(R.id.editText_create_exam_course_name);
            this.semester = getActivity().findViewById(R.id.radioGroup_semester);
            this.term = getActivity().findViewById(R.id.radioGroup_term);
            this.year = getActivity().findViewById(R.id.editText_create_exam_year);
            pb = getActivity().findViewById(R.id.progressBar_create_exam);
            radioButtonListner = new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    refreshAddVersionButton();
                }
            };
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    refreshCreateExamButton();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };
            courseName.addTextChangedListener(textWatcher);
            year.addTextChangedListener(textWatcher);
            semester.setOnCheckedChangeListener(radioButtonListner);
            term.setOnCheckedChangeListener(radioButtonListner);
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
