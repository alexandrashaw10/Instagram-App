package com.codepath.instagram_app;
import android.app.Application;

import com.codepath.instagram_app.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration config = new Parse.Configuration.Builder(this)
                .applicationId("qualified-insta")
                .clientKey("kcech-hill-quad-gregory")
                .server("http://ashaw-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(config);
    }
}
