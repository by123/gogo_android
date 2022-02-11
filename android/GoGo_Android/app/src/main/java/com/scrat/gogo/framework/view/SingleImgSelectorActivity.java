package com.scrat.gogo.framework.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scrat.gogo.framework.common.BaseActivity;
import com.scrat.gogo.framework.util.L;
import com.scrat.gogo.framework.util.Utils;

import java.io.File;

/**
 * Created by yixuanxuan on 2017/6/26.
 */

public class SingleImgSelectorActivity extends BaseActivity {

    private static final int REQUEST_CODE_SELECTOR_IMG = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final String PATH = "path";
    private static final String URI = "uri";
    private static final String MSG = "msg";

    public static void show(Activity activity, int requestCode) {
        Intent i = new Intent(activity, SingleImgSelectorActivity.class);
        activity.startActivityForResult(i, requestCode);
    }

    @NonNull
    @Override
    protected String getActivityName() {
        return "SingleImgSelectorActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean granted = Utils.checkPermission(
                this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION);

        if (granted) {
            selectImg();
        }
    }

    private void selectImg() {
        try {
            Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
            getImage.addCategory(Intent.CATEGORY_OPENABLE);
            getImage.setType("image/*");
            startActivityForResult(getImage, REQUEST_CODE_SELECTOR_IMG);
        } catch (Exception e) {
            L.e(e);
            finish(null, null, "未知错误");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) {
            finish(null, null, "已取消");
            return;
        }

        if (data == null || data.getData() == null) {
            finish(null, null, "获取图片信息失败");
            return;
        }

        Uri uri = data.getData();
        String path = Utils.getPath(this, uri);
        if (path == null) {
            finish(null, uri, "获取图片路径失败");
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            finish(path, uri, "文件不存在");
            return;
        }

        finish(path, uri, "ok");
    }

    @Nullable
    public static String parsePath(@NonNull Intent intent) {
        return intent.getStringExtra(PATH);
    }

    @NonNull
    public static String parseMsg(@NonNull Intent intent) {
        return intent.getStringExtra(MSG);
    }

    @Nullable
    public static Uri parseUri(@NonNull Intent intent) {
        return intent.getParcelableExtra(URI);
    }

    private void finish(String path, Uri uri, String msg) {
        Intent i = new Intent();
        i.putExtra(PATH, path);
        i.putExtra(URI, uri);
        i.putExtra(MSG, msg);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_CODE_PERMISSION) {
            return;
        }

        if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImg();
        } else {
            finish(null, null, "获取权限失败");
        }
    }

}
