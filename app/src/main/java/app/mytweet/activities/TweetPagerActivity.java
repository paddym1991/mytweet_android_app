package app.mytweet.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.pm.mytweet.R;

import java.util.ArrayList;

import app.mytweet.app.MyTweetApp;
import app.mytweet.fragments.TweetFragment;
import app.mytweet.models.Portfolio;
import app.mytweet.models.Tweet;

import static app.helpers.LogHelper.info;

public class TweetPagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
{
    private ViewPager viewPager;

    //Introduce instance variables for the list of tweets and for a portfolio
    private ArrayList<Tweet> tweets;
    private Portfolio portfolio;

    //Declare a PageAdapter instance variable
    private PagerAdapter pagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //This creates a view programatically (i.e. select create field to resolve the error)
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        setTimeline();

        //Instantiate the pageAdapter
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tweets);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        setCurrentItem();
    }

    //obtain a reference to the list of tweets stored in the model layer
    private void setTimeline()
    {
        MyTweetApp app = MyTweetApp.getApp();
        portfolio = app.portfolio;
        tweets = portfolio.tweets;
    }

    /*
 * Ensure selected residence is shown in details view
 */
    private void setCurrentItem() {
        Long tweetId = (Long) getIntent().getSerializableExtra(TweetFragment.EXTRA_TWEET_ID);
        for (int i = 0; i < tweets.size(); i++) {
            if (tweets.get(i).id.equals(tweetId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        info(this, "onPageScrolled: position " + position + " arg1 " + positionOffset + " positionOffsetPixels " + positionOffsetPixels);
        Tweet tweet = tweets.get(position);
        if (tweet.tweetText != null) {
            setTitle(tweet.tweetText);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class PagerAdapter extends FragmentStatePagerAdapter
    {
        private ArrayList<Tweet>  tweets;

        public PagerAdapter(FragmentManager fm, ArrayList<Tweet> tweets)
        {
            super(fm);
            this.tweets = tweets;
        }

        @Override
        public int getCount()
        {
            return tweets.size();
        }

        @Override
        public Fragment getItem(int pos)
        {
            Tweet tweet = tweets.get(pos);
            Bundle args = new Bundle();
            args.putSerializable(TweetFragment.EXTRA_TWEET_ID, tweet.id);
            TweetFragment fragment = new TweetFragment();
            fragment.setArguments(args);
            return fragment;
        }
    }
}




//package app.mytweet.activities;
//
//import org.pm.mytweet.R;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//
//public class TweetActivity extends AppCompatActivity
//{
//    ActionBar actionBar;
//
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_container);
//
//        actionBar = getSupportActionBar();
//
//        FragmentManager manager = getSupportFragmentManager();
//        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
//        if (fragment == null)
//        {
//            fragment = new TweetFragment();
//            manager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
//        }
//    }
//}



//package app.mytweet.activities;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.icu.text.SimpleDateFormat;
//import android.icu.util.Calendar;
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import org.pm.mytweet.R;
//
//import app.mytweet.app.MyTweetApp;
//import app.mytweet.models.Portfolio;
//import app.mytweet.models.Tweet;
//
////importing the helper method for 'up' style navigation
//import static app.helpers.ContactHelper.getContact;
//import static app.helpers.ContactHelper.getEmail;
//import static app.helpers.IntentHelper.navigateUp;
//import static app.helpers.IntentHelper.selectContact;
//import android.content.Intent;
//
////import helper for sending an email
//import static app.helpers.ContactHelper.sendEmail;
//
//public class TweetActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
//
//    private Tweet tweet;
//    private EditText tweetText;
//    private TextView tweetDate;
//    private Portfolio portfolio;
//    //An ID we ill use for the implicit intent
//    private static final int REQUEST_CONTACT = 1;
//    //button to trigger the intent
//    private Button selectContactButton;
//    private String emailAddress = "";
//    //button to trigger email intent
//    private Button   emailTweetButton;
//    // New field for intent data. This field is initialized in `onActivityResult`.
//    // This field is required to provide us with access to the data intent outside the method onActivityResult
//    private Intent data;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_tweet);
//        //for navigation purposes
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        tweetText = (EditText) findViewById(R.id.tweetText);
//        tweet = new Tweet();
//        tweetDate = (TextView) findViewById(R.id.tweetDate);
//
//        // Register a TextWatcher in the EditText tweetText object
//        tweetText.addTextChangedListener(this);
//
//        //initialising the portfolio field
//        MyTweetApp app = (MyTweetApp) getApplication();
//        portfolio = app.portfolio;
//
//        //Recover the ID passed to us via the intent in TimelineActivity
//        Long tweetId = (Long) getIntent().getExtras().getSerializable("TWEET_ID");
//        //get the Tweet object from the portfolio
//        tweet = portfolio.getTweet(tweetId);
//
//        //call update controls if we are sure we found a reference
//        if(tweet != null) {
//            updateControls(tweet);
//        }
//
//        //initialisation of selectContactButton
//        selectContactButton = (Button)   findViewById(R.id.selectContact);      //'selectContact' is ID of the button in fragment_tweet
//        //event handler set up for selectContactButton
//        selectContactButton.setOnClickListener(this);
//
//        //initialisation of the emailTweetButton
//        emailTweetButton = (Button)   findViewById(R.id.emailTweet);        //emailTweet is ID of the button in fragment_tweet
//        //Enabling the event handler for emailTweetButton
//        emailTweetButton.setOnClickListener(this);
//    }
//
//    /**
//     * Send the tweet data to the widgets.
//     * @param tweet
//     */
//    public void updateControls(Tweet tweet) {
//        tweetText.setText(tweet.tweetText);
//        tweetDate.setText(tweet.getDateString());
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    //set tweetText of Tweet object to text entered.
//    @Override
//    public void afterTextChanged(Editable editable) {
//
//        tweet.setTweetText(editable.toString());
//    }
//
//    /**
//     * Event handler
//     * overides super class by this class implementing 'View.OnClickListener'
//     * @param view
//     */
//    @Override
//    public void onClick(View view)
//    {
//        switch (view.getId())
//        {
//            //event handler for select contact button
//            case R.id.selectContact : selectContact(this, REQUEST_CONTACT);
//                break;
//            //event handler for email tweet button
//            case R.id.emailTweet :
//                sendEmail(this, emailAddress,
//                        getString(R.string.tweet_email_subject), tweet.getTweetEmail(this));        //tweet_email_subject found in 'strings'
//                break;
//        }
//    }
//
//    /**
//     * Trigger a save when the user leaves the TweetActivity
//     */
//    @Override
//    public void onPause() {
//
//        super.onPause();
//        portfolio.saveTweets();
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:  navigateUp(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    /**
//     * To deal with selectContent method triggering a 'startActivityForResult'
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    //refactored method. reading of contact details farmed out to seperate method (readContact()) below
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CONTACT:
//                this.data = data;
//                checkContactsReadPermission();
//                break;
//        }
//    }
//
//    /**
//     * As we will be adding permission checks prior to reading the contact details,
//     * we will farm the reading of the contact details into this new private method
//     */
//    private void readContact() {
//        String name = getContact(this, data);
//        emailAddress = getEmail(this, data);
//        selectContactButton.setText(name + " : " + emailAddress);
//        tweet.contact = name;
//    }
//
//    //https://developer.android.com/training/permissions/requesting.html
//
//    /**
//     *  This method will check if the app has permission to read the contact details.
//     *  If it does have permission, the readContact() method will be called.
//     *  If it doesn't, it will request the permission via a pop-up dialog box. The user can choose to Allow or Deny access.
//     */
//    private void checkContactsReadPermission() {
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            //We can request the permission.
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
//        }
//        else {
//            //We already have permission, so go ahead and read the contact
//            readContact();
//        }
//    }
//
//    //https://developer.android.com/training/permissions/requesting.html
//
//    /**
//     * When the user responds to the dialog box (allow or deny), the system invokes the app's onRequestPermissionsResult() method,
//     * passing in the user response.
//     * Note that, if permission is granted, the readContact method is called, otherwise it is ignored.
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CONTACT: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted
//                    readContact();
//                }
//            }
//        }
//    }
//}
