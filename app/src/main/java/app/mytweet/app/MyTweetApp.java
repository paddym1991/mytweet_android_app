package app.mytweet.app;

import android.app.Application;
import static app.helpers.LogHelpers.info;

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

        info(this, "MyTweet app launched");
    }
}
