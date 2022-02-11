package com.scrat.gogo.framework.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.util.L;
import com.scrat.gogo.framework.util.Utils;

import java.io.File;

/**
 * Created by yixuanxuan on 2017/6/23.
 */

public class CropPhotoActivity extends BaseActivity {

    private static final String PATH = "path";
    private static final String URI = "uri";
    private static final String ASPECT_X = "aspectX";
    private static final String ASPECT_Y = "aspectY";
    private static final int REQUEST_CODE_CROP_PHOTO = 1;

    public static void show(Activity activity, int requestCode, Uri uri, int aspectX, int aspectY) {
        Intent i = new Intent(activity, CropPhotoActivity.class);
        i.putExtra(URI, uri);
        i.putExtra(ASPECT_X, aspectX);
        i.putExtra(ASPECT_Y, aspectY);
        activity.startActivityForResult(i, requestCode);
    }

    private File cropPhotoFile;

    @NonNull
    @Override
    protected String getActivityName() {
        return "CropPhotoActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri sourceUri = getIntent().getParcelableExtra(URI);

        int aspectX = getIntent().getIntExtra(ASPECT_X, 0);
        int aspectY = getIntent().getIntExtra(ASPECT_Y, 0);

        cropPhotoFile = new File(Utils.getSdCardAppDirPath(this) + File.separator + "tmp.crop");
        Uri targetUri = Uri.fromFile(cropPhotoFile);
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(sourceUri, "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            intent.putExtra("circleCrop", false);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
            startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
        } catch (Exception e) {
            L.e(e);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }

        if (cropPhotoFile == null || !cropPhotoFile.exists()) {
            finish();
            return;
        }

        Intent i = new Intent();
        i.putExtra(PATH, cropPhotoFile.getPath());
        setResult(RESULT_OK, i);
        finish();
    }

    @Nullable
    public static String parsePath(@NonNull Intent data) {
        return data.getStringExtra(PATH);
    }
}
