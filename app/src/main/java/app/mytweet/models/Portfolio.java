package app.mytweet.models;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.pm.mytweet.R;

import java.util.ArrayList;

public class Portfolio {

    public ArrayList<Tweet> tweets;

    public Portfolio() {
        tweets = new ArrayList<>();
        this.generateTestData();
    }

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

    private void generateTestData() {

        for (int i = 0; i < 5; i += 1) {
            Tweet t = new Tweet();
            t.tweetText = "Dummy Tweet";
            tweets.add(t);
        }
    }
}


