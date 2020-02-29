package com.example.examscanner.components.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.examscanner.R;
import com.example.examscanner.repositories.exam.Exam;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Exam> mExams;
    private static final String TAG = "MyAdapter";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView examName;
        RelativeLayout parentLayout;
        public MyViewHolder(RelativeLayout v) {
            super(v);
            examName = v.findViewById(R.id.exam_name);
            parentLayout = v;
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Exam> exams) {
        this.mExams = exams;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.examName.setText(mExams.get(position).toString());
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mExams.size();
    }
}
