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

public class Signup extends AppCompatActivity {

    MyTweetApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        app = MyTweetApp.getApp();
    }


    /**
     * https://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
     *
     * Create a new user object and store it in the MyTweetApp object.
     * @param view
     * Retrieve the fields from the widgets.
     */
    public void registerButtonPressed (View view)
    {
        String firstName = ((TextView)  findViewById(R.id.firstName)).getText().toString();
        String lastName  = ((TextView)  findViewById(R.id.lastName)).getText().toString();
        String email     = ((TextView)  findViewById(R.id.signupEmail)).getText().toString();
        String password  = ((TextView)  findViewById(R.id.signupPassword)).getText().toString();

        MyTweetApp app = MyTweetApp.getApp();

        if (firstName.isEmpty()) {
            createToastMessage("Empty first name field").show();
        }

        else if (lastName.isEmpty()) {
            createToastMessage("Empty last name field").show();
        }

        // ensure email field is not empty and a valid email address is entered
        else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            createToastMessage("Enter a valid email address").show();
        }

        else if (password.isEmpty()) {
            createToastMessage("Empty password field").show();
        }

        else {
            User user = new User(firstName, lastName, email, password);
            app.userStore.addUser(user);
            //app.newUser(user);

            startActivity(new Intent(this, Welcome.class));
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // created a helper method for Toast response
    private Toast createToastMessage(String string) {
        return Toast.makeText(app.getApplicationContext(), string, Toast.LENGTH_SHORT);
    }


}
