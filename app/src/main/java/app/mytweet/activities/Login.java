package app.mytweet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.pm.mytweet.R;

import app.mytweet.app.MyTweetApp;

import app.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Check that the entered details match a user
     *
     * @param view
     */
    public void signinButtonPressed(View view) {
        final MyTweetApp app = MyTweetApp.getApp();

        TextView email = (TextView) findViewById(R.id.loginEmail);
        TextView password = (TextView) findViewById(R.id.loginPassword);

        String loggedInEmail = email.getText().toString();
        String loggedInPassword = password.getText().toString();

//        if (app.validUser(loggedInEmail, loggedInPassword)) {
//
//            //set logged in user when sign in is clicked
//            app.setLoggedInUser(loggedInEmail);
//            startActivity(new Intent(this, TimelineActivity.class));
//        } else {
//
//            Toast toast = Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT);
//            toast.show();
//        }

//        User user = new User(null, null, email.getText().toString(), password.getText().toString());
//
//        Call<User> call = (Call<User>) app.mytweetService.authenticate(user);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                User user = response.body();
//                // Currently logs in a User but issue with
//                // java.lang.NullPointerException: Attempt to read from field 'java.lang.String coady.mytweetapp.model.User.email' on a null object reference
//                // when user enters correct email but wrong password
//                if (app.validUser(user.email, user.password)) {
//                    startActivity (new Intent(Login.this, TweetPagerActivity.class));
//                } else {
//                    Toast toast = Toast.makeText(Login.this, "Invalid Credentials 1", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast toast = Toast.makeText(Login.this, "Invalid Credentials 2", Toast.LENGTH_SHORT);
//                toast.show();
//            }


        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (userEmail.equals("") || userPassword.equals("")) {
            Toast toast = Toast.makeText(this, "Email or Password is empty", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            User user = new User(null, null, email.getText().toString(), password.getText().toString());

            Call<User> call = (Call<User>) app.mytweetService.authenticate(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    // Currently logs in a User but issue with
                    // java.lang.NullPointerException: Attempt to read from field 'java.lang.String coady.mytweetapp.model.User.email' on a null object reference
                    // when user enters correct email but wrong password
                    if (app.validUser(user.email, user.password)) {
                        startActivity(new Intent(Login.this, TimelineActivity.class));
                    } else {
                        Toast toast = Toast.makeText(Login.this, "Invalid Credentials 1", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast toast = Toast.makeText(Login.this, "Invalid Credentials 2", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
}
