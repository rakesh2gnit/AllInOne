package com.mycollection.rakesh.mycollection;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by gleecus on 12/8/16.
 */

public class FileManagementInAndroid extends BaseActivity {

    String filename = "myfile";
    String string = "Hello world!";
    FileOutputStream outputStream;
    String LOG_TAG = "PermissionCheckerActivity";
    ImageView iv_pic, imgView;
    Bitmap bitmap;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_filesavinginandroid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        imgView = (ImageView) findViewById(R.id.imgView);

        BitmapDrawable drawable = (BitmapDrawable) iv_pic.getDrawable();
        bitmap = drawable.getBitmap();

        //Save a file in internal storage by
        // 1)getFilesDir();  Returns a File representing an internal directory for your app.
        // 2)getCacheDir();  Returns a File representing an internal directory for your app's temporary cache files.

        //If you want to save public files on the external storage, use the getExternalStoragePublicDirectory()
        //If you want to save files that are private to your app on the external storage, you can acquire the appropriate directory by calling getExternalFilesDir()

        //If none of the pre-defined sub-directory names suit your files, you can instead call getExternalFilesDir() and pass null.
        //This returns the root directory for your app's private directory on the external storage.

        //Remember that getExternalFilesDir() creates a directory inside a directory that is deleted when the user uninstalls your app
        //if you want to keep the file after uninstall use getExternalStoragePublicDirectory()

        //call openFileOutput() to get a FileOutputStream that writes to a file in your internal directory.

        /*try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        ((Button) findViewById(R.id.btn1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File cacheDir = new File(getCacheDir(), "profilepic");
                if (cacheDir.exists()) {
                    for (File f : cacheDir.listFiles()) {
                        //perform here your operation
                        Log.e("file list:", f.getAbsolutePath());
                    }
                }
                // Log.e("file3:", "file created" + cacheDir.getAbsolutePath());
            }
        });

        ((Button) findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //trimCache(FileManagementInAndroid.this);
                File imgFile = new  File(getFilesDir() + File.separator + "MyDirName"+ File.separator + "myapp.webp");
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imgView.setImageBitmap(myBitmap);
                }
            }
        });

        ((Button) findViewById(R.id.createtempfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTempFile(FileManagementInAndroid.this, "myapp");
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1001);

        }

        if (isExternalStorageWritable()) {
            //getAlbumStorageDir("myapp");
            getAlbumStorageDir(this, "myapp");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                return;
        }
    }

    //For creating temp file
    public File getTempFile(Context context, String url) {
        File file = null;
        File cacheDir = null;
        try {
            //String fileName = Uri.parse(url).getLastPathSegment();
            cacheDir = new File(context.getCacheDir(), "profilepic");
            //data/user/0/com.mycollection.rakesh.mycollection/cache/profilepic
            if (!cacheDir.exists())
                cacheDir.mkdir();
            //file = File.createTempFile(fileName, null, context.getCacheDir());
            //file = File.createTempFile("prefix", "extension", context.getCacheDir());
            /*file = File.createTempFile(url, ".webp", cacheDir);
            if (!file.exists()) {
                file.createNewFile();
                Log.e("file3:", "file created");
            }*/

            ///data/user/0/com.mycollection.rakesh.mycollection/cache/myapp-938130105.tmp
            //delete after unistall app
            //.e("file3:", "file created" + file.getAbsolutePath());

        } catch (Exception e) {
            Log.e("file3:", "file not created");
            // Error while creating file
        }

        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)) {//mounted
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //Get file from external storage that save publicly
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        ///storage/emulated/0/Pictures/myapp
        //this is not deleted after uninstall the app
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    //Get file from external storage that save privately
    public File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        /*File fileDir = new File(Environment.getExternalStorageDirectory()
                + File.separator + "MyDirName");*/
        File fileDir = new File(getFilesDir()
                + File.separator + "MyDirName");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
            Log.e(LOG_TAG, "Directory not created" + fileDir);
        } else {
            Log.e(LOG_TAG, "Directory created" + fileDir);
        }


        String mRootPath = fileDir + File.separator + "myapp.webp";
        File file = new File(mRootPath);
        if (file.exists()) {
            file.delete();
        }

        try {
            OutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, output);
            output.flush();
            output.close();
//            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void onDestroy() {
        super.onDestroy();

        try {
            trimCache(this);
            // Toast.makeText(this,"onDestroy " ,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void trimCache(Context context) {
        try {
            File dir = new File(context.getCacheDir(), "profilepic");
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            for (String children : dir.list()) {
                boolean success = deleteDir(new File(dir, children));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }
}