package com.example.examscanner.components.home;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examscanner.R;
import com.example.examscanner.repositories.exam.Exam;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private HomeViewModel homeViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((FloatingActionButton) root.findViewById(R.id.floating_button_home_create_exam)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = HomeFragmentDirections.actionNavHomeToCreateExamFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });
        return root;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((ProgressBar) view.findViewById(R.id.progressBar_home)).setVisibility(View.VISIBLE);//Always good to set some good feedback
        HViewModelFactory factory = new HViewModelFactory(this.getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.exams_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Observable.fromCallable(() -> {
            homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
            return homeViewModel.getExams();})
                .subscribeOn(Schedulers.io())//The DB access executes on a non-main-thread thread
                .observeOn(AndroidSchedulers.mainThread())//Upon completion of the DB-involved execution, the continuation runs on the main thread
                .subscribe(this::onExamsRetrival);

    }


    public void onExamsRetrival(List<LiveData<Exam>> exams) {
        mAdapter = new MyAdapter(exams);
        recyclerView.setAdapter(mAdapter);
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_home)).setVisibility(View.INVISIBLE);
    }

    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }

}