package app.mytweet.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import org.pm.mytweet.R;

import static app.helpers.IntentHelper.navigateUp;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

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
