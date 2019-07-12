package com.example.parstagram;

import android.app.Application;

import com.example.parstagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("parstagram") // should correspond to APP_ID env variable
                .clientKey("beyonce-summertime")  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("http://hanat18-fbu-instagram.herokuapp.com/parse")
                .build());
    }
}
