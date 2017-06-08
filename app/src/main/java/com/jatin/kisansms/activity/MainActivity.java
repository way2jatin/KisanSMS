package com.jatin.kisansms.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.jatin.kisansms.util.ApiCall;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = ApiCall.getJsonFromUrl("https://way2jatin.github.io/contacts.json");
                    String contactJSON = object.getString("contacts");
                    ObjectMapper mapper = new ObjectMapper();
                    contacts = mapper.readValue(contactJSON,new TypeReference<List<Contact>>(){});

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("DATA",contacts);
                    Message message = new Message();
                    message.setData(bundle);
                    refreshItemListHandler.sendMessage(message);
                }catch (Exception e){
                    Log.e("SMSError",e.getMessage(),e);
                }
            }
        }).start();

    }

    private Handler refreshItemListHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            contacts = message.getData().getParcelableArrayList("DATA");
            viewPager.setAdapter(new SMSTabsAdapter(getSupportFragmentManager(),contacts,smsDetails));
            tabsStrip.setupWithViewPager(viewPager);
            return true;
        }
    });

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
