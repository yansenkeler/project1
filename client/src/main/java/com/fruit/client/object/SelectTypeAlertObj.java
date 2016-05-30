package com.fruit.client.object;

/**
 * Created by user on 2016/3/24.
 */
public class SelectTypeAlertObj {
    private int image;
    private String title;

    public SelectTypeAlertObj(int mImage, String mTitle) {
        image = mImage;
        title = mTitle;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int mImage) {
        image = mImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }
}
