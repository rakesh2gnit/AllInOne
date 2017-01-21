package com.mycollection.rakesh.mycollection.permissionchecker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mycollection.rakesh.mycollection.BaseActivity;
import com.mycollection.rakesh.mycollection.R;
import com.mycollection.rakesh.mycollection.helper.Constant;
import com.mycollection.rakesh.mycollection.util.AlertManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class PermissionCheckerActivity extends BaseActivity {

    private Button btn1, btn2, btn3;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private String dirGalleryPath;
    private AlertManager alr = new AlertManager();
    private boolean cameraAccepted = false;
    private boolean storageAccepted = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_permissionchecker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_permissionchecker);

        dirGalleryPath = getFilesDir().getAbsolutePath() + Constant.directoryGalleryPath;

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        mImageView = (ImageView) findViewById(R.id.imageView);

        btn1.setOnClickListener(new View.OnClickListener() {//For Gallery
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                    PermissionChecker.checkStoragePermission(PermissionCheckerActivity.this);
                else
                    openGallery();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {//For Camera
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                    PermissionChecker.checkCameraStoragePermission(PermissionCheckerActivity.this);
                else
                    dispatchTakePictureIntent();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getFilesDir().getAbsolutePath() + "/MyDirectory");
                File[] list = file.listFiles();
                int count = 0;
                for (File f : list) {
                    String name = f.getName();
                    if (name.endsWith(".jpg") || name.endsWith(".mp3") || name.endsWith(".some media extention"))
                        count++;
                    Toast.makeText(PermissionCheckerActivity.this, "" + count, Toast.LENGTH_SHORT).show();
                    System.out.println("170 " + count);
                }
            }
        });
    }

    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        //Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        //Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                    Log.e("denied", permission);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
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
                alr.showAlertDialog(this, "Permissions Required", message, false, R.drawable.dialog_info, false, "", null, "OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alr.successdialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, "Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alr.successdialog.dismiss();
                    }
                });
            }
        } else {
            switch (requestCode) {
                //act according to the request code used while requesting the permission(s).
                case PermissionChecker.PERMISSION_REQUEST_CAMERA_AND_STORAGE:

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted && cameraAccepted) {
                        dispatchTakePictureIntent();
                    }
                    break;

                case PermissionChecker.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:

                    boolean extstorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (extstorageAccepted) {
                        openGallery();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102 && resultCode == RESULT_OK) {
            Uri selectedUri = Uri.parse(mCurrentPhotoPath);
            getImageResolutionInPixel(selectedUri, mCurrentPhotoPath);
            mImageView.setImageURI(selectedUri);
        }
        if (requestCode == 101 && resultCode == RESULT_OK) {//It gives thumbnail
            //For getting same resolution image we must save that image into memory
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                Log.e("width:", String.valueOf(myBitmap.getWidth()));//Returns in pixel
                Log.e("height:", String.valueOf(myBitmap.getHeight()));
                String path = saveImageInDevice(myBitmap);
                Log.e("path:", path);
                Uri selectedUri = Uri.parse(path);
                getImageResolutionInPixel(selectedUri, path);
                // mImageView.setImageBitmap(myBitmap);
                mImageView.setImageURI(selectedUri);
            } catch (Exception e) {
                Log.e("Gallery:", "FileNotFoundException");
            }
        }
    }

    private void getImageResolutionInPixel(Uri uri, String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (path == null || path.length() == 0)
            path = uri.getPath();
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        Log.e("uri width:", String.valueOf(imageWidth));//Returns in pixel
        Log.e("uri height:", String.valueOf(imageHeight));
    }

    private String createDirectoryForGallery(String imageName) {
        String fileName = null;
        File storageDir = new File(dirGalleryPath);
        if (!storageDir.exists())
            storageDir.mkdirs();
        fileName = dirGalleryPath + "/" + imageName;
        return fileName;
    }

    private String saveImageInDevice(Bitmap imgBitmap) {
        String imageFileName = "IMG_" + UUID.randomUUID().toString().replaceAll("-", "") + ".webp";
        String imagePath = null;
        try {
            imagePath = createDirectoryForGallery(imageFileName);
            FileOutputStream fOut = new FileOutputStream(imagePath);
            // Here path is either sdcard or internal storage
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            //b.recycle();

            galleryAddPic(imagePath);

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
           /* MediaScannerConnection.scanFile(this,
                    new String[]{imagePath}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });*/
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        return imagePath;
    }

    private String createDirectoryForCamera(String imageName) {
        String fileName = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/MyDirectory/Camera");
        if (!storageDir.exists())
            storageDir.mkdirs();
        fileName = storageDir + "/" + imageName;
        return fileName;
    }

    private void dispatchTakePictureIntent() {
        //file:///storage/emulated/0/MyDirectory/Camera/IMG_5f73de4e8a504359968b1978f9fbb7c4.jpg
        String imageFileName = "IMG_" + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
        mCurrentPhotoPath = createDirectoryForCamera(imageFileName);
        Uri photoURI = Uri.fromFile(new File(mCurrentPhotoPath));

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            // Continue only if the File was successfully created
            if (photoURI != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 102);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "IMG_" + UUID.randomUUID().toString().replaceAll("-", "");
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //This only requires add images in device gallery
    //This code not scan file created by getExternalFilesDir
    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    //Decode a Scaled Image
    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

}