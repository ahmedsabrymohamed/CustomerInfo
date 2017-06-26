package com.fromscratch.android.customerinfo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by moon on 6/24/2017.
 */
@IgnoreExtraProperties
public class Customer {


        public String phone;
        public String name;
        public String address;
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


}
