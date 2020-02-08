package com.example.app.ourapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.app.ourapplication.Keys;
import com.example.app.ourapplication.rest.model.response.Kid;
import com.example.app.ourapplication.rest.model.response.Person;
import com.example.app.ourapplication.rest.model.response.Subscriber;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ROYSH on 6/8/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String MESSAGE_ID_COLUMN = "POSTID";
    private static final String MESSAGE_COLUMN = "MESSAGE";
    private static final String MESSAGE_USER_NAME_COLUMN = "USER_NAME";
    private static final String MESSAGE_USER_ID_COLUMN = "USER_ID";
    private static final String MESSAGE_IMAGE_COLUMN = "MESSAGE_IMAGE";
    private static final String MESSAGE_TIME_COLUMN = "MESSAGETIME";
    private static final String MESSAGE_LIKES_COLUMN = "LIKES";
    private static final String PROFILE_IMAGE_COLUMN = "PROFILEIMAGE";
    private static final String PROFILE_USER_COLUMN = "PROFILEUSER";
    private static final String PROFILE_ID_COLUMN = "PROFILEID";
    private static final String MESSAGE_PROTOCOL_COLUMN = "PROTOCOL";
    private static final String SUBSCRIPTION_FLAG_COLUMN = "SUBSCRIPTION_FLAG";
    private static final String SUBSCRIBER_ID_COLUMN = "SUBSCRIBER_ID";
    private static final String USER_ID_COLUMN = "USER_ID";

    private SQLiteDatabase mydatabase;

    public DBHelper(Context context) {
        super(context, "FEED", null, 35); //34 is the database version
    }

    @Override
    public void onCreate(SQLiteDatabase mydatabase) {
        // TODO Auto-generated method stub
        mydatabase.execSQL(
                "create table MESSAGE_DATA ("+MESSAGE_ID_COLUMN+" VARCHAR,"+
                        MESSAGE_USER_ID_COLUMN +" VARCHAR,"+
                        MESSAGE_USER_NAME_COLUMN+" VARCHAR,"+
                        MESSAGE_COLUMN+" VARCHAR,"+
                        PROFILE_IMAGE_COLUMN +" VARCHAR,"+
                        MESSAGE_IMAGE_COLUMN+" VARCHAR,"+
                        MESSAGE_TIME_COLUMN+" VARCHAR,"+
                        SUBSCRIPTION_FLAG_COLUMN+" VARCHAR)"

        );

        mydatabase.execSQL(
                "create table COMMENT_DATA ("+MESSAGE_ID_COLUMN+" VARCHAR,"+
                        MESSAGE_USER_ID_COLUMN+" VARCHAR,"+
                        MESSAGE_USER_NAME_COLUMN+" VARCHAR,"+
                        MESSAGE_COLUMN+" VARCHAR,"+
                        PROFILE_IMAGE_COLUMN +" VARCHAR,"+
                        MESSAGE_IMAGE_COLUMN+" VARCHAR,"+
                        MESSAGE_TIME_COLUMN+" VARCHAR,"+
                        SUBSCRIPTION_FLAG_COLUMN+" VARCHAR)"

        );

        mydatabase.execSQL(
                "create table PROFILE_DATA (" + PROFILE_ID_COLUMN + " VARCHAR PRIMARY KEY," +
                        PROFILE_USER_COLUMN + " VARCHAR ," +
                        PROFILE_IMAGE_COLUMN + " VARCHAR )"
                //"CREATE TABLE IF NOT EXISTS DATA(FROM VARCHAR,TO VARCHAR,MESSAGE VARCHAR );"
        );

        mydatabase.execSQL(
                "create table SUBSCRIBER_DATA (" + USER_ID_COLUMN + " VARCHAR," +
                        SUBSCRIBER_ID_COLUMN + " VARCHAR," +
                        "PRIMARY KEY(" + USER_ID_COLUMN + "," + SUBSCRIBER_ID_COLUMN + "))"
                //"CREATE TABLE IF NOT EXISTS DATA(FROM VARCHAR,TO VARCHAR,MESSAGE VARCHAR );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase mydatabase, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        mydatabase.execSQL("DROP TABLE IF EXISTS MESSAGE_DATA");
        mydatabase.execSQL("DROP TABLE IF EXISTS PROFILE_DATA");
        mydatabase.execSQL("DROP TABLE IF EXISTS COMMENT_DATA");
        mydatabase.execSQL("DROP TABLE IF EXISTS SUBSCRIBER_DATA");
        onCreate(mydatabase);
    }

    public boolean insertSubscriberData (Subscriber subscriber) {

        mydatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID_COLUMN, subscriber.getUserId());
        contentValues.put(SUBSCRIBER_ID_COLUMN, subscriber.getSubscriberId());
        mydatabase.insert("SUBSCRIBER_DATA", null, contentValues);
        return true;
    }


    public ArrayList<Person> getFeedDataAll() {
        ArrayList<Person> array_list = new ArrayList<Person>();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor msg_res =  db.rawQuery( "select * from MESSAGE_DATA where " +MESSAGE_FROM_COLUMN+ " = \"" + id + "\" or " +MESSAGE_TO_COLUMN_NAME+ " = \""+id+"\" ORDER BY "+ MESSAGE_TIME_COLUMN_NAME+" DESC", null );
        //Cursor msg_res =  db.rawQuery("select * from MESSAGE_DATA where " + MESSAGE_PROTOCOL_COLUMN + " = \"HTTP\" ORDER BY " + MESSAGE_TIME_COLUMN + " DESC", null);
        Cursor msg_res =  db.rawQuery("select * from MESSAGE_DATA  ORDER BY " + MESSAGE_TIME_COLUMN + " DESC", null);
        msg_res.moveToFirst();

        while(msg_res.isAfterLast() == false){
            String column0 = msg_res.getString(0);
            String column1 = msg_res.getString(1);
            String column2 = msg_res.getString(2);
            String column3 = msg_res.getString(3);
            String column4 = msg_res.getString(4);
            String column5 = msg_res.getString(5);
            String column6 = msg_res.getString(6);
            String column7 = msg_res.getString(7);

            array_list.add(new Person("F",column0,"",column1 ,"" ,column2 , column3, column4,column5,column6,column7 ));
            msg_res.moveToNext();
        }
        return array_list;
    }

    public Person getFeedData(String id) {
        Person  item;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor msg_res =  db.rawQuery( "select * from MESSAGE_DATA where " +MESSAGE_FROM_COLUMN+ " = \"" + id + "\" or " +MESSAGE_TO_COLUMN_NAME+ " = \""+id+"\" ORDER BY "+ MESSAGE_TIME_COLUMN_NAME+" DESC", null );
       // Cursor msg_res =  db.rawQuery( "select * from MESSAGE_DATA where "+MESSAGE_ID_COLUMN+ " = \"" + id +"\" and " + MESSAGE_PROTOCOL_COLUMN + " = \"HTTP\" ORDER BY "+ MESSAGE_TIME_COLUMN+" DESC", null );

        Cursor msg_res =  db.rawQuery("select * from MESSAGE_DATA  where "+MESSAGE_ID_COLUMN+ " = \"" + id + "\" ORDER BY " + MESSAGE_TIME_COLUMN + " DESC", null);
        msg_res.moveToFirst();

        String column0 = msg_res.getString(0);
        String column1 = msg_res.getString(1);
        String column2 = msg_res.getString(2);
        String column3 = msg_res.getString(3);
        String column4 = msg_res.getString(4);
        String column5 = msg_res.getString(5);
        String column6 = msg_res.getString(6);
        String column7 = msg_res.getString(7);

        item = new Person("F",column0, "" ,column1 ,"" ,column2 , column3, column4,column5,column6,column7);


        return item;
    }

    public ArrayList<Person> getFeedDataUser(String id) {
        ArrayList<Person> array_list = new ArrayList<Person>();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor msg_res =  db.rawQuery( "select * from MESSAGE_DATA where " +MESSAGE_FROM_COLUMN+ " = \"" + id + "\" or " +MESSAGE_TO_COLUMN_NAME+ " = \""+id+"\" ORDER BY "+ MESSAGE_TIME_COLUMN_NAME+" DESC", null );
        //Cursor msg_res =  db.rawQuery("select * from MESSAGE_DATA where " + MESSAGE_PROTOCOL_COLUMN + " = \"HTTP\" ORDER BY " + MESSAGE_TIME_COLUMN + " DESC", null);
        Cursor msg_res =  db.rawQuery("select * from MESSAGE_DATA  where " + MESSAGE_USER_ID_COLUMN + " = \"" + id + "\" ORDER BY " + MESSAGE_TIME_COLUMN + " DESC", null);
        msg_res.moveToFirst();

        while(msg_res.isAfterLast() == false){
            String column0 = msg_res.getString(0);
            String column1 = msg_res.getString(1);
            String column2 = msg_res.getString(2);
            String column3 = msg_res.getString(3);
            String column4 = msg_res.getString(4);
            String column5 = msg_res.getString(5);
            String column6 = msg_res.getString(6);
            String column7 = msg_res.getString(7);

            array_list.add(new Person("F",column0,"", column1,"" , column2 , column3, column4,column5,column6,column7 ));
            msg_res.moveToNext();
        }
        return array_list;
    }



    public String getUserSubscription(String id,String sid ) {
        String  columndata;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor msg_res =  db.rawQuery("select SUBSCRIBER_ID from SUBSCRIBER_DATA where " + USER_ID_COLUMN + " = \"" + id + "\"" + " and " + SUBSCRIBER_ID_COLUMN + " = \"" + sid + "\"", null);

        msg_res.moveToFirst();

        if (msg_res.getCount() != 0){
            msg_res.moveToFirst();
            columndata = msg_res.getString(0);
            Log.d(TAG, "getFeedDataColumn: " + columndata);}
        else{
            columndata="nosubscription";
        }

        return columndata;
    }


    public String getFeedDataColumn(String id, Integer columnnumber ) {
        String  columndata;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor msg_res =  db.rawQuery("select * from MESSAGE_DATA where " + MESSAGE_ID_COLUMN + " = \"" + id + "\"", null);
        Log.d(TAG, "get column data for id: " + id);
        msg_res.moveToFirst();

        if (msg_res.getCount() != 0){
            msg_res.moveToFirst();
            columndata = msg_res.getString(columnnumber);
            Log.d(TAG, "getFeedDataColumn: " + columndata);}
        else{
            columndata="noimage";
        }

        return columndata;
    }


    public String getProfileInfo(String id,Integer columnnumber) {
        String columndata;
        SQLiteDatabase db = this.getReadableDatabase();

        //String Query = "SELECT * FROM PROFILE_DATA WHERE PROFILE_USER = 'Fiji' ";
        Cursor prof_res = db.rawQuery("SELECT * FROM PROFILE_DATA WHERE " + PROFILE_ID_COLUMN + " = \"" + id + "\"", null);
        Log.d(TAG, "getProfileInfoCount: " + prof_res.getCount());
        Log.d(TAG, "getProfileInfoID: " + id);

        if (prof_res.getCount() != 0){
            prof_res.moveToFirst();
            columndata = prof_res.getString(columnnumber);
            //    Log.d(TAG, "getProfileInfo: " + columndata);
        }
        else{
            columndata="noimage";
        }

        return columndata;
    }
    public ArrayList<Person> getCommentData(String id) {
        ArrayList<Person> array_list = new ArrayList<Person>();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor msg_res =  db.rawQuery( "select * from MESSAGE_DATA where " +MESSAGE_FROM_COLUMN+ " = \"" + id + "\" or " +MESSAGE_TO_COLUMN_NAME+ " = \""+id+"\" ORDER BY "+ MESSAGE_TIME_COLUMN_NAME+" DESC", null );
        //Cursor msg_res =  db.rawQuery("select * from MESSAGE_DATA where " + MESSAGE_PROTOCOL_COLUMN + " = \"HTTP\" ORDER BY " + MESSAGE_TIME_COLUMN + " DESC", null);

        Cursor msg_res =  db.rawQuery("select * from COMMENT_DATA  WHERE " + MESSAGE_ID_COLUMN + " = \"" + id + "\" ORDER BY " + MESSAGE_TIME_COLUMN + " ASC", null);
        msg_res.moveToFirst();

        while(msg_res.isAfterLast() == false){
            String column0 = msg_res.getString(0);
            String column1 = msg_res.getString(1);
            String column2 = msg_res.getString(2);
            String column3 = msg_res.getString(3);
            String column4 = msg_res.getString(4);
            String column5 = msg_res.getString(5);
            String column6 = msg_res.getString(6);
            String column7 = msg_res.getString(7);

            array_list.add(new Person("C",column0,"", column1,"" , column2 , column3, column4,column5,column6,column7 ));
            msg_res.moveToNext();
        }
        return array_list;
    }


    public boolean updateCommentRead ( String id) {
        JSONObject msgObject = null;
        try {
            msgObject = new JSONObject("COMMENT_DATA");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mydatabase = this.getWritableDatabase();
        String strFilter =  MESSAGE_ID_COLUMN+ "=" + id;
        ContentValues args = new ContentValues();
        args.put(SUBSCRIPTION_FLAG_COLUMN, "READ");
        mydatabase.update("COMMENT_DATA", args, strFilter, null);

        //args.put(msgObject.optString("columnname"), msgObject.optString("columndata"));
        //mydatabase.update(tablename, contentValues, PROFILE_ID_COLUMN + "= \"" + msgObject.optString(Keys.KEY_USERID) + "\" ", null);
        return true;
    }

   /* public boolean updateProfile (String profile) {
        JSONObject msgObject = null;
        try {
            msgObject = new JSONObject(profile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mydatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(PROFILE_USER_COLUMN_NAME, msgObject.optString(Keys.KEY_NAME));
        contentValues.put(msgObject.optString("columnname"), msgObject.optString("columndata"));
        mydatabase.update("PROFILE_DATA", contentValues, PROFILE_ID_COLUMN + "= \"" + msgObject.optString(Keys.KEY_USERID) + "\" ", null);

        return true;
    }*/







    public boolean insertFeedData (Person  message ) {
       /* JSONObject msgObject = null;
        try {
            msgObject = new JSONObject(message);
            Log.d(TAG, "Inserted");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        mydatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_ID_COLUMN, message.getPostId());
        contentValues.put(MESSAGE_USER_ID_COLUMN, message.getUserId());
        contentValues.put(MESSAGE_USER_NAME_COLUMN, message.getSenderName());
        contentValues.put(MESSAGE_COLUMN, message.getMessage());
        contentValues.put(PROFILE_IMAGE_COLUMN, message.getPhotoId());
        contentValues.put(MESSAGE_IMAGE_COLUMN, message.getPhotoMsg());
        contentValues.put(MESSAGE_TIME_COLUMN, message.getTimeMsg());
        contentValues.put(SUBSCRIPTION_FLAG_COLUMN, message.getFlag());

        mydatabase.insert("MESSAGE_DATA", null, contentValues);
        return true;
    }

    public void deleteFeedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MESSAGE_DATA", null, null);
    }

    public boolean insertCommentData (Person message) {

        mydatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_ID_COLUMN, message.getPostId());
        contentValues.put(MESSAGE_USER_ID_COLUMN, message.getUserId());
        contentValues.put(MESSAGE_USER_NAME_COLUMN, message.getSenderName());
        contentValues.put(MESSAGE_COLUMN, message.getMessage());
        contentValues.put(PROFILE_IMAGE_COLUMN, message.getPhotoId());
        contentValues.put(MESSAGE_IMAGE_COLUMN, message.getPhotoMsg());
        contentValues.put(MESSAGE_TIME_COLUMN, message.getTimeMsg());
        contentValues.put(SUBSCRIPTION_FLAG_COLUMN, message.getFlag());
        mydatabase.insert("COMMENT_DATA", null, contentValues);
        Log.d(TAG, "insertCommentData: " + message.getMessage());
        return true;

    }

    public String getFeedDataLatestTime() {
        String  columndata;
        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor msg_res =  db.rawQuery( "select * from MESSAGE_DATA where " + MESSAGE_PROTOCOL_COLUMN + " = \"HTTP\"  ORDER BY "+ MESSAGE_TIME_COLUMN+" DESC" , null );
        Cursor msg_res =  db.rawQuery( "select * from MESSAGE_DATA   ORDER BY "+ MESSAGE_TIME_COLUMN+" DESC" , null );

        msg_res.moveToFirst();

        if (msg_res.getCount() != 0){
            //msg_res.moveToFirst();
            columndata = msg_res.getString(6);
        }
        else{
            columndata="2000-12-31 12:00:00";
        }
        Log.d(TAG, "getFeedDataLatestTime: " + columndata);
        return columndata;
    }

    public String getCommentDataLatestTime(String id) {
        String  columndata;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor msg_res =  db.rawQuery( "select * from COMMENT_DATA where " +MESSAGE_ID_COLUMN+ " = \"" + id +"\" AND "+ SUBSCRIPTION_FLAG_COLUMN + " ORDER BY "+ MESSAGE_TIME_COLUMN+" DESC" , null );

        msg_res.moveToFirst();

        if (msg_res.getCount() != 0) {
            //msg_res.moveToFirst();
            columndata = msg_res.getString(5);
        }
        else{
            columndata="2000-12-31 12:00:00";
        }
        Log.d(TAG, "getCommentDataColumn: " + columndata);
        return columndata;
    }

    public String checkCommentRead(String id) {
        String  columndata;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor msg_res =  db.rawQuery( "select * from COMMENT_DATA where " +MESSAGE_ID_COLUMN+ " = \"" + id +"\"  ORDER BY "+ MESSAGE_TIME_COLUMN+" ASC" , null );

        msg_res.moveToFirst();

        if (msg_res.getCount() != 0) {
            //msg_res.moveToFirst();
            columndata = msg_res.getString(5);
        }
        else{
            columndata="2000-12-31 12:00:00";
        }
        Log.d(TAG, "getCommentDataColumn: " + columndata);
        return columndata;
    }



}