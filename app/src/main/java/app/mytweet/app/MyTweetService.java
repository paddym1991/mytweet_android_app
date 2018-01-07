
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
        import retrofit2.http.PUT;
        import retrofit2.http.Path;

/**
 * This class will serve as a local interface for interacting with the remote service.
 */
public interface MyTweetService
{
    @GET("/api/users")
    Call<List<User>> getAllUsers();

    @GET("/api/users/{id}")
    Call<User> getUser(@Path("id") Long id);

    @POST("/api/users")
    Call<User> createUser(@Body User user);

    @POST("/api/users/authenticate")
    Call<User> authenticate(@Body User user);

    @PUT("/api/users")
    Call<User> updateUser(@Body User user);

    @GET("/api/tweets")
    Call<List<Tweet>> getAllTweets();

    @GET("/api/tweets/{id}")
    Call<Tweet> deleteTweet(@Path("id") String id);

    @POST("/api/tweets")
    Call<Tweet> createTweet(@Body Tweet tweet);

    @POST("/api/users/{id}/follow")
    Call<User> follow(@Path("id") Long id, @Body String user);

    @POST("/api/users/{id}/unfollow")
    Call<User> unfollow(@Path("id") Long id, @Body String user);

    @GET("/api/users/{id}/userfollowsTimeline")
    Call<List<Tweet>> getUserFollowsTimeline(@Path("id") Long id);

    @GET("/api/users/{id}/tweets")
    Call<List<Tweet>> personalTweets(@Path("id") Long id);
}