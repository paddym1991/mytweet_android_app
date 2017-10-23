package app.mytweet.app;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import static app.helpers.LogHelper.info;

import app.mytweet.models.User;
import app.mytweet.models.Portfolio;
import app.mytweet.models.PortfolioSerializer;

/**
 * Created by Paddym1991 on 09/10/2017.
 */

public class MyTweetApp extends Application {

    public Portfolio portfolio;
    //introduce a field to hold the file name we will use to store the portfolio
    private static final String FILENAME = "portfolio.json";
    //declare a protected MyTweetApp field
    protected static MyTweetApp app;
    //incorporate a new collection of Users
    public List<User> users = new ArrayList<User>();

    @Override
    public void onCreate() {
        super.onCreate();
        //portfolio creation
        PortfolioSerializer serializer = new PortfolioSerializer(this, FILENAME);
        portfolio = new Portfolio(serializer);

        info(this, "MyTweet app launched");
        //initialize protected MyRentApp field in onCreate
        app = this;


    }

    public static MyTweetApp getApp() {

        return app;
    }

    /**
     * Method to add a user to the collection
     * @param user
     */
    public void newUser(User user) {

        users.add(user);
    }

    /**
     * Method to validate users
     * @param email
     * @param password
     * @return
     */
    public boolean validUser (String email, String password)
    {
        for (User user : users)
        {
            if (user.email.equals(email) && user.password.equals(password))
            {
                return true;
            }
        }
        return false;
    }

}
