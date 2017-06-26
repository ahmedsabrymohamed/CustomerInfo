package com.fromscratch.android.customerinfo;

/**
 * Created by moon on 6/24/2017.
 */

public class Branch {

    public String password;
    public String address;
    public String key;
    public Branch() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Branch(String address, String password) {
        this.password = password;
        this.address = address;

    }

}
