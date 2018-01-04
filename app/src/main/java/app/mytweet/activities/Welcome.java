package app.mytweet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.util.Log;

import org.pm.mytweet.R;

import app.mytweet.activities.Login;
import app.mytweet.activities.Signup;
import app.mytweet.app.MyTweetApp;
import app.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.Toast;

import java.util.List;

public class Welcome extends AppCompatActivity implements Callback<List<User>> {

    private MyTweetApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        app = (MyTweetApp) getApplication();
    }

    /**
     * issuing a request to the API for the list of all donors
     */
    @Override
    public void onResume()
    {
        super.onResume();
        app.loggedInUser = null;
        Call<List<User>> call1 = (Call<List<User>>) app.mytweetService.getAllUsers();
        call1.enqueue(this);
    }

    /**
     * Successful response to API request
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<List<User>> call, Response<List<User>> response)
    {
        serviceAvailableMessage();
        app.users = response.body();
        //Log.v("donation", app.users.toString());
        app.mytweetServiceAvailable = true;
    }

    /**
     * Error response to API request
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<List<User>> call, Throwable t)
    {
        app.mytweetServiceAvailable = false;
        serviceUnavailableMessage();
    }

    public void loginButtonPressed (View view)
    {
        if (app.mytweetServiceAvailable)
        {
            startActivity (new Intent(this, Login.class));
        }
        else
        {
            serviceUnavailableMessage();
        }
    }

    public void signupButtonPressed (View view)
    {
        if (app.mytweetServiceAvailable)
        {
            startActivity (new Intent(this, Signup.class));
        }
        else
        {
            serviceUnavailableMessage();
        }
    }

    void serviceUnavailableMessage()
    {
        Toast toast = Toast.makeText(this, "Donation Service Unavailable. Try again later", Toast.LENGTH_LONG);
        toast.show();
    }

    void serviceAvailableMessage()
    {
        Toast toast = Toast.makeText(this, "Donation Contacted Successfully", Toast.LENGTH_LONG);
        toast.show();
    }

}
