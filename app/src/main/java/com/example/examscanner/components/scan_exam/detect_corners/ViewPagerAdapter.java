package com.example.examscanner.components.scan_exam.detect_corners;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.CornerDetectedCapture;

import java.util.List;

public class ViewPagerAdapter extends  RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private List<MutableLiveData<CornerDetectedCapture>> mCornerDetectedCaptures;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;
    private MutableLiveData<Integer> position;
    ViewPagerAdapter(Context context, List<MutableLiveData<CornerDetectedCapture>> cornerDetectedCaptures, ViewPager2 viewPager) {
        this.mCornerDetectedCaptures = cornerDetectedCaptures;
        this.viewPager2 = viewPager;
        this.mInflater = LayoutInflater.from(context);
        this.position = new MutableLiveData<>(1);
        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ViewPagerAdapter.this.position.setValue(position+1);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_corner_detected_capture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(mCornerDetectedCaptures.get(position).getValue().getBitmap());
        this.position.setValue(position+1);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        System.out.println("ASd");
    }

    public LiveData<Integer> getPosition(){
        return position;
    }

    @Override
    public int getItemCount() {
        return mCornerDetectedCaptures.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2_corner_detected_capture);
        }

    }
}
