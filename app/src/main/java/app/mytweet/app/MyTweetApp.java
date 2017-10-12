package app.mytweet.app;

import android.app.Application;
import static app.helpers.LogHelper.info;

import app.mytweet.models.Portfolio;
import app.mytweet.models.PortfolioSerializer;

/**
 * Created by Paddym1991 on 09/10/2017.
 */

public class MyTweetApp extends Application {

    public Portfolio portfolio;
    //introduce a field to hold the file name we will use to store the portfolio
    private static final String FILENAME = "portfolio.json";

    @Override
    public void onCreate() {
        super.onCreate();
        //portfolio creation
        PortfolioSerializer serializer = new PortfolioSerializer(this, FILENAME);
        portfolio = new Portfolio(serializer);

        info(this, "MyTweet app launched");
    }
}
