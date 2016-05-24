package com.bdjobs.gallery;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Tabriz on 24-May-16.
 */
public interface API {
    String baseURL = "https://www.dropbox.com/s/";

    //https://www.dropbox.com/s/j5bbbdxe3yl1qc8/wallpaper.json?dl=0

    @GET("j5bbbdxe3yl1qc8/wallpaper.json?raw=1")
    Call<Wallpaper> getData();

    class Factory {
        public static API api;

        public static API getInstance() {
            if (api == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(baseURL)
                        .build();
                api = retrofit.create(API.class);
                return api;
            } else {
                return api;
            }
        }
    }
}
