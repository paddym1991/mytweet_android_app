package app.mytweet.activities;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.pm.mytweet.R;

import app.mytweet.app.MyTweetApp;
import app.mytweet.models.Portfolio;
import app.mytweet.models.Tweet;

//importing the helper method for 'up' style navigation
import static app.helpers.ContactHelper.getContact;
import static app.helpers.ContactHelper.getEmail;
import static app.helpers.IntentHelper.navigateUp;
import static app.helpers.IntentHelper.selectContact;
import android.content.Intent;

public class TweetActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private Tweet tweet;
    private EditText tweetText;
    private TextView tweetDate;
    private Portfolio portfolio;
    //An ID we ill use for the implicit intent
    private static final int REQUEST_CONTACT = 1;
    //button to trigger the intent
    private Button selectContactButton;
    private String emailAddress = "";

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

        //initialisation of selectContactButton
        selectContactButton = (Button)   findViewById(R.id.selectContact);      //'selectContact' is ID of the button in activity_tweet
        //event handler set up for selectContactButton
        selectContactButton.setOnClickListener((View.OnClickListener) this);
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
     * Event handler
     * overides super class by this class implementing 'View.OnClickListener'
     * @param view
     */
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.selectContact : selectContact(this, REQUEST_CONTACT);
                break;
        }
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

    /**
     * To deal with selectContent method triggering a 'startActivityForResult'
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CONTACT:
                String name = getContact(this, data);
                emailAddress = getEmail(this, data);
                selectContactButton.setText(name + " : " + emailAddress);
                tweet.contact = name;
                break;
        }
    }
}
