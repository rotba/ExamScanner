package com.example.examscanner.components.scan_exam.reslove_answers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

import java.util.List;

class ScannedCapturesAdapter extends RecyclerView.Adapter<ScannedCapturesAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<MutableLiveData<ScannedCapture>> scannedCaptures;
    private MutableLiveData<Integer> position;
    private ViewPager2 viewPager2;
    private FragmentActivity activity;
    public ScannedCapturesAdapter(FragmentActivity activity, List<MutableLiveData<ScannedCapture>> scannedCaptures, ViewPager2 viewPager) {
        this.scannedCaptures=scannedCaptures;
        this.activity=activity;
        this.mInflater = LayoutInflater.from(activity);
        this.viewPager2=viewPager;
        this.position = new MutableLiveData<>(1);
        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ScannedCapturesAdapter.this.position.setValue(position+1);
            }
        });
    }

    public MutableLiveData<Integer> getPosition() {
        return position;
    }

    public static String positionKey(){
        return "captureId";
    }

    @NonNull
    @Override
    public ScannedCapturesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_scanned_capture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        scannedCaptures.get(position).observe(activity, new Observer<ScannedCapture>() {
            @Override
            public void onChanged(ScannedCapture scannedCapture) {
                holder.identifeidTextView.setText(
                        "Identified: "+scannedCapture.getIdentified()
                );
                holder.unidentifeidTextView.setText(
                        "Unidentified: "+scannedCapture.getUnidentified()
                );
            }
        });
    }


    @Override
    public int getItemCount() {
        return scannedCaptures.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView identifeidTextView;
        TextView unidentifeidTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            identifeidTextView = itemView.findViewById(R.id.textView_identified);
            unidentifeidTextView = itemView.findViewById(R.id.textView_unidentified);
        }

    }
}
