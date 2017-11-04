package app.mytweet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.pm.mytweet.R;

import app.mytweet.app.MyTweetApp;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Check that the entered details match a user
     * @param view
     */
    public void signinButtonPressed (View view) {
        MyTweetApp app = MyTweetApp.getApp();

        TextView email = (TextView) findViewById(R.id.loginEmail);
        TextView password = (TextView) findViewById(R.id.loginPassword);

        String loggedInEmail = email.getText().toString();
        String loggedInPassword = password.getText().toString();

        if (app.validUser(loggedInEmail, loggedInPassword)) {

            //set logged in user when sign in is clicked
            app.setLoggedInUser(loggedInEmail);
            startActivity(new Intent(this, TimelineActivity.class));
        } else {

            Toast toast = Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
