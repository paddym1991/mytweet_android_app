package app.mytweet.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import org.pm.mytweet.R;

import app.mytweet.app.MyTweetApp;
import app.mytweet.models.User;

import static app.helpers.IntentHelper.navigateUp;
import static app.helpers.LogHelper.info;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private SharedPreferences prefs;
    MyTweetApp app = MyTweetApp.getApp();

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //enable the up button
        setHasOptionsMenu(true);
    }

    /**
     * Initialize the share preference.
     * Register the listener.
     */
    @Override
    public void onStart()
    {
        super.onStart();
        //initializing the share preference
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //registering the listener
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Settings onSharedPreferenceChanged Interface method
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        User user = app.userStore.getUser(app.loggedInUser.id);

        if (key.equals("email")) {
            user.email = sharedPreferences.getString(key, "");
        } else if (key.equals("password")) {
            user.password = sharedPreferences.getString(key, "");
        } else if (key.equals("firstName")) {
            user.firstName = sharedPreferences.getString(key, "");
        } else if (key.equals("lastName")) {
            user.lastName = sharedPreferences.getString(key, "");
        }
        app.userStore.saveUsers();

        //log to console when change made to settings
        info(getActivity(), "Setting change - key : value = " + key + " : " + sharedPreferences.getString(key, ""));
    }

    /**
     * Unregister the listener
     */
    @Override
    public void onStop()
    {
        super.onStop();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Menu handler for the 'up' button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                navigateUp(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
