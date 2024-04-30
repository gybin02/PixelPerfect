package com.example.pixelperfect.Sticker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class StickerUtils {

    public static void notifySystemGallery(@NonNull Context context, @NonNull File file) {
        if (file != null && file.exists())
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
                return;
            } catch (FileNotFoundException fileNotFoundException) {
                throw new IllegalStateException("File couldn't be found");
            }
        throw new IllegalArgumentException("bmp should not be null");
    }

    public static File saveImageToGallery(@NonNull File file, @NonNull Bitmap bitmap) {
        if (bitmap != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("saveImageToGallery: the path of bmp is ");
            stringBuilder.append(file.getAbsolutePath());
            Log.e("StickerView", stringBuilder.toString());
            return file;
        }
        throw new IllegalArgumentException("bmp should not be null");
    }


    /**
     * 将给定的点数组限制在一个矩形范围内。
     *
     * @param rect 限制后的矩形范围
     * @param points 给定的点数组
     */
    public static void trapToRect(@NonNull RectF rect, @NonNull float[] points) {
        // 初始化矩形范围为正无穷到负无穷
        rect.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        // 遍历点数组，更新矩形范围
        for (int i = 1; i < points.length; i += 2) {
            // 获取当前点的 x 和 y 坐标，并四舍五入保留一位小数
            float x = Math.round(points[i - 1] * 10.0F) / 10.0F;
            float y = Math.round(points[i] * 10.0F) / 10.0F;

            // 更新矩形范围的左上角坐标
            rect.left = Math.min(x, rect.left);
            rect.top = Math.min(y, rect.top);

            // 更新矩形范围的右下角坐标
            rect.right = Math.max(x, rect.right);
            rect.bottom = Math.max(y, rect.bottom);
        }

        // 对矩形范围进行排序
        rect.sort();
    }
}
