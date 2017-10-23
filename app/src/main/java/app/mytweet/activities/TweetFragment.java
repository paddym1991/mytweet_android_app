
/**
 * Created by Paddym1991 on 18/10/2017.
 */
package app.mytweet.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import android.widget.Toast;

//import helper for sending an email
import static app.helpers.ContactHelper.sendEmail;

public class TweetFragment extends Fragment implements TextWatcher, OnClickListener {

    public static   final String  EXTRA_TWEET_ID = "mytweet.TWEET_ID";
    //An ID we will use for the implicit intent
    private static  final int     REQUEST_CONTACT = 1;

    private Tweet tweet;
    private Portfolio portfolio;

    private EditText tweetText;
    private TextView tweetDate;
    //button to trigger the intent
    private Button selectContactButton;
    //button to trigger email intent
    private Button   emailTweetButton;

    private String emailAddress = "";
    // New field for intent data. This field is initialized in `onActivityResult`.
    // This field is required to provide us with access to the data intent outside the method onActivityResult
    private Intent data;

    MyTweetApp app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //Recover the ID passed to us via the intent in TimelineActivity
        // Long tweetId = (Long) getActivity().getIntent().getSerializableExtra("EXTRA_TWEET_ID");
        Long tweetId = (Long) getActivity().getIntent().getSerializableExtra(EXTRA_TWEET_ID);

        app = MyTweetApp.getApp();
        portfolio = app.portfolio;
        tweet = portfolio.getTweet(tweetId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        super.onCreateView(inflater,  parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_tweet, parent, false);

        TweetActivity tweetActivity = (TweetActivity)getActivity();
        tweetActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        addListeners(v);
        updateControls(tweet);

        return v;
    }

    private void addListeners(View v)
    {
        tweetText = (EditText) v.findViewById(R.id.tweetText);
        tweetDate = (TextView) v.findViewById(R.id.tweetDate);
        emailTweetButton = (Button)   v.findViewById(R.id.emailTweet);
        selectContactButton = (Button)   v.findViewById(R.id.selectContact);

        tweetText.addTextChangedListener(this);
        tweetDate.addTextChangedListener(this);
        emailTweetButton.setOnClickListener(this);
        selectContactButton.setOnClickListener(this);
    }

//        setContentView(R.layout.fragment_tweet);
//        for navigation purposes
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

    /**
     * Send the tweet data to the widgets.
     * @param tweet
     */
    public void updateControls(Tweet tweet) {
        tweetText.setText(tweet.tweetText);
        tweetDate.setText(tweet.getDateString());
        selectContactButton.setText("Contact: " + tweet.contact);
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

        Log.i(this.getClass().getSimpleName(), "tweetText " + editable.toString());
        tweet.tweetText = editable.toString();
        //tweet.setTweetText(editable.toString());
    }

    /**
     * Bind the newly created menu to the tweet activity as we wish it to appear here
     *
     * @param menu
     * @return
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tweet, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:  navigateUp(getActivity());
                return true;

//            case R.id.action_settings: Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
//                return true;

            case R.id.menuLogout:   Intent in = new Intent(getActivity(), Welcome.class);
                //Prevents user from pressing back after logging out.
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(in, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
            //event handler for select contact button
            case R.id.selectContact :
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
                selectContactButton.setText("Contact: "+ tweet.contact);
                break;
            //event handler for email tweet button
            case R.id.emailTweet :
                sendEmail(getActivity(), emailAddress,
                        getString(R.string.tweet_email_subject), tweet.getTweetEmail(getActivity()));        //tweet_email_subject found in 'strings'
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


    /**
     * To deal with selectContent method triggering a 'startActivityForResult'
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    //refactored method. reading of contact details farmed out to seperate method (readContact()) below
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode) {
            case REQUEST_CONTACT:
                this.data = data;
                checkContactsReadPermission();
                break;
        }
    }

    /**
     * As we will be adding permission checks prior to reading the contact details,
     * we will farm the reading of the contact details into this new private method
     */
    private void readContact() {
        String name = getContact(getActivity(), data);
        emailAddress = getEmail(getActivity(), data);
        tweet.contact = name;
        selectContactButton.setText(name + ": " + emailAddress);
    }


    /**
     * //https://developer.android.com/training/permissions/requesting.html
     *
     *  This method will check if the app has permission to read the contact details.
     *  If it does have permission, the readContact() method will be called.
     *  If it doesn't, it will request the permission via a pop-up dialog box. The user can choose to Allow or Deny access.
     */
    private void checkContactsReadPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //We can request the permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
        }
        else {
            //We already have permission, so go ahead and read the contact
            readContact();
        }
    }

    //https://developer.android.com/training/permissions/requesting.html

    /**
     * When the user responds to the dialog box (allow or deny), the system invokes the app's onRequestPermissionsResult() method,
     * passing in the user response.
     * Note that, if permission is granted, the readContact method is called, otherwise it is ignored.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    readContact();
                }
            }
        }
    }
}