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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import org.pm.mytweet.R;
import app.mytweet.app.MyTweetApp;
import app.mytweet.models.User;
import app.mytweet.settings.SettingsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.AdapterView;

import static app.helpers.IntentHelper.navigateUp;


public class Users extends AppCompatActivity implements Callback<List<User>>, AdapterView.OnItemClickListener {

    private ListView listView;
    private MyTweetApp app;
    private UserAdapter adapter;
    private Button follow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        app = (MyTweetApp) getApplication();

        listView = (ListView) findViewById(R.id.usersList);
        adapter = new UserAdapter(this, app.users);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //follow = (Button) findViewById(R.id.follow);
        //follow.setOnClickListener(this);

        Call<List<User>> call = (Call<List<User>>) app.mytweetService.getAllUsers();
        call.enqueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_tweet:
                startActivity(new Intent(this, TweetPagerActivity.class));
                break;
            case R.id.timeline:
                startActivity(new Intent(this, TimelineActivity.class));
                break;
            case R.id.userFollowsTimeline:
                startActivity(new Intent(this, UserFollowsTimeline.class));
                return true;
            case R.id.personalTimeline:
                startActivity(new Intent(this, PersonalTimeline.class));
                return true;
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
    public void onResume() {
        super.onResume();
        Call<List<User>> call1 = (Call<List<User>>) app.mytweetService.getAllUsers();
        call1.enqueue(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        adapter.users = response.body();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<List<User>> call, Throwable t) {
        Toast toast = Toast.makeText(this, "Error retrieving users", Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        final User user = adapter.getItem(position);
        Long userId = user.id;
        if (app.loggedInUser.following != null) {
            if (app.loggedInUser.following.contains(userId)) {
                Call<User> call1 = (Call<User>) app.mytweetService.unfollow(app.loggedInUser.id, userId.toString());
                call1.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast toast = Toast.makeText(Users.this, "Unfollowed " + user.firstName + " " + user.lastName, Toast.LENGTH_LONG);
                        toast.show();
                        app.loggedInUser = response.body();
                        startActivity(new Intent(Users.this, TimelineActivity.class));
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast toast = Toast.makeText(Users.this, "Error unfollowing " + user.firstName + " " + user.lastName, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            } else if (!app.loggedInUser.following.contains(userId)) {
                Call<User> call1 = (Call<User>) app.mytweetService.follow(app.loggedInUser.id, userId.toString());
                call1.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast toast = Toast.makeText(Users.this, "Started following " + user.firstName + " " + user.lastName, Toast.LENGTH_LONG);
                        toast.show();
                        app.loggedInUser = response.body();
                        startActivity(new Intent(Users.this, TimelineActivity.class));
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast toast = Toast.makeText(Users.this, "Error following " + user.firstName + " " + user.lastName, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }
    }
}

class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    public List<User> users = new ArrayList<User>();
    private MyTweetApp app;

    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.users_row_layout, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.users_row_layout, parent, false);
        User user = users.get(position);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        userName.setText(user.firstName + " " + user.lastName);

        TextView button = (TextView) view.findViewById(R.id.follow);
        if(app.loggedInUser.following != null) {
            if(app.loggedInUser.following.contains(user.id)) {
                button.setText("Unfollow");
                } else {
                button.setText("Follow");
                }
            }

        return view;
    }
}