package app.mytweet.activities;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.pm.mytweet.R;

import app.mytweet.app.MyTweetApp;
import app.mytweet.models.Portfolio;
import app.mytweet.models.Tweet;

//importing the helper method for 'up' style navigation
import static app.helpers.IntentHelper.navigateUp;

public class TweetActivity extends AppCompatActivity implements TextWatcher {

    private Tweet tweet;
    private EditText tweetText;
    private TextView tweetDate;
    private Portfolio portfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        //for navigation purposes
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tweetText = (EditText) findViewById(R.id.tweetText);
        tweet = new Tweet();
        tweetDate = (TextView) findViewById(R.id.tweetDate);

        // Register a TextWatcher in the EditText tweetText object
        tweetText.addTextChangedListener(this);

//        Long tdate = System.currentTimeMillis();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        String dateString = sdf.format(tdate);
//        tweetDate.setText(dateString);

        //initialising the portfolio field
        MyTweetApp app = (MyTweetApp) getApplication();
        portfolio = app.portfolio;

        //Recover the ID passed to us via the intent in TimelineActivity
        Long tweetId = (Long) getIntent().getExtras().getSerializable("TWEET_ID");
        //get the Tweet object from the portfolio
        tweet = portfolio.getTweet(tweetId);

        //call update controls if we are sure we found a reference
        if(tweet != null) {
            updateControls(tweet);
        }
    }

    /**
     * Send the tweet data to the widgets.
     * @param tweet
     */
    public void updateControls(Tweet tweet) {
        tweetText.setText(tweet.tweetText);
        tweetDate.setText(tweet.getDateString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    //set tweetText of Tweet object to text entered.
    @Override
    public void afterTextChanged(Editable editable) {

        tweet.setTweetText(editable.toString());
    }

    /**
     * Trigger a save when the user leaves the TweetActivity
     */
    @Override
    public void onPause() {

        super.onPause();
        portfolio.saveTweets();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:  navigateUp(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
