package com.fruit.common.img;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by JOJO on 2015/11/15.
 */
public class FruitImageLoader extends ImageLoader{
    private static ImageLoader imageLoader = null;
    private static FruitImageLoader fruitImageLoader = null;


    private FruitImageLoader(){
        super();
        imageLoader = ImageLoader.getInstance();
    }

    public static synchronized FruitImageLoader getInstance(){
        if(fruitImageLoader==null){
            fruitImageLoader = new FruitImageLoader();
        }
        return fruitImageLoader;
    }

}
