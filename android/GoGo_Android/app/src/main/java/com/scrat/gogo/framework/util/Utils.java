package com.scrat.gogo.framework.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.TypedValue;

import java.io.File;
import java.util.Calendar;

/**
 * Created by scrat on 2017/11/12.
 */

public class Utils {
    public static String formatDate(long ts) {

        long zeroTs = getZeroTs();

        if (ts > zeroTs) {
            return DateFormat.format("H:mm", ts).toString();
        }

        long yesterdayZeroTs = zeroTs - 1000 * 60 * 60 * 24;
        if (ts > yesterdayZeroTs) {
            return DateFormat.format("昨天 H:mm", ts).toString();
        }

        return DateFormat.format("M月d日 H:mm", ts).toString();

//        long now = System.currentTimeMillis();
//        long betweenSecond = (now - ts) / 1000L;
//        if (betweenSecond < 60) {
//            return "刚刚";
//        }
//
//        if (betweenSecond < 60 * 60) {
//            return String.format(Locale.getDefault(), "%d分钟前", betweenSecond / 60);
//        }
//
//        if (betweenSecond < 24 * 60 * 60) {
//            return String.format(Locale.getDefault(), "%d小时前", betweenSecond / 60 / 60);
//        }
//
//        long zeroSecond = getZeroTs() / 1000;
//        if (betweenSecond < zeroSecond - 24 * 60 * 60) {
//            return "昨天";
//        }
//
//        if (betweenSecond < zeroSecond - 2 * 24 * 60 * 60) {
//            return "前天";
//        }
//
//        return DateFormat.format("M月d日 H:mm", ts).toString();
    }

    private static long getZeroTs() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static float dpToPx(@NonNull Context context, float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static String getVersionName(Context applicationContext) {
        try {
            String pkName = applicationContext.getPackageName();
            return applicationContext.getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Context applicationContext) {
        try {
            String pkName = applicationContext.getPackageName();
            return applicationContext.getPackageManager().getPackageInfo(pkName, 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static boolean checkPermission(
            @NonNull Activity activity, String[] permissions, int requestCode) {

        boolean granted = true;
        for (String permission : permissions) {
            int permissionCode = ContextCompat.checkSelfPermission(activity, permission);
            if (permissionCode != PackageManager.PERMISSION_GRANTED) {
                granted = false;
            }
        }
        if (granted) {
            return true;
        }

        try {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }
        return null;
    }

    private static String getDataColumn(
            Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getSdCardAppDirPath(Context context) {
        String path = "/sdcard";
        File file = context.getExternalCacheDir();
        if (file != null) {
            path = file.getPath();
        }

        File f = new File(path + "/.ts_circle");
        if (f.exists()) {
            return f.getPath();
        }

        if (f.mkdir()) {
            return f.getPath();
        }

        return path;
    }

    public static void showVideo(Context context, Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "video/mp4");
            context.startActivity(intent);
        } catch (Exception e) {
            L.e(e);
        }
    }
}
