package com.example.aquafarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static String uid;
    public DBHelper(@Nullable Context context, String UID) {
        super(context, UID+".db", null, 1);
        uid = UID;
    }
    public DBHelper(@Nullable Context context) {
        super(context, uid+".db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists feed_list(" +
                "feed_no varchar(5)," +
                "brand varchar(10) not null," +
                "price mediumint not null," +
                "primary key(feed_no))");
        int i = 1;
        while (i <= 5) {
            db.execSQL("insert or replace into feed_list values("+i+",\"CP\",7"+i+"000)");
            i++;
        }

            db.execSQL("create table if not exists site_list (site_loc varchar primary key)");

            db.execSQL("create table if not exists pond_list(" +
                    "site_loc varchar," +
                    "pond_no char(1)," +
                    "start_date date not null," +
                    "breed varchar(20)," +
                    "primary key(site_loc,pond_no)," +
                    "foreign key(site_loc) references site_list(site_loc))");


            db.execSQL("create table if not exists feeding(" +
                    "date_time datetime," +
                    "site_loc varchar," +
                    "pond_no char(1)," +
                    "feed_wt decimal(2,2) not null," +
                    "feed_no varchar(5) not null," +
                    "primary key(date_time,site_loc,pond_no)," +
                    "foreign key(site_loc,pond_no) references pond_list(site_loc,pond_no)," +
                    "foreign key(feed_no) references feed_list(feed_no))");

            db.execSQL("create table if not exists feed_stock(" +
                    "site_loc varchar," +
                    "feed_no varchar(5)," +
                    "avl_qty decimal(5,2)," +
                    "primary key(site_loc,feed_no)," +
                    "foreign key(site_loc) references site_list(site_loc)," +
                    "foreign key(feed_no) references feed_list(feed_no))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




    public boolean insertSite(String siteLoc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("site_loc",siteLoc);
        long f = db.insert("site_list", null, contentValues);
        return (f == -1) ? false : true;
    }

    public boolean insertPond(String siteLoc, String pondNo, String date, String breed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("site_loc",siteLoc);
        contentValues.put("pond_no",pondNo);
        contentValues.put("start_date",date);
        contentValues.put("breed",breed);
        long f = db.insert("pond_list", null, contentValues);
        return (f == -1) ? false : true;
    }

    public boolean insertFeeding(String siteLoc, String pondNo, String dateTime, String feedNo, String feedWt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_time",dateTime);
        contentValues.put("site_loc",siteLoc);
        contentValues.put("pond_no",pondNo);
        contentValues.put("feed_no",feedNo);
        contentValues.put("feed_wt",feedWt);
        long f = db.insert("feeding", null, contentValues);
        db.execSQL("UPDATE FEED_STOCK SET AVL_QTY = AVL_QTY - "+feedWt+" WHERE SITE_LOC = \""+siteLoc+"\" AND FEED_NO = \""+feedNo+"\"");
        return (f == -1) ? false : true;
    }

    public void insertStock(String siteLoc,String feedNo, String feedQty) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("insert into feed_stock values(\""+siteLoc+"\", \""+feedNo+"\", "+feedQty+") on conflict(site_loc,feed_no) do update set avl_qty = avl_qty + "+feedQty);

    }


    public List<String> getSiteList() {

        List<String> siteList = new ArrayList<String>();

        String query = "SELECT * FROM SITE_LIST";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                String site = cursor.getString(0);
                siteList.add(site);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return siteList;
    }


    public List<String> getPondList(String siteLoc) {
        List<String> pondList = new ArrayList<String>();

        String query = "SELECT POND_NO FROM POND_LIST WHERE SITE_LOC = \"" + siteLoc+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                String pond = cursor.getString(0);
                pondList.add(pond);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pondList;
    }

    public List<String> getFeedList() {
        List<String> feedList = new ArrayList<String>();

        String query = "SELECT feed_no FROM feed_list";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                String feedNo = cursor.getString(0);
                feedList.add(feedNo);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return feedList;
    }

    public List<FeedStockModel> getStock(String siteLoc) {
        List<FeedStockModel> feedStockList = new ArrayList<FeedStockModel>();
        String query = "SELECT l.brand,s.feed_no,s.avl_qty FROM feed_list l, feed_stock s WHERE " +
                "s.site_loc=\""+siteLoc+"\" AND s.feed_no=l.feed_no and s.avl_qty!=0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                String brand = cursor.getString(0);
                String feedNo = cursor.getString(1);
                String avlQty = Float.toString(cursor.getFloat(2));
                FeedStockModel obj = new FeedStockModel(brand, feedNo, avlQty);
                feedStockList.add(obj);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return feedStockList;
    }

    public List<FeedStockModel> getFeedUsage(String siteLoc) {
        List<FeedStockModel> feedStockList = new ArrayList<FeedStockModel>();
        String query = "select l.feed_no,sum(f.feed_wt),l.price from feeding f, feed_list l where " +
                "site_loc=\""+siteLoc+"\" and f.feed_no=l.feed_no group by l.feed_no";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                String feedNo = cursor.getString(0);
                String feedWt = cursor.getString(1);
                String cost = Float.toString(cursor.getFloat(2) * Float.parseFloat(feedWt)/1000);
                FeedStockModel obj = new FeedStockModel(feedNo, feedWt, cost);
                feedStockList.add(obj);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return feedStockList;
    }


}


