package com.example.examscanner.components.scan_exam.reslove_answers.resolve_conflicted_answers;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.repositories.scanned_capture.ConflictedAnswer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

import java.util.ArrayList;
import java.util.List;

class ConflictedAnswersAdapter extends RecyclerView.Adapter<ConflictedAnswersAdapter.ViewHolder> {
    private MutableLiveData<ScannedCapture> scannedCapture;

    private List<ResolutionSubscriber> resolutionSubscribers;

    private final ViewPager2 viewPager;
    private LayoutInflater inflater;
    private MutableLiveData<Integer> position;
    public ConflictedAnswersAdapter(FragmentActivity activity, MutableLiveData<ScannedCapture> scannedCapture, ViewPager2 viewPager) {
        this.inflater = LayoutInflater.from(activity);
        this.scannedCapture = scannedCapture;
        this.viewPager = viewPager;
        position = new MutableLiveData<>();
        this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ConflictedAnswersAdapter.this.position.setValue(position+1);
            }
        });
        resolutionSubscribers = new ArrayList<>();
    }

    public List<ResolutionSubscriber> getResolutionSubscribers() {
        return resolutionSubscribers;
    }

    public MutableLiveData<Integer> getPosition() {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_conflicted_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConflictedAnswer ca = scannedCapture.getValue().getConflictedAnswers().get(position);
        Bitmap bm = scannedCapture.getValue().getBm();
        holder.answerFrameImageView.setImageBitmap(generateCAnswerFrame(ca,bm));
        resolutionSubscribers.add(new ResolutionSubscriber(position+1) {
            @Override
            public void onResolution(Choice c) {
                switch (c){
                    case NO_ANSWER:
                        holder.resolutionTextView.setText("No Answer");
                        return;
                    default:
                        holder.resolutionTextView.setText(Integer.toString(c.value));
                }
            }
        });
    }

    private Bitmap generateCAnswerFrame(ConflictedAnswer ca, Bitmap bm) {
        int firstX = (int)(ca.getUpperLeft().x *bm.getWidth());
        int firstY = (int)(ca.getUpperLeft().y *bm.getHeight());
        int width =  (int)(ca.getBottomRight().x *bm.getWidth()) -firstX;
        int height =  (int)(ca.getBottomRight().y *bm.getHeight()) -firstY;
        return Bitmap.createBitmap(bm, firstX,firstY,width, height);
    }

    @Override
    public int getItemCount() {
        return scannedCapture.getValue().getConflictedAnswers().size();
    }

    public ResolutionSubscriber getCurrentCAResolutionSubscriber() {
        for (ResolutionSubscriber rs: resolutionSubscribers) {
            if(rs.isCurrent(getPosition())){
                return rs;
            }
        }
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView answerFrameImageView;
        TextView resolutionTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerFrameImageView =itemView.findViewById(R.id.imageView_conflicted_answer_frame);
            resolutionTextView = itemView.findViewById(R.id.textView_resolution);
        }
    }

    public abstract class ResolutionSubscriber {
        private int poistion;

        public ResolutionSubscriber(int poistion) {
            this.poistion = poistion;
        }
        public abstract  void onResolution(Choice c);

        public boolean isCurrent(MutableLiveData<Integer> position){return  position.getValue()==this.poistion;}
    }
}
