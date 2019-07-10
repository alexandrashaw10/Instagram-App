package com.codepath.instagram_app;

import android.app.Application;

import com.codepath.instagram_app.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        // TODO make sure that this code is needed
        // Use for monitoring Parse OkHttp traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        final Parse.Configuration config = new Parse.Configuration.Builder(this)
                .applicationId("qualified-insta")
                .clientKey("kcech-hill-quad-gregory")
                .server("http://ashaw-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(config);
    }
}
