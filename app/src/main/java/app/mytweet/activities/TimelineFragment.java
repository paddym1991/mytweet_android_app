package app.mytweet.activities;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import org.pm.mytweet.R;

import java.util.ArrayList;

import app.helpers.IntentHelper;
import app.mytweet.app.MyTweetApp;
import app.mytweet.models.Portfolio;
import app.mytweet.models.Tweet;

/**
 * Created by Paddym1991 on 18/10/2017.
 */

public class TimelineFragment extends ListFragment implements OnItemClickListener {

    private ArrayList<Tweet> tweets;
    private Portfolio portfolio;
    //In order to incorporate it ResidenceAdapter into ResidenceListActivity, first introduce a new field
    private TweetAdapter adapter;

    MyTweetApp app;


    @Override
    public void onCreate(Bundle savedInstancesState) {

        super.onCreate(savedInstancesState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.app_name);

        app = MyTweetApp.getApp();
        portfolio = app.portfolio;
        tweets = portfolio.tweets;

        adapter = new TweetAdapter(getActivity(), tweets);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, parent, savedInstanceState);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Tweet tweet = ((TweetAdapter) getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), TweetActivity.class);
        i.putExtra(TweetFragment.EXTRA_TWEET_ID, tweet.id);
        startActivityForResult(i, 0);
    }

    //interface method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Tweet tweet = adapter.getItem(position);
        IntentHelper.startActivityWithData(getActivity(), TweetActivity.class, "TWEET_ID", tweet.id);
    }

    /**
     * Bind the newly created menu to the timeline activity as we wish it to appear here
     *
     * @param menu
     * @return
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.timeline, menu);
    }

    /**
     * Respond to selecting the menu item to create a new tweet instance
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_item_new_tweet:
                Tweet tweet = new Tweet();
                portfolio.addTweet(tweet);

                Intent i = new Intent(getActivity(), TweetActivity.class);
                i.putExtra(TweetFragment.EXTRA_TWEET_ID, tweet.id);
                startActivityForResult(i, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Ensure changes made in TimelineActivity are reflected in the list
    @Override
    public void onResume() {
        super.onResume();
        ((TweetAdapter) getListAdapter()).notifyDataSetChanged();
    }


//In order to update the list with the residence objects contained in the portfolio, we need an Adapter.
//        An Adapter is a special class we can append to the end of the existing ResidenceListActivity class (make sure it is outside the closing brace of ResidenceListActivity).

    class TweetAdapter extends ArrayAdapter<Tweet> {

        private Context context;

        public TweetAdapter(Context context, ArrayList<Tweet> tweets) {

            super(context, 0, tweets);
            this.context = context;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.timeline_item_tweet, null);
            }
            Tweet tweet = getItem(position);

            TextView tweetText = (TextView) convertView.findViewById(R.id.timeline_item_tweetText);
            tweetText.setText(tweet.tweetText);

            TextView tweetDate = (TextView) convertView.findViewById(R.id.timeline_item_tweetDate);
            tweetDate.setText(tweet.getDateString());

            return convertView;
        }
    }
}


