package app.mytweet.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import app.mytweet.fragments.SettingsFragment;

/**
 * Created by Paddym1991 on 23/10/2017.
 */

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

}