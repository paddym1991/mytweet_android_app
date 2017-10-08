package app.mytweet.models;

import java.util.Date;
import java.util.Random;

/**
 * Created by Paddym1991 on 05/10/2017.
 */

public class Tweet {

    public Long id;
    public Long date;


    public Tweet() {
        id = unsignedLong();
        date = new Date().getTime();
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

    public String dateString() {
        String dateFormat = "EEE d MMM yyy H:mm";
        return android.text.format.DateFormat.format(dateFormat, date).toString();
    }

    public String getDateString() {
        return "Posted: " + dateString();
    }

}
