package com.example.examscanner.components.scan_exam.detect_corners;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.components.scan_exam.capture.CornerDetectedCapture;

import java.util.List;

public class CornerDetectionCapturesAdapter extends FragmentStateAdapter {

    private List<MutableLiveData<CornerDetectedCapture>> cornerDetectedCaptures;
    private MutableLiveData<Integer> position;
    private ViewPager2 viewPager2;

    public CornerDetectionCapturesAdapter(@NonNull FragmentActivity fragmentActivity, List<MutableLiveData<CornerDetectedCapture>> cornerDetectedCaptures, ViewPager2 viewPager2) {
        super(fragmentActivity);
        this.cornerDetectedCaptures = cornerDetectedCaptures;
        this.viewPager2 = viewPager2;
        this.position = new MutableLiveData<>(1);
        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                CornerDetectionCapturesAdapter.this.position.setValue(position+1);
            }
        });
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        CornerDetectionCardFragment f =  new CornerDetectionCardFragment();
        Bundle b = new Bundle();
        b.putInt(positionKey(), position);
        f.setArguments(b);
        return f;
    }

    @Override
    public int getItemCount() {
        return cornerDetectedCaptures.size();
    }

    public LiveData<Integer> getPosition(){
        return position;
    }

    public static String positionKey(){
        return "captureId";
    }
}
