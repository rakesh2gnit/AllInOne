package com.mycollection.rakesh.mycollection.permissionchecker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mycollection.rakesh.mycollection.BaseActivity;
import com.mycollection.rakesh.mycollection.R;
import com.mycollection.rakesh.mycollection.helper.Constant;

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
                if (PermissionChecker.checkStoragePermission(PermissionCheckerActivity.this))
                    openGallery();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {//For Camera
            @Override
            public void onClick(View v) {

                if (PermissionChecker.checkCameraStoragePermission(PermissionCheckerActivity.this))
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

        if (requestCode == PermissionChecker.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            //Toast.makeText(PermissionCheckerActivity.this, "PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    alr.showAlertDialog(this, "ALERT", "Storage permission is required for this app", false, R.drawable.dialog_success, true, "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alr.successdialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PermissionChecker.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                            } else {
                                openGallery();
                            }
                        }
                    }, "", null, "", null);
                } else {
                    Toast.makeText(this, "Go to settings and enable Storage permission.", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        } /*else if (requestCode == PermissionChecker.PERMISSION_REQUEST_CAMERA) {
            Toast.makeText(PermissionCheckerActivity.this, "PERMISSION_REQUEST_CAMERA", Toast.LENGTH_SHORT).show();

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    alr.showAlertDialog(this, "ALERT", "Camera permission is required for this app", false, R.drawable.dialog_success, true, "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alr.successdialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        PermissionChecker.PERMISSION_REQUEST_CAMERA);
                            } else {
                                dispatchTakePictureIntent();
                            }
                        }
                    }, "", null, "", null);
                } else {
                    Toast.makeText(this, "Go to settings and enable Camera permission.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }*/ else if (requestCode == PermissionChecker.PERMISSION_REQUEST_CAMERA_AND_STORAGE) {
            //Toast.makeText(PermissionCheckerActivity.this, "PERMISSION_REQUEST_CAMERA_AND_STORAGE", Toast.LENGTH_SHORT).show();

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    alr.showAlertDialog(this, "ALERT", "Storage Permissions is required for this app", false, R.drawable.dialog_success, true, "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alr.successdialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PermissionChecker.PERMISSION_REQUEST_CAMERA_AND_STORAGE);
                            } else {
                                dispatchTakePictureIntent();
                            }
                        }
                    }, "", null, "", null);
                } else {
                    Toast.makeText(this, "Go to settings and enable Storage permissions.", Toast.LENGTH_SHORT)
                            .show();
                }
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    alr.showAlertDialog(this, "ALERT", "Camera Permission is required for this app", false, R.drawable.dialog_success, true, "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alr.successdialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        PermissionChecker.PERMISSION_REQUEST_CAMERA);
                            } else {
                                dispatchTakePictureIntent();
                            }
                        }
                    }, "", null, "", null);
                } else {
                    Toast.makeText(this, "Go to settings and enable Camera permission.", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    alr.showAlertDialog(this, "ALERT", "Camera and Storage permissions is required for this app", false, R.drawable.dialog_success, true, "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alr.successdialog.dismiss();
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PermissionChecker.PERMISSION_REQUEST_CAMERA_AND_STORAGE);
                            } else {
                                dispatchTakePictureIntent();
                            }
                        }
                    }, "", null, "", null);
                } else {
                    Toast.makeText(this, "Go to settings and enable Camera and Storage permissions", Toast.LENGTH_SHORT)
                            .show();
                }
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