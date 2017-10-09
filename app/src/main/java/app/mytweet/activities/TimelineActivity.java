package app.mytweet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.pm.mytweet.R;

import app.mytweet.app.MyTweetApp;
import app.mytweet.models.Portfolio;

/**
 * Created by Paddym1991 on 09/10/2017.
 */

public class TimelineActivity extends AppCompatActivity {

    private ListView listView;
    private Portfolio portfolio;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_timeline);

        listView = (ListView) findViewById(R.id.timeline);

        MyTweetApp app = (MyTweetApp) getApplication();
        portfolio = app.portfolio;
    }
}
