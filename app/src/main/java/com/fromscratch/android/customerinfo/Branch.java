package com.fromscratch.android.customerinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by moon on 6/24/2017.
 */

public class Branch implements Parcelable{

    public String address;
    public String password;
    public String key;
    public Branch() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Branch(String address, String password) {
        this.address = address;
        this.password = password;

    }
    public Branch(Parcel in) {
        this.address = in.readString();
        this.password = in.readString();
        this.key = in.readString();


    }

    public static final Parcelable.Creator<Branch> CREATOR = new Parcelable.Creator<Branch>()
    {
        public Branch createFromParcel(Parcel in)
        {
            return new Branch(in);
        }
        public Branch[] newArray(int size)
        {
            return new Branch[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(password);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
