package com.dgaf.happyhour;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.Model.UserModel;
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
        ParseObject.registerSubclass(UserModel.class);
        // Production Database
        //Parse.initialize(this, "2TR1bOpjpFL8Wm7a7TXcbuoFCyJRB0VdSOXnf8tB", "hY2oFPc3k8RJLsZIsadM1Me0JS1ndnwRwim104WK");
        // Setup Parse db connection
        Parse.initialize(this, "zjbuJvWrvzgdpDvRnHejLD008hLGf6zHua5nCGvq", "7DegaPYh670uARgycPIDmt6yQgM2x6XvRDo5o6vI");

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