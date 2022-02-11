package com.scrat.gogo.framework.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;

/**
 * Created by scrat on 16/5/12.
 */
public class BitmapUtil {

    public static boolean createImageThumbnail(String sourcePath, String targetPath) {
        Bitmap bitmap = createImageThumbnail(sourcePath);
        if (bitmap == null) {
            return false;
        }

        return saveImage(bitmap, targetPath);
    }

    public static Bitmap createImageThumbnail(String path) {
        return compressImage(800f, 0, true, false, path);
    }

    public static byte[] createImageThumbnailByte(String path) {
        Bitmap bmp = createImageThumbnail(path);
        if (bmp == null)
            return null;

        byte[] byteArray;
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
        } finally {
            close(stream);
        }

        return byteArray;
    }

    private static Bitmap compressImage(
            float size, int orientation, boolean scale, boolean isLow, String filePath) {
        Bitmap bmp = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bmp = BitmapFactory.decodeFile(filePath, options);

            float actualHeight = options.outHeight;
            float actualWidth = options.outWidth;

            float destHeight = size;
            float destWidth = size;
            // 解析过程出错，options.outHeight = -1
            if (actualHeight <= 0 || actualWidth <= 0 || size <= 0)
                return null;

            if (scale) {
                if (actualHeight > actualWidth) {
                    destWidth = (actualWidth * size) / actualHeight;
                    destHeight = size;
                } else if (actualWidth > actualHeight) {
                    destHeight = (actualHeight * size) / actualWidth;
                    destWidth = size;
                }
            }

            options.inSampleSize = calculateInSampleSize(
                    options.outWidth, options.outHeight,
                    destWidth, destHeight);
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            bmp = BitmapFactory.decodeFile(filePath, options);
            if (bmp == null)
                return null;

            Bitmap scaledBitmap = Bitmap.createBitmap(
                    (int) destWidth, (int) destHeight,
                    isLow ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888);

            float ratioX = destWidth / options.outWidth;
            float ratioY = destHeight / options.outHeight;
            float middleX = destWidth / 2.0f;
            float middleY = destHeight / 2.0f;
            if (!scale) {
                // 计算非缩放下的放大倍数及放大后中心点
                ratioX = ratioX > ratioY ? ratioX : ratioY;
                ratioY = ratioX;
                middleX = ((options.outWidth) * ratioX) / 2.0f;
                middleY = ((options.outHeight) * ratioY) / 2.0f;
            }

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            ColorMatrix mSaturationMatrix = new ColorMatrix();
            mSaturationMatrix.reset();
            mSaturationMatrix.setSaturation(1.3f);

            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            paint.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);

            float x = middleX - (bmp.getWidth()) / 2.0f;
            float y = middleY - (bmp.getHeight()) / 2.0f;
            if (!scale) {
                // 计算截取图片中心缩放中心坐标
                if (bmp.getWidth() > bmp.getHeight()) {
                    x = x - ((bmp.getWidth() - bmp.getHeight()) / 2.0f);
                } else {
                    y = y - ((bmp.getHeight() - bmp.getWidth()) / 2.0f);
                }
            }
            canvas.drawBitmap(bmp, x, y, paint);
            Matrix matrix = new Matrix();
            if (orientation == 90 || orientation == 180 || orientation == 270) {
                matrix.postRotate(orientation);
            }
            scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap,
                    0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            return scaledBitmap;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        } finally {
            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
                System.gc();
            }
        }
    }

    private static int calculateInSampleSize(
            float outWidth, float outHeight, float reqWidth, float reqHeight) {

        if (reqHeight == 0 || reqWidth == 0)
            return 1;

        int inSampleSize = 1;
        if (outHeight > reqHeight || outWidth > reqWidth) {
            final int heightRatio = Math.round(outHeight / reqHeight);
            final int widthRatio = Math.round(outWidth / reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = outWidth * outHeight;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    private static boolean saveImage(Bitmap photo, String path) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(path, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close(bos);
        }
    }

    private static void close(Closeable c) {
        if (c == null) {
            return;
        }

        try {
            c.close();
        } catch (Exception ignore) {
        }
    }
}
