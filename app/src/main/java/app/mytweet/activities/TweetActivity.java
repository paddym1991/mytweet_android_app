package app.mytweet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.pm.mytweet.R;

import app.mytweet.models.Tweet;

public class TweetActivity extends AppCompatActivity implements TextWatcher {

    private Tweet tweet;
    private EditText tweetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tweetText = (EditText) findViewById(R.id.tweetText);
        tweet = new Tweet();

        // Register a TextWatcher in the EditText tweetText object
        tweetText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
