package com.example.examscanner.components.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examscanner.R;
import com.example.examscanner.repositories.exam.Exam;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<LiveData<Exam>> mExams;
    private static final String TAG = "MyAdapter";

    public MyAdapter(List<LiveData<Exam>> exams) {
        this.mExams = exams;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.examName.setText(mExams.get(position).getValue().toString());
    }

    @Override
    public int getItemCount() {
        return mExams.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView examName;
        RelativeLayout parentLayout;

        public MyViewHolder(RelativeLayout v) {
            super(v);
            examName = v.findViewById(R.id.exam_name);
            parentLayout = v;
        }

    }
}
