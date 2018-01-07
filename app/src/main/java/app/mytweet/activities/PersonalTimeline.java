package app.mytweet.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pm.mytweet.R;
import app.mytweet.app.MyTweetApp;
import app.mytweet.models.Tweet;
import app.mytweet.settings.SettingsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PersonalTimeline extends AppCompatActivity implements Callback<List<Tweet>> {

    private ListView listView;
    private MyTweetApp app;
    private MyTimelineAdapter adapter;
    private String selectedItem;
    private final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        app = (MyTweetApp) getApplication();

        listView = (ListView) findViewById(R.id.timeline);
        adapter = new MyTimelineAdapter(this, app.tweets);
        listView.setAdapter(adapter);

        Call<List<Tweet>> call = (Call<List<Tweet>>) app.mytweetService.personalTweets(app.loggedInUser.id);
        call.enqueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_timeline_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_tweet:
                startActivity(new Intent(this, Tweet.class));
                break;
            case R.id.timeline:
                startActivity(new Intent(this, TimelineActivity.class));
                break;
            case R.id.userFollowsTimeline:
                startActivity(new Intent(this, UserFollowsTimeline.class));
                break;
            case R.id.user:
                startActivity(new Intent(this, Users.class));
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menuLogout:
                startActivity(new Intent(this, Welcome.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
        adapter.tweets = response.body();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<List<Tweet>> call, Throwable t) {
        Toast toast = Toast.makeText(this, "Error retrieving tweets", Toast.LENGTH_LONG);
        toast.show();
    }
}

class MyTimelineAdapter extends ArrayAdapter<Tweet> {
    private Context context;
    public List<Tweet> tweets = new ArrayList<Tweet>();

    public MyTimelineAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.users_row_layout, tweets);
        this.context = context;
        this.tweets = tweets;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.users_row_layout, parent, false);
        Tweet tweet = tweets.get(position);

        TextView tweetText = (TextView) view.findViewById(R.id.timeline_item_tweetText);
        TextView tweetDate = (TextView) view.findViewById(R.id.timeline_item_tweetDate);

        tweetText.setText(tweet.tweetText);
        //tweetDate.setText(tweet.date);

        return view;
    }

    @Override
    public int getCount() {
        return tweets.size();
    }
}