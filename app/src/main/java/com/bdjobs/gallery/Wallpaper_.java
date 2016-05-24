
package com.bdjobs.gallery;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wallpaper_ {

    @SerializedName("picurl")
    @Expose
    private String picurl;

    /**
     * 
     * @return
     *     The picurl
     */
    public String getPicurl() {
        return picurl;
    }

    /**
     * 
     * @param picurl
     *     The picurl
     */
    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

}
