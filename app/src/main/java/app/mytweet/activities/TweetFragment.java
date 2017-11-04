
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
import android.support.v4.app.NavUtils;
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

import app.helpers.IntentHelper;
import app.mytweet.app.MyTweetApp;
import app.mytweet.models.Portfolio;
import app.mytweet.models.Tweet;
import app.mytweet.models.User;

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
    //button to post tweet
    private Button tweetButton;
    //button to trigger the intent
    private Button selectContactButton;
    //button to trigger email intent
    private Button   emailTweetButton;
    //text view for counting down characters of tweet
    private TextView charCounter;

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

    /**
     * 1. Bind listener to widgets
     * 2. Set specific listener to each widget
     * @param v
     */
    private void addListeners(View v)
    {
        tweetText = (EditText) v.findViewById(R.id.tweetText);
        tweetDate = (TextView) v.findViewById(R.id.tweetDate);
        emailTweetButton = (Button)   v.findViewById(R.id.emailTweet);
        selectContactButton = (Button)   v.findViewById(R.id.selectContact);
        tweetButton = (Button) v.findViewById(R.id.tweetButton);
        charCounter = (TextView) v.findViewById(R.id.charCounter);

        tweetText.addTextChangedListener(this);
        //tweetDate.addTextChangedListener(this);       //tweet Date does not change after intitially being set, so no need for a listener
        emailTweetButton.setOnClickListener(this);
        selectContactButton.setOnClickListener(this);
        tweetButton.setOnClickListener(this);
    }


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

        int remainingChars = 140 - tweetText.getText().toString().length();
        charCounter.setText(Integer.toString(remainingChars));
    }

    //set tweetText of Tweet object to text entered.
    @Override
    public void afterTextChanged(Editable editable) {

        //log to keep track of tweetText being changed
        Log.i("twitter", "tweetText " + editable.toString());
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
            case android.R.id.home:
                //if textView contains text then navigateUp to parent(timeline), saving tweet to array.
            if (tweetText.getText().length() > 0) {
                tweet.tweetText = tweetText.getText().toString();

                navigateUp(getActivity());
                portfolio.saveTweets();
                return true;

            } else {
                //else if textView is empty then delete this created tweet and navigate to timeline. No new tweet will be visible in timeline.
                portfolio.deleteTweet(tweet);

                startActivity(new Intent(getActivity(), TimelineActivity.class));
                createToastMessage("No message entered!").show();
                return true;
            }

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
            case R.id.tweetButton:
                if (tweetText.getText().length() > 0) {
                    tweet.tweetText = tweetText.getText().toString();
                    IntentHelper.startActivity(getActivity(), TimelineActivity.class);
                    portfolio.saveTweets();

                    break;
                } else {
                    createToastMessage("No message entered!").show();
                    break;
                }

            //event handler for select contact button
            case R.id.selectContact :
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
                selectContactButton.setText("Contact: "+ tweet.contact);
                break;
            //event handler for email tweet button
            case R.id.emailTweet :
                User user = app.userStore.getUser(tweet.getUserId());
                //sendEmail(getActivity(), emailAddress, getString(R.string.tweet_email_subject), tweet.getTweetEmail(getActivity()));        //tweet_email_subject found in 'strings'
                sendEmail(getActivity(), emailAddress, "New Tweet by " + user.firstName + " " + user.lastName, tweet.getTweetEmail(getActivity()));
                break;
        }
    }

    /**
     * Trigger a save when the user leaves the TweetActivity
     */
    @Override
    public void onPause() {

        super.onPause();
        //portfolio.saveTweets();           //not using portfolio.saveTweets() anymore as I've implemented it in the 'onOptionsItemSelected' & 'inClick' methods
                                            //This was the reason that a blank tweet was added and saved to the timeline when back button was pressed.

        //Long tweetId = (Long) getActivity().getIntent().getSerializableExtra(EXTRA_TWEET_ID);
    }


    /**
     * To deal with selectContent method triggering a 'startActivityForResult'
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    //refactored method. reading of contact details farmed out to separate method (readContact()) below
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

    // created a helper method for Toast response
    private Toast createToastMessage(String string) {
        return Toast.makeText(app.getApplicationContext(), string, Toast.LENGTH_SHORT);
    }
}