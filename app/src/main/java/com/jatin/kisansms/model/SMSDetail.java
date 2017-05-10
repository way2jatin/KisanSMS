package com.jatin.kisansms.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by uw on 9/5/17.
 */

public class SMSDetail implements Parcelable {

    private String otp;
    private String sentFrom;
    private String time;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.otp);
        dest.writeString(this.sentFrom);
        dest.writeString(this.time);
    }

    public SMSDetail() {
    }

    protected SMSDetail(Parcel in) {
        this.otp = in.readString();
        this.sentFrom = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<SMSDetail> CREATOR = new Parcelable.Creator<SMSDetail>() {
        @Override
        public SMSDetail createFromParcel(Parcel source) {
            return new SMSDetail(source);
        }

        @Override
        public SMSDetail[] newArray(int size) {
            return new SMSDetail[size];
        }
    };


    public static SMSDetail detail(Cursor cursor) {
        SMSDetail detail = new SMSDetail();
        detail.otp = cursor.getString(cursor.getColumnIndex("otp"));
        detail.sentFrom = cursor.getString(cursor.getColumnIndex("sent_from"));
        detail.time = cursor.getString(cursor.getColumnIndex("time"));
        return detail;
    }
}
