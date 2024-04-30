package com.example.pixelperfect.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
/**
 * 权限申请类
 */
public class PermissionsUtils {
    public static final String[] PERMISSIONS_EXTERNAL_WRITE = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int PERMISSION_REQUEST_CODE_WRITE_STORAGE = 3;
    /**
     * 检查写入存储权限是否已授予。
     *
     * @param activity 调用该方法的活动
     * @return 如果已授予写入存储权限，则返回 true；否则返回 false
     */
    public static boolean checkWriteStoragePermission(Activity activity) {
        // 如果设备运行 Android 10（API 级别 29）或更高版本，则始终返回 true，因为在 Android 10 及更高版本中，WRITE_EXTERNAL_STORAGE权限不再需要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true;
        }

        // 检查 WRITE_EXTERNAL_STORAGE 权限是否已授予
        boolean isPermissionGranted = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        // 如果权限未授予，则请求权限
        if (!isPermissionGranted) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_EXTERNAL_WRITE, PERMISSION_REQUEST_CODE_WRITE_STORAGE);
        }

        return isPermissionGranted;
    }
}
