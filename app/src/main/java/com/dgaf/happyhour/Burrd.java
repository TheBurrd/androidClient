package com.dgaf.happyhour;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

/**
 * Created by Adam on 5/19/2015.
 */
public class Burrd extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // Setup Model objects
        ParseObject.registerSubclass(DealModel.class);
        ParseObject.registerSubclass(RestaurantModel.class);
        // Setup Parse db connection
        Parse.initialize(this, "aOa7pfDy6GLtckl4cYBEMCnkBW9NyDLZ7ta4FVoI", "SRlu6KjPyWSVvge8NPZ5NU78lH5LCT1Ve5qXQHOY");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }
}