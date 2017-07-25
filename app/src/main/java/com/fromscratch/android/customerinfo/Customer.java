package com.fromscratch.android.customerinfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by moon on 6/24/2017.
 */
@IgnoreExtraProperties
public class Customer implements Parcelable{


        public String name;
        public String address;
        public String phone;
        public String branch_address;
        public String key;
        public Customer() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Customer(String name, String address, String phone, String branch_address) {
            this.name = name;
            this.address = address;
            this.phone=phone;
            this.branch_address=branch_address;
        }

    public Customer(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.phone=in.readString();
        this.branch_address=in.readString();
        this.key=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(branch_address);
        dest.writeString(key);

    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>()
    {
        public Customer createFromParcel(Parcel in)
        {
            return new Customer(in);
        }
        public Customer[] newArray(int size)
        {
            return new Customer[size];
        }
    };
}
