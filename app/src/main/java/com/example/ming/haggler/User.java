package com.example.ming.haggler;

/**
 * Created by Ming on 3/06/2017.
 */

public class User {
    private String username;
    private String password;
    //https://stackoverflow.com/questions/22209046/fix-android-studio-login-activity-template-generated-activity
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
