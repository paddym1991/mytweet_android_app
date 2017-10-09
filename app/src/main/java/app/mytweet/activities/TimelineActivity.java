package app.mytweet.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.pm.mytweet.R;

import java.util.ArrayList;

import app.mytweet.app.MyTweetApp;
import app.mytweet.models.Portfolio;
import app.mytweet.models.Tweet;

/**
 * Created by Paddym1991 on 09/10/2017.
 */

public class TimelineActivity extends AppCompatActivity {

    private ListView listView;
    private Portfolio portfolio;
    //In order to incorporate it ResidenceAdapter into ResidenceListActivity, first introduce a new field
    private TweetAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_timeline);

        listView = (ListView) findViewById(R.id.timeline);

        MyTweetApp app = (MyTweetApp) getApplication();
        portfolio = app.portfolio;

        //in order to trigger TweetAdapter
        adapter = new TweetAdapter(this, portfolio.tweets);
        listView.setAdapter(adapter);
    }
}

//In order to update the list with the residence objects contained in the portfolio, we need an Adapter.
//        An Adapter is a special class we can append to the end of the existing ResidenceListActivity class (make sure it is outside the closing brace of ResidenceListActivity).

class TweetAdapter extends ArrayAdapter<Tweet> {

    private Context context;

    public TweetAdapter(Context context, ArrayList<Tweet> tweets) {

        super(context, 0, tweets);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.timeline_item_tweet, null);
        }
        Tweet tweet = getItem(position);

        TextView tweetText = (TextView) convertView.findViewById(R.id.timeline_item_tweetText);
        tweetText.setText(tweet.tweetText);

//        TextView dateTextView = (TextView) convertView.findViewById(R.id.timeline_item_tweetDate);
//        tweetDate.setText(tweet.getDateString());

        return convertView;
    }
}


