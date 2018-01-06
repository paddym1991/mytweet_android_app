package app.mytweet.models;

import app.mytweet.models.User;

public class Token
{
    public boolean success;
    public String token;
    public User user;

    public Token(boolean success, String token)
    {
        this.success = success;
        this.token = token;
    }
}