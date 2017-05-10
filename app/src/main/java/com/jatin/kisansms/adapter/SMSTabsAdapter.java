package com.jatin.kisansms.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jatin.kisansms.fragment.ContactsFragment;
import com.jatin.kisansms.fragment.SMSHistoryFragment;
import com.jatin.kisansms.model.Contact;
import com.jatin.kisansms.model.SMSDetail;

import java.util.ArrayList;

/**
 * Created by uw on 9/5/17.
 */

public class SMSTabsAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;
    ArrayList<Contact> contacts = new ArrayList<>();
    ArrayList<SMSDetail> smsDetails = new ArrayList<>();

    public SMSTabsAdapter(FragmentManager supportFragmentManager, ArrayList<Contact> contacts,ArrayList<SMSDetail> smsDetails) {
        super(supportFragmentManager);
        this.contacts = contacts;
        this.smsDetails = smsDetails;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ContactsFragment.newInstance(contacts);
            case 1:
                return SMSHistoryFragment.newInstance(smsDetails);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Contacts";
        }
        else {
            return "History";
        }
    }
}
