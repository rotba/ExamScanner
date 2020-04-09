package com.example.examscanner.components.create_exam.get_version_file;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.examscanner.R;

import java.io.IOException;

class VersionImageImpl implements VersionImageGetter {

    private static final int PICKFILE_REQUEST_CODE = 0;
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "VersionImageImpl::";

    @Override
    public void get(FragmentActivity activity) {
        Intent intent = new Intent(Intent. ACTION_OPEN_DOCUMENT );
        intent.setType("image/jpeg");
        activity.startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    @Override
    public Bitmap accessBitmap(Intent data, FragmentActivity activity) {
        Uri uri = data.getData();
        try {
            return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            Log.d(TAG,MSG_PREF+" accessBitmap");
            e.printStackTrace();
        }
        throw new FailedGettingVersionImageException();
    }
}
