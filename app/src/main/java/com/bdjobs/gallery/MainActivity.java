package com.bdjobs.gallery;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    GridView grid;
    ArrayList<String> links = new ArrayList<>();
    private List<Wallpaper_> wallpapers = new ArrayList<Wallpaper_>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = (GridView) findViewById(R.id.grid);
        getServerData();

    }

    private void getServerData() {
        API.Factory.getInstance().getData().enqueue(new Callback<Wallpaper>() {
            @Override
            public void onResponse(Call<Wallpaper> call, Response<Wallpaper> response) {
                wallpapers=response.body().getWallpaper();

                for(int i=0;i<wallpapers.size();i++)
                {
                    links.add(i,wallpapers.get(i).getPicurl());
                }
                grid.setAdapter(new GridAdapter(MainActivity.this,links));
            }

            @Override
            public void onFailure(Call<Wallpaper> call, Throwable t) {

            }
        });
    }

    private class GridAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> links = new ArrayList<>();

        public GridAdapter(Context context, ArrayList<String> links) {
            this.context = context;
            this.links = links;
        }

        @Override
        public int getCount() {
            return links.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder holder = new Holder();

                convertView = getLayoutInflater().inflate(R.layout.grid_item, parent, false);
                holder.img = (ImageView) convertView.findViewById(R.id.imgv);


            Glide.with(context)
                    .load(links.get(position))
                    .override(200,200)
                    .into(holder.img);

            final String ln = links.get(position);

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,Main2Activity.class);
                    intent.putExtra("link",ln);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,holder.img,holder.img.getTransitionName()).toBundle();
                    startActivity(intent,bundle);
                }
            });

            setAnimation(holder.img,position);
            return convertView;
        }
        public class Holder
        {
            TextView tv;
            ImageView img;
        }
        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            //lastPosition = position;

        }


    }
}
