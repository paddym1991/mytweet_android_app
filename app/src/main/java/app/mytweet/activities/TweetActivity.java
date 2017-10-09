package app.mytweet.activities;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import org.pm.mytweet.R;

import app.mytweet.models.Tweet;

public class TweetActivity extends AppCompatActivity implements TextWatcher {

    private Tweet tweet;
    private EditText tweetText;
    private TextView tweetDate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetText = (EditText) findViewById(R.id.tweetText);
        tweet = new Tweet();
        tweetDate = (TextView) findViewById(R.id.tweetDate);

        // Register a TextWatcher in the EditText tweetText object
        tweetText.addTextChangedListener(this);

//        Long tdate = System.currentTimeMillis();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        String dateString = sdf.format(tdate);
//        tweetDate.setText(dateString);


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
}
