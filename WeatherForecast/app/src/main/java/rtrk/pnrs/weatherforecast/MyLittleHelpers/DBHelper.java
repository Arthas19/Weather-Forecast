package rtrk.pnrs.weatherforecast.MyLittleHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import rtrk.pnrs.weatherforecast.Data.DataEntry;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //"CREATE TABLE data(id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT);";
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_TABLE = "CREATE TABLE " +
                DataEntry.TABLE_NAME + " (" +
                DataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                DataEntry.COLUMN_COORDINATES + " TEXT NOT NULL);";
        db.execSQL(SQL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME);
        onCreate(db);
    }

    //Add a new row to the database
    public boolean insert(String city, String coordinates) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataEntry.COLUMN_CITY, city);
        contentValues.put(DataEntry.COLUMN_COORDINATES, coordinates);

        if (db.insert(DataEntry.TABLE_NAME, null, contentValues) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    //Delete data from the database
    public boolean remove(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_NAME, COLUMN_ITEM + "=?", new String[] {item}); SQL injection proof
        //db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM + "=\"" + item + "\";");
        if (db.delete(DataEntry.TABLE_NAME, DataEntry.COLUMN_CITY + "=" + item, null) > 0) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public Cursor getListContnets() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + DataEntry.TABLE_NAME, null);
    }
}
