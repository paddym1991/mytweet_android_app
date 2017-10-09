package app.mytweet.models;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.pm.mytweet.R;

import java.util.ArrayList;

public class Timeline {

    public ArrayList<Tweet> tweets;

    public Timeline() {
        tweets = new ArrayList<>();
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
}


