package app.mytweet.app;

import android.app.Application;

import app.mytweet.models.Portfolio;

/**
 * Created by Paddym1991 on 09/10/2017.
 */

public class MyTweetApp extends Application {

    public Portfolio portfolio;

    @Override
    public void onCreate() {
        super.onCreate();
        portfolio = new Portfolio();
    }
}
