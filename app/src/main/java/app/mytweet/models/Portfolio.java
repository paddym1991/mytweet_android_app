package app.mytweet.models;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.pm.mytweet.R;

import java.util.ArrayList;

import static app.helpers.LogHelper.info;

public class Portfolio {

    public ArrayList<Tweet> tweets;
    //introduce the serializer as a member of the Portfolio class
    private PortfolioSerializer serializer;


    /**
     * Revise the constructor to take a serializer when it is being initialised
     * @param serializer
     */
    public Portfolio(PortfolioSerializer serializer)
    {
        this.serializer = serializer;
        try
        {
            tweets = serializer.loadTweets();
        }
        catch (Exception e)
        {
            info(this, "Error loading tweets: " + e.getMessage());
            tweets = new ArrayList<Tweet>();
            //this.generateTestData();
        }
    }

    /**
     * Method to add a tweet to the list
     * @param tweet
     */
    public void addTweet(Tweet tweet) {
        tweets.add(tweet);
    }

    public Tweet getTweet(Long id) {
        Log.i(getClass().getSimpleName(), "Long parameter id: " + id);

        for (Tweet tweet : tweets) {
            if (id.equals(tweet.id)) {
                return tweet;
            }
        }
        return null;
    }

    /**
     * Introduce a new method to save all the tweets to disk
     * @return
     */
    public boolean saveTweets()
    {
        try
        {
            serializer.saveTweets(tweets);
            info(this, "Tweets saved to file");
            return true;
        }
        catch (Exception e)
        {
            info(this, "Error saving tweets: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete tweet from list
     * @param tweet
     */
    public void deleteTweet(Tweet tweet)
    {
        tweets.remove(tweet);
        saveTweets();
    }

//    private void generateTestData() {
//
//        for (int i = 0; i < 5; i += 1) {
//            Tweet t = new Tweet();
//            t.tweetText = "Dummy Tweet";
//            tweets.add(t);
//        }
//    }
}


