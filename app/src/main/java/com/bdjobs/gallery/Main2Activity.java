package com.bdjobs.gallery;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main2Activity extends AppCompatActivity {
    ImageView imageView;
    String link;
    Bitmap theBitmap;
    String loc;
    String fname="";
    private ProgressDialog progress;
    Button button;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        imageView = (ImageView) findViewById(R.id.imgD);
        button= (Button) findViewById(R.id.downloadBTN);
        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        Glide.with(this).load(link).into(imageView);
        Rqpr();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String permission = sharedPref.getString("permission","0");
        if(permission.matches("permission_denied"))
        {
            button.setVisibility(View.INVISIBLE);
        }
        else if(permission.matches("permission_granted"))
        {
            button.setVisibility(View.VISIBLE);
        }


    }

    private void Rqpr() {
        if (ContextCompat.checkSelfPermission(Main2Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Main2Activity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
    }

    public void OnClickDownload(View view)
    {
        Rqpr();
        String condition = button.getText().toString();
        if(condition.matches("Download Wallpaper"))
        {
            new ImageDownload().execute();
        }
        else if (condition.matches("Set as Wallpaper"))
        {
            new SetWallpaper().execute();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("permission","permission_granted");
                    editor.commit();
                    button.setVisibility(View.VISIBLE);

                } else {

                    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("permission","permission_denied");
                    editor.commit();
                    button.setVisibility(View.INVISIBLE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void OnClickSetWallpaper(View view)
    {
        if (fname.matches(""))
        {
            Toast.makeText(Main2Activity.this, "You should download the wallpaper first!", Toast.LENGTH_SHORT).show();
        }
        else {
            new SetWallpaper().execute();
        }

    }

    private  class ImageDownload extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                theBitmap = Glide.
                        with(Main2Activity.this).
                        load(link).
                        asBitmap().
                        into(-1,-1). // Width and height
                        get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            SaveImage(theBitmap);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            button.setText("Set as Wallpaper");
            Toast.makeText(Main2Activity.this, "Downloaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            if(progress !=null)
            {
                progress = null;
            }
            progress=new ProgressDialog(Main2Activity.this);
            progress.setTitle("Please wait!");
            progress.setMessage("Downloading Waallpaper");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();

        }
    }

    private void SaveImage(Bitmap finalBitmap) {
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = "Image-"+ n;

        System.out.print("file name:"+fname);

       /* File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "wallpaper");*/
        loc = Environment.getExternalStorageDirectory().toString()
                + "/Evan";
        File path = new File(loc);
        path.mkdirs();

        if (!path.mkdirs()) {
           System.out.println("Directory not created");
        }


        File file = new File(path, fname +".jpg");

        if (file.exists ()) file.delete ();
        try {
            path.mkdirs();

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  class SetWallpaper extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = -1;
            System.out.print("file name:"+fname);
            final Bitmap b = BitmapFactory.decodeFile(loc+"/"+fname+".jpg", options);
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setBitmap(b);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            Toast.makeText(Main2Activity.this, "Wallpaper Changed Successfully", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            if(progress !=null)
            {
                progress = null;
            }
            progress=new ProgressDialog(Main2Activity.this);
            progress.setTitle("Please wait!");
            progress.setMessage("Setting Wallpaper");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();

        }
    }
}
