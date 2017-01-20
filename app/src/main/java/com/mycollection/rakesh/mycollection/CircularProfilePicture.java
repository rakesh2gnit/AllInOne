package com.mycollection.rakesh.mycollection;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mycollection.rakesh.mycollection.helper.Constant;
import com.mycollection.rakesh.mycollection.library.gallery.ImagePicker;
import com.mycollection.rakesh.mycollection.library.gallery.ImagePickerActivity;
import com.mycollection.rakesh.mycollection.library.gallery.helper.ImageUtils;
import com.mycollection.rakesh.mycollection.library.gallery.model.Folder;
import com.mycollection.rakesh.mycollection.library.gallery.model.Image;
import com.mycollection.rakesh.mycollection.util.AlertManager;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.angle;
import static android.R.attr.pivotX;
import static android.R.attr.pivotY;

/**
 * Created by rakesh on 11/16/16.
 */

public class CircularProfilePicture extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CircularProfilePicture";

    private LinearLayout mainLayout;
    private String imageDirectory;
    private String currentImagePath;

    private Thread thread;

    private TextView txt_profile_pic;
    private ImageView iv_pic;
    private ArrayList<Image> images = new ArrayList<>();
    AlertManager alr = new AlertManager();

    @Override
    protected int getLayoutResId() {
        return R.layout.act_circular_pic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar_title.setText("Circular Profile Pics");
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageDirectory = getIntent().getStringExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_DIRECTORY);
        if (imageDirectory == null || TextUtils.isEmpty(imageDirectory)) {
            imageDirectory = getString(R.string.image_directory);
        }

        mainLayout = (LinearLayout) findViewById(R.id.main);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        txt_profile_pic = (TextView) findViewById(R.id.txt_profile_pic);
        iv_pic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pic:
                alr.showConfirmationAlert(
                        CircularProfilePicture.this,
                        "What do you want?", "To change profile picture select", "Camera",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alr.confirmationdialog.dismiss();
                                captureImageWithPermission();

                            }
                        }, "Gallery", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alr.confirmationdialog.dismiss();
                                ArrayList<Image> images = new ArrayList<>();
                                ImagePicker.create(CircularProfilePicture.this)
                                        .folderMode(true) // set folder mode (false by default)
                                        .folderTitle("Folder") // folder selection title
                                        .imageTitle("Tap to select") // image selection title
                                        .single() // single mode
                                        .multi() // multi mode (default mode)
                                        .limit(1) // max images can be selected (99 by default)
                                        .showCamera(true) // show camera or not (true by default)
                                        .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                                        .origin(images) // original selected images, used in multi mode
                                        .start(Constant.REQUEST_CODE_PICKER); // start image picker activity with request code
                            }
                        }, false);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0, l = images.size(); i < l; i++) {
                //stringBuffer.append(images.get(i).getPath() + "\n");
                stringBuffer.append(images.get(i).getPath());
            }
            iv_pic.setImageBitmap(BitmapFactory.decodeFile(stringBuffer.toString()));
            txt_profile_pic.setVisibility(View.GONE);
        } else if (requestCode == Constant.REQUEST_CODE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = Uri.parse(currentImagePath);
                if (imageUri != null) {
                    Bitmap bmpSource = BitmapFactory.decodeFile(imageUri.getPath());
                    int rotate = getCameraPhotoOrientation(this,
                            Uri.fromFile(new File(imageUri.getPath())), imageUri.getPath());
                    if (rotate != 0) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(rotate);
                        bmpSource = Bitmap.createBitmap(bmpSource, 0, 0,
                                bmpSource.getWidth(), bmpSource.getHeight(), matrix,
                                true);
                    }
                    //iv_pic.setImageBitmap(BitmapFactory.decodeFile(imageUri.getPath()));
                    iv_pic.setImageBitmap(bmpSource);
                    txt_profile_pic.setVisibility(View.GONE);
                    MediaScannerConnection.scanFile(this,
                            new String[]{imageUri.getPath()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    //Log.v(TAG, "File " + path + " was scanned successfully: " + uri);
                                }
                            });
                }
            }
        }
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri,
                                                String imagePath) {
        int rotate = 0;
        try {
            if(context!=null && imageUri!=null){
                context.getContentResolver().notifyChange(imageUri, null);
            }
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            Log.d("Exif orientation: ", orientation + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }


    /**
     * Request for camera permission
     */
    private void captureImageWithPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (rc == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else {
                Log.w(TAG, "Camera permission is not granted. Requesting permission");
                requestCameraPermission();
            }
        } else {
            captureImage();
        }
    }

    /**
     * Start camera intent
     * Create a temporary file and pass file Uri to camera intent
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File imageFile = ImageUtils.createImageFile(imageDirectory);
            if (imageFile != null) {
                Uri uri = FileProvider.getUriForFile(this, getString(R.string.shared_file_provider), imageFile);
                currentImagePath = "file:" + imageFile.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, Constant.REQUEST_CODE_CAPTURE);
            } else {
                Toast.makeText(this, getString(R.string.error_create_image_file), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
        }
    }

    private void requestCameraPermission() {
        //Log.w(TAG, "Write External permission is not granted. Requesting permission");

        final String[] permissions = new String[]{android.Manifest.permission.CAMERA};

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, Constant.PERMISSION_REQUEST_CAMERA);
        } else {
            if (isPermissionRequested(Constant.PREF_CAMERA_REQUESTED) == false) {
                ActivityCompat.requestPermissions(this, permissions, Constant.PERMISSION_REQUEST_CAMERA);
                setPermissionRequested(Constant.PREF_CAMERA_REQUESTED);
            } else {
                Snackbar snackbar = Snackbar.make(mainLayout, R.string.msg_no_camera_permission,
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAppSettings();
                    }
                });
                snackbar.show();
            }
        }
    }

    /**
     * Set a permission is requested
     */
    private void setPermissionRequested(String permission) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(permission, true);
        editor.commit();
    }

    /**
     * Check if a permission is requestted or not (false by default)
     */
    private boolean isPermissionRequested(String permission) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(permission, false);
    }

    /**
     * Open app settings screen
     */
    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Request for permission
     * If permission denied or app is first launched, request for permission
     * If permission denied and user choose 'Nerver Ask Again', show snackbar with an action that navigate to app settings
     */
    private void requestWriteExternalPermission() {
        Log.w(TAG, "Write External permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, permissions, Constant.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            if (isPermissionRequested(Constant.PREF_WRITE_EXTERNAL_STORAGE_REQUESTED) == false) {
                ActivityCompat.requestPermissions(this, permissions, Constant.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                setPermissionRequested(Constant.PREF_WRITE_EXTERNAL_STORAGE_REQUESTED);
            } else {
                Snackbar snackbar = Snackbar.make(mainLayout, R.string.msg_no_write_external_permission,
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAppSettings();
                    }
                });
                snackbar.show();
            }
        }

    }

    /**
     * Stop loading data task
     */
    private void abortLoading() {
        if (thread == null)
            return;
        if (thread.isAlive()) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
