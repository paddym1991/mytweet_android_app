
package app.mytweet.app;

        import java.util.List;

        import app.mytweet.activities.TweetPagerActivity;
        import app.mytweet.models.Tweet;
        import app.mytweet.models.User;
        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.DELETE;
        import retrofit2.http.GET;
        import retrofit2.http.POST;
        import retrofit2.http.Path;

/**
 * This class will serve as a local interface for interacting with the remote service.
 */
public interface MyTweetService
{
    @GET("/api/users")
    Call<List<User>> getAllUsers();

    @GET("/api/users/{id}")
    Call<User> getUser(@Path("id") String id);

    @POST("/api/users")
    Call<User> createUser(@Body User user);

    @GET("/api/tweets")
    Call<List<Tweet>> getAllTweets();

    @GET("/api/tweets/{id}")
    Call<Tweet> deleteTweet(@Path("id") String id);

    @POST("/api/tweets")
    Call<Tweet> createTweet(@Body Tweet tweet);
}