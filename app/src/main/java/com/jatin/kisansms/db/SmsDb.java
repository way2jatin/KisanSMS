package com.jatin.kisansms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jatin.kisansms.model.SMSDetail;

/**
 * Created by jatinjha on 09/05/17.
 */

public class SmsDb {

    private Context con;
    private final String db_name = "kisan.db";
    private final int db_version = 1;
    private DatabaseHelper dbhelp;
    private SQLiteDatabase db;

    private static final String TBL_SMS_HISTORY = "sms_history";

    String db_create = "create table "+TBL_SMS_HISTORY+" (otp TEXT , sent_from TEXT, time TEXT)";


    public SmsDb(Context con) {
        this.con = con;
        dbhelp = new SmsDb.DatabaseHelper(con,db_name,null,db_version);
        db = dbhelp.getWritableDatabase();
    }

    private static SmsDb smsDb;

    public static synchronized SmsDb getInstance(Context context){
        if (smsDb==null){
            smsDb = new SmsDb(context.getApplicationContext());
        }
        return smsDb;
    }

    public class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            createDb(sqLiteDatabase);
        }

        private void createDb(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(db_create);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            dropDb(sqLiteDatabase);
            createDb(sqLiteDatabase);
        }

        private void dropDb(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_SMS_HISTORY);
        }
    }

    public void insertSMSDetail(SMSDetail detail){
        ContentValues contentValues = new ContentValues();
        contentValues.put("otp",detail.getOtp());
        contentValues.put("sent_from",detail.getSentFrom());
        contentValues.put("time",detail.getTime());
        db.insert(TBL_SMS_HISTORY,null,contentValues);
    }

    public Cursor retrieveSMSDetail(){
        Cursor cursor = db.rawQuery("Select * from "+TBL_SMS_HISTORY,null);
        return cursor;
    }

    public void deleteSMSDetail(){
        db.execSQL("Delete From "+TBL_SMS_HISTORY);
    }

}
