package com.example.examscanner.components.scan_exam.detect_corners;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CornerDetectionCapturesAdapter extends FragmentStateAdapter {

    private List<MutableLiveData<CornerDetectedCapture>> cornerDetectedCaptures;
    private MutableLiveData<Integer> position;

    private MutableLiveData<Integer> mItemCount;

    private List<ProgressBarHandler> pbHandles;
    private ViewPager2 viewPager2;
    private LinkedList<PositionLink> positionLL;
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
//                currLinkPointer = viewsModel.get(position).second;
            }
        });
        pbHandles = new ArrayList<>();
        positionLL = new LinkedList<>();
        for (int i = 0; i <this.cornerDetectedCaptures.size() ; i++) {
            positionLL.addLast(new PositionLink(i,i));
        }
//        currLinkPointer = viewsModel.getFirst().second;
        mItemCount = new MutableLiveData<>(positionLL.size());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ProgressBarHandler pbh = new ProgressBarHandler(position);
        pbHandles.add(pbh);
        CornerDetectionCardFragment f =  new CornerDetectionCardFragment(pbh);
        Bundle b = new Bundle();
        b.putInt(captureId(), cornerDetectedCaptures.get(getOriginelPosition(position)).getValue().getId());
        f.setArguments(b);
        return f;
    }

    private int getOriginelPosition(int currentPosition){
        for (PositionLink link: positionLL) {
            if(link.getVirtualPosition()==currentPosition)return link.getOriginalPosition();
        }
        return -1;
    }

    public void notifiProcessBegun(int position){
        for (ProgressBarHandler pbh: pbHandles) {
            if (position==pbh.getPosition()) pbh.onProcessingBegun();
        }
    }

    public void handleProcessFinish(int position){
        notifiProcessFinished(position);
        for (PositionLink link: positionLL) {
            if(link.getVirtualPosition()>=position)
                link.setVirtualPosition(link.getVirtualPosition()-1);
        }
//        positionLL.remove(position);
        mItemCount.setValue(getVirtualSize());
        this.notifyItemRemoved(position);
    }

    private int getVirtualSize() {
        
    }

    public void notifiProcessFinished(int position){
        for (ProgressBarHandler pbh: pbHandles) {
            if (position==pbh.getPosition()) pbh.onProcessingFinished();
        }
    }

    @Override
    public long getItemId(int position) {
        long currentPos = super.getItemId(position);
        for (PositionLink link: positionLL) {
            if(currentPos==link.virtualPosition) return link.getOriginalPosition();
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mItemCount.getValue();
    }
    public LiveData<Integer> getmItemCount() {
        return mItemCount;
    }

    public LiveData<Integer> getPosition(){
        return position;
    }

    public static String captureId(){
        return "captureId";
    }

    public int getCDCaptureInPosition(int adapterBasedOPosition) {
        for (PositionLink link: positionLL) {
            if(link.getVirtualPosition()==adapterBasedOPosition)
                return cornerDetectedCaptures.get(link.getOriginalPosition()).getValue().getId();
        }
        return -1;
    }

    public class ProgressBarHandler implements com.example.examscanner.components.scan_exam.detect_corners.ProgressBarHandler{
        private View view;
        private int position;

        public ProgressBarHandler(int position) {
            this.position = position;
        }

        @Override
        public void onProcessingBegun() {
            ((ProgressBar)view.findViewById(R.id.progressBar2_scanning_answers)).setVisibility(View.VISIBLE);
        }

        @Override
        public void onProcessingFinished() {
            ((ProgressBar)view.findViewById(R.id.progressBar2_scanning_answers)).setVisibility(View.INVISIBLE);
        }

        @Override
        public void setContextView(View view) {
            this.view=view;
        }

        public int getPosition() {
            return position;
        }
    }

    private class PositionLink {
        private int originalPosition;
        private int virtualPosition;

        public void setVirtualPosition(int virtualPosition) {
            this.virtualPosition = virtualPosition;
        }

        public int getOriginalPosition() {
            return originalPosition;
        }

        public int getVirtualPosition() {
            return virtualPosition;
        }

        public PositionLink(int originalPosition, int currentPosition) {
            this.originalPosition = originalPosition;
            this.virtualPosition = currentPosition;
        }
    }
}
