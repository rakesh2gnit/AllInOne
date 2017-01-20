package com.mycollection.rakesh.mycollection.permissionchecker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by gleecus on 1/19/17.
 */

public class PermissionChecker {

    static int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1001;
    static int PERMISSION_REQUEST_CAMERA = 1002;
    static int PERMISSION_REQUEST_CAMERA_AND_STORAGE = 1003;

    public static boolean checkCameraStoragePermission(Context mCtx) {

        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mCtx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions((Activity) mCtx, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CAMERA_AND_STORAGE);
                return false;
            } else if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(mCtx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mCtx, new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CAMERA);
                return false;
            } else if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(mCtx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions((Activity) mCtx, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CAMERA_AND_STORAGE);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean checkStoragePermission(Context mCtx) {

        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mCtx, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions((Activity) mCtx, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /*public void RequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == pCode) {
            //boolean flag = true;
           *//* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                for (int i = 0, len = permissions.length; i < len; i++)
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        flag = false;
            if (flag) {
                if (permissionListener != null)
                    permissionListener.permissionResult(true);
            } else if (permissionListener != null)
                permissionListener.permissionResult(false);
            finish();*//*
        }
    }*/
}
