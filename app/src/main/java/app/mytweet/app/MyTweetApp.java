package app.mytweet.app;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static app.helpers.LogHelper.info;

import app.mytweet.models.Tweet;
import app.mytweet.models.User;
import app.mytweet.models.Portfolio;
import app.mytweet.models.PortfolioSerializer;
import app.mytweet.models.UserSerializer;
import app.mytweet.models.UserStore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public UserStore userStore;
    //introduce a field to hold the file name we will use to store the users
    private static final String USER_FILENAME = "users.json";
    public User loggedInUser;

    public List<Tweet> tweets = new ArrayList<>();

    public MyTweetService mytweetService;
    public boolean         mytweetServiceAvailable = false;
    // public String          service_url  = "http://10.0.2.2:9000";   // Standard Emulator IP Address
    public String          service_url  = "http://10.8.89.59:4000";   // Standard Emulator IP Address
    //public String          service_url  = "http://10.0.2.2:4000";   // Standard Emulator IP Address : use this one when app is secured

    @Override
    public void onCreate() {
        super.onCreate();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(service_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mytweetService = retrofit.create(MyTweetService.class);

        Log.v("Donation", "Donation App Started");

//        //portfolio creation
//        PortfolioSerializer serializer = new PortfolioSerializer(this, FILENAME);
//        portfolio = new Portfolio(serializer);
//        //user store creation
//        UserSerializer userSerializer = new UserSerializer(this, USER_FILENAME);
//        userStore = new UserStore(userSerializer);
//
//        info(this, "MyTweet app launched");
//        //initialize protected MyRentApp field in onCreate
//        app = this;


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

    public void newTweet(Tweet tweet) {
        tweets.add(tweet);
    }

    /**
     * Method to validate users
     * @param email
     * @param password
     * @return
     */
    public boolean validUser(String email, String password) {
//        User user = userStore.getUserByEmail(email);
//        if (user != null) {
        for(User user : users) {
            if (user.email.equals(email) && user.password.equals(password)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public void setLoggedInUser(String email) {
        loggedInUser = userStore.getUserByEmail(email);
    }


}
