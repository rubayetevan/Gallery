
package com.bdjobs.gallery;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Wallpaper {

    @SerializedName("wallpaper")
    @Expose
    private List<Wallpaper_> wallpaper = new ArrayList<Wallpaper_>();

    /**
     * 
     * @return
     *     The wallpaper
     */
    public List<Wallpaper_> getWallpaper() {
        return wallpaper;
    }

    /**
     * 
     * @param wallpaper
     *     The wallpaper
     */
    public void setWallpaper(List<Wallpaper_> wallpaper) {
        this.wallpaper = wallpaper;
    }

}
