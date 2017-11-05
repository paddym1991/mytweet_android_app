package app.mytweet.fragments;

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
import app.mytweet.activities.TweetPagerActivity;
import app.mytweet.activities.Welcome;
import app.mytweet.app.MyTweetApp;
import app.mytweet.fragments.TweetFragment;
import app.mytweet.models.Portfolio;
import app.mytweet.models.Tweet;
import app.mytweet.settings.SettingsActivity;

import android.widget.AbsListView;
import android.view.ActionMode;

/**
 * Created by Paddym1991 on 18/10/2017.
 */

public class TimelineFragment extends ListFragment implements OnItemClickListener, AbsListView.MultiChoiceModeListener {

    private ArrayList<Tweet> tweets;
    private Portfolio portfolio;
    //In order to incorporate it ResidenceAdapter into ResidenceListActivity, first introduce a new field
    private TweetAdapter adapter;
    //listView field for multi choice mode
    private ListView listView;

    MyTweetApp app;


    @Override
    public void onCreate(Bundle savedInstancesState) {

        super.onCreate(savedInstancesState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.timeline_label);

        app = MyTweetApp.getApp();
        portfolio = app.portfolio;
        tweets = portfolio.tweets;

        adapter = new TweetAdapter(getActivity(), tweets);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, parent, savedInstanceState);

        listView = (ListView) v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Tweet tweet = ((TweetAdapter) getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), TweetPagerActivity.class);
        i.putExtra(TweetFragment.EXTRA_TWEET_ID, tweet.id);
        startActivityForResult(i, 0);
    }

    //interface method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Tweet tweet = adapter.getItem(position);
        IntentHelper.startActivityWithData(getActivity(), TweetPagerActivity.class, "TWEET_ID", tweet.id);
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
                Tweet tweet = new Tweet(app.loggedInUser.id);
                portfolio.addTweet(tweet);

                Intent i = new Intent(getActivity(), TweetPagerActivity.class);
                i.putExtra(TweetFragment.EXTRA_TWEET_ID, tweet.id);
                startActivityForResult(i, 0);
                return true;

            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;

            case R.id.action_clearAll:

                for (int j  = tweets.size()  - 1; j >= 0; j--) {
                    portfolio.deleteTweet(tweets.get(j));
                    adapter.notifyDataSetChanged();
                }
                return true;

            case R.id.menuLogout:   Intent in = new Intent(getActivity(), Welcome.class);
                //Prevents user from pressing back after logging out.
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(in, 0);
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

    /* ************ MultiChoiceModeListener methods (begin) *********** */

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.timeline_context, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.menu_item_delete_tweet:
                deleteTweet(actionMode);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    private void deleteTweet(ActionMode actionMode)
    {
        for (int i = adapter.getCount() - 1; i >= 0; i--)
        {
            if (listView.isItemChecked(i))
            {
                portfolio.deleteTweet(adapter.getItem(i));
            }
        }
        actionMode.finish();
        adapter.notifyDataSetChanged();
    }

    /* ************ MultiChoiceModeListener methods (end) *********** */


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
            //assert tweet != null;
            tweetText.setText(tweet.getTweetText());

            TextView tweetDate = (TextView) convertView.findViewById(R.id.timeline_item_tweetDate);
            tweetDate.setText(tweet.getDateString());

            return convertView;
        }
    }
}


