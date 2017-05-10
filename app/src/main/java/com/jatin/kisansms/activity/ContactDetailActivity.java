package com.jatin.kisansms.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jatin.kisansms.R;
import com.jatin.kisansms.db.SmsDb;
import com.jatin.kisansms.model.SMSDetail;
import com.jatin.kisansms.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jatinjha on 10/05/17.
 */

public class ContactDetailActivity extends AppCompatActivity {

    String name,mobileno,otp;

    @BindView(R.id.txt_message)
    TextView txt_message;

    @BindView(R.id.txt_mobile_no)
    TextView txt_mobile_no;

    @BindView(R.id.txt_name)
    TextView txt_name;

    @BindView(R.id.btn_send)
    Button btn_send;

    @BindView(R.id.btn_cancel)
    Button btn_cancel;

    ProgressDialog progressDialog;

    private OkHttpClient mClient = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contact_detail_view);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(ContactDetailActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        if (getIntent().hasExtra("name")){
            name = getIntent().getStringExtra("name");
        }

        if (getIntent().hasExtra("otp")){
            otp = getIntent().getStringExtra("otp");
        }

        if (getIntent().hasExtra("mobile_no")){
            mobileno = getIntent().getStringExtra("mobile_no");
        }

        txt_message.setText("Hi! Your OTP will be "+otp);

        txt_mobile_no.setText("Mobile No : " + mobileno);

        txt_name.setText(name);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                try {
                    post(getResources().getString(R.string.backend_url),mobileno,otp, new  Callback(){

                        @Override
                        public void onFailure(Call call, IOException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"SMS Sent Unsuccessful !",Toast.LENGTH_LONG).show();
                            Log.e("smsError",e.getMessage(),e);
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    if (response.code() == 200){
                                        SMSDetail detail = new SMSDetail();
                                        detail.setOtp(otp);
                                        detail.setSentFrom(name);
                                        detail.setTime(getCurrentTime());
                                        SmsDb db = SmsDb.getInstance(getApplicationContext());
                                        db.insertSMSDetail(detail);
                                        Toast.makeText(getApplicationContext(),"SMS Sent!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"SMS Sent Unsuccessful. Please try again!",Toast.LENGTH_LONG).show();
                                        Log.d("smsError",""+response.code());
                                    }

                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    Log.e("smsError",e.getMessage(),e);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        return sdf.format(cal.getTime());
    }

    Call post(String url, String mobileno, String otp, Callback callback) throws IOException{
        RequestBody formBody = new FormBody.Builder()
                .add("To", mobileno)
                .add("Body", "Your OTP is " + otp)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call response = mClient.newCall(request);
        response.enqueue(callback);
        return response;

    }

}
