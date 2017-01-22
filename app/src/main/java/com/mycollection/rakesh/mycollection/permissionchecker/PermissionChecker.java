package com.mycollection.rakesh.mycollection.permissionchecker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.mycollection.rakesh.mycollection.R;
import com.mycollection.rakesh.mycollection.adapter.CollectionAdapter;

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

    public static void onRequestPermissionResult(Context mCtx,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, OnRequestPermissionResult onPermissionResult){
        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            StringBuilder sb = new StringBuilder();
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mCtx, permission)) {
                    //denied
                    Log.e("denied", permission);
                } else {
                    if (ActivityCompat.checkSelfPermission(mCtx, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        if (permission.contains("Camera"))
                            sb.append("Camera");
                        if (permission.contains("WRITE_EXTERNAL_STORAGE"))
                            sb.append("Storage");
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                String message = "";
                if (requestCode == PermissionChecker.PERMISSION_REQUEST_CAMERA_AND_STORAGE) {
                    message = "You have forcefully denied camera or storage permissions for this action. \n Please open settings, go to permissions and allow them.";
                } else {
                    message = "You have forcefully denied " + sb + " permissions for this action. \n Please open settings, go to permissions and allow them.";
                }
                if (onPermissionResult != null) {
                    onPermissionResult.onPermissionDenied(message);
                }
            }
        } else {
            switch (requestCode) {
                //act according to the request code used while requesting the permission(s).
                case PERMISSION_REQUEST_CAMERA_AND_STORAGE:

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted && cameraAccepted) {
                        if (onPermissionResult != null) {
                            onPermissionResult.onPermissionGranted(PERMISSION_REQUEST_CAMERA_AND_STORAGE);
                        }
                    }
                    break;

                case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:

                    boolean extstorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (extstorageAccepted) {
                        if (onPermissionResult != null) {
                            onPermissionResult.onPermissionGranted(PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }
                    break;
            }
        }
    }

    public interface OnRequestPermissionResult{
        public void onPermissionGranted(int requestCode);
        public void onPermissionDenied(String message);
    }

}
