package com.mycollection.rakesh.mycollection.permissionchecker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by gleecus on 1/19/17.
 */

public class PermissionChecker {

    static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1001;
    static final int PERMISSION_REQUEST_CAMERA_AND_STORAGE = 1003;

    public static void checkCameraStoragePermission(Context mCtx) {

        String[] perms = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

        ActivityCompat.requestPermissions((Activity) mCtx, perms, PERMISSION_REQUEST_CAMERA_AND_STORAGE);
    }

    public static void checkStoragePermission(Context mCtx) {

        String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};

        ActivityCompat.requestPermissions((Activity) mCtx, perms, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

}
