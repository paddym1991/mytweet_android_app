package app.mytweet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.pm.mytweet.R;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void loginButtonPressed (View view) {
        startActivity(new Intent(this, Login.class));
    }

    public void signupButtonPressed (View view) {
        startActivity(new Intent(this, Signup.class));
    }
}
