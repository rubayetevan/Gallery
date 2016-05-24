package com.bdjobs.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main2Activity extends AppCompatActivity {
    ImageView imageView;
    String link;
    Bitmap theBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        imageView = (ImageView) findViewById(R.id.imgD);
        Intent intent = getIntent();
        link = intent.getStringExtra("link");


        Glide.with(this).load(link).into(imageView);

    }

    public void OnClickDownload(View view)
    {

        new ImageDownload().execute();


    }

    private  class ImageDownload extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                theBitmap = Glide.
                        with(Main2Activity.this).
                        load(link).
                        asBitmap().
                        into(320,640). // Width and height
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
            Toast.makeText(Main2Activity.this, "Downloaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {

        }
    }

    private void SaveImage(Bitmap finalBitmap) {

       /* String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();*/
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, fname +".jpg");

        if (file.exists ()) file.delete ();
        try {
            path.mkdirs();

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
