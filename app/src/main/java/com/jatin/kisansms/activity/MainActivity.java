package com.jatin.kisansms.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jatin.kisansms.R;
import com.jatin.kisansms.adapter.SMSTabsAdapter;
import com.jatin.kisansms.db.SmsDb;
import com.jatin.kisansms.model.Contact;
import com.jatin.kisansms.model.SMSDetail;
import com.jatin.kisansms.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.sliding_tabs)
    TabLayout tabsStrip;

    boolean doubleBackToExitPressedOnce = false;

    ArrayList<Contact> contacts = new ArrayList<>();
    ArrayList<SMSDetail> smsDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        insertContactData();
        retrieveSMSHistory();

        viewPager.setAdapter(new SMSTabsAdapter(getSupportFragmentManager(),contacts,smsDetails));
        tabsStrip.setupWithViewPager(viewPager);

    }

    private void retrieveSMSHistory() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = SmsDb.getInstance(getApplicationContext()).retrieveSMSDetail();
                if (cursor.moveToFirst()){
                    do{
                        SMSDetail detail = SMSDetail.detail(cursor);
                        smsDetails.add(detail);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        }).start();
    }


    private void insertContactData() {

        String strJson = "{\n" +
                "    \"contacts\": [\n" +
                "        {\n" +
                "                \"id\": \"1\",\n" +
                "                \"firstName\": \"Jatin\",\n" +
                "\t\t\"lastName\" : \"Jha\",\n" +
                "                \"mobile\": \"+91 9536462481\"\n" +
                "        },{\n" +
                "                \"id\": \"2\",\n" +
                "                \"firstName\": \"Abhishek\",\n" +
                "\t\t\"lastName\" : \"Sharma\",\n" +
                "                \"mobile\": \"+91 7053265863\"\n" +
                "        },{\n" +
                "                \"id\": \"3\",\n" +
                "                \"firstName\": \"Kanav\",\n" +
                "\t\t\"lastName\" : \"Singla\",\n" +
                "                \"mobile\": \"+91 8570808777\"\n" +
                "        },{\n" +
                "                \"id\": \"4\",\n" +
                "                \"firstName\": \"Abhijeet\",\n" +
                "\t\t\"lastName\" : \"Singh\",\n" +
                "                \"mobile\": \"+91 9536462481\"\n" +
                "        },{\n" +
                "                \"id\": \"5\",\n" +
                "                \"firstName\": \"Nikhil\",\n" +
                "\t\t\"lastName\" : \"Yadav\",\n" +
                "                \"mobile\": \"+91 9536462481\"\n" +
                "        },{\n" +
                "                \"id\": \"5\",\n" +
                "                \"firstName\": \"Gregory\",\n" +
                "\t\t\"lastName\" : \"George\",\n" +
                "                \"mobile\": \"+91 9536462481\"\n" +
                "        },{\n" +
                "                \"id\": \"6\",\n" +
                "                \"firstName\": \"Kisan\",\n" +
                "\t\t\"lastName\" : \"Network\",\n" +
                "                \"mobile\": \"+91 9971792703\"\n" +
                "        },{\n" +
                "                \"id\": \"7\",\n" +
                "                \"firstName\": \"Amit\",\n" +
                "\t\t\"lastName\" : \"Modi\",\n" +
                "                \"mobile\": \"+91 9536462481\"\n" +
                "        },{\n" +
                "                \"id\": \"8\",\n" +
                "                \"firstName\": \"Saurabh\",\n" +
                "\t\t\"lastName\" : \"Sharma\",\n" +
                "                \"mobile\": \"+91 9536462481\"\n" +
                "        },{\n" +
                "                \"id\": \"9\",\n" +
                "                \"firstName\": \"Ankur\",\n" +
                "\t\t\"lastName\" : \"Jain\",\n" +
                "                \"mobile\": \"+91 9536462481\"\n" +
                "        },{\n" +
                "                \"id\": \"10\",\n" +
                "                \"firstName\": \"Neha\",\n" +
                "\t\t\"lastName\" : \"Yadav\",\n" +
                "                \"mobile\": \"+91 9536462481\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        try {
            JSONObject object = new JSONObject(strJson);
            String contactJSON = object.getString("contacts");
            ObjectMapper mapper = new ObjectMapper();
            contacts = mapper.readValue(contactJSON,new TypeReference<List<Contact>>(){});
        }catch (Exception e){
            Log.e("SMSError",e.getMessage(),e);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
