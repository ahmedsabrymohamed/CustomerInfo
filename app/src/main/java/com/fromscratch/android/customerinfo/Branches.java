package com.fromscratch.android.customerinfo;

/**
 * Created by moon on 6/24/2017.
 */

public class Branches {

    public String password;
    public String address;
    public Branches() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Branches(String address,String password) {
        this.password = password;
        this.address = address;

    }

}
