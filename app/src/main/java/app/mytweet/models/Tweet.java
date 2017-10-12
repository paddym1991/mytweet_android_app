package app.mytweet.models;

import java.util.Date;
import java.util.Random;

//import JSON for saving and loading
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paddym1991 on 05/10/2017.
 */

public class Tweet {

    public Long id;
    public Long date;
    public String tweetText;

    //define in our classes appropriate names for each of the fields we wish to serialize
    private static final String JSON_ID         = "id"          ;
    private static final String JSON_TWEETTEXT  = "tweetText"   ;
    private static final String JSON_DATE       = "date"        ;


    public Tweet() {
        id = unsignedLong();
        date = new Date().getTime();
        //tweetText = "";
    }

    /**
     * Generate a long greater than zero
     * @return Unsigned Long value greater than zero
     */
    private Long unsignedLong() {
        long rndVal = 0;
        do {
            rndVal = new Random().nextLong();
        } while (rndVal <= 0);
        return rndVal;
    }

    /**
     * New constructor to load a Tweet object from JSON
     * @param json
     * @throws JSONException
     */
    public Tweet(JSONObject json) throws JSONException {
        id          = json.getLong(JSON_ID);
        tweetText   = json.getString(JSON_TWEETTEXT);
        date        = json.getLong(JSON_DATE);
    }

    /**
     * Method to save an object to JSON
     * @return
     * @throws JSONException
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID            , Long.toString(id));
        json.put(JSON_TWEETTEXT     , tweetText);
        json.put(JSON_DATE          , date);
        return json;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getTweetText() {
        return tweetText;
    }

    public String dateString() {
        String dateFormat = "EEE d MMM yyy H:mm";
        return android.text.format.DateFormat.format(dateFormat, date).toString();
    }

    public String getDateString() {
        return "Posted: " + dateString();
    }

}
