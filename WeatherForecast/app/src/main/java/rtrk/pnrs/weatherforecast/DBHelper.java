package rtrk.pnrs.weatherforecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "data.db";
    private static final String TABLE_NAME = "data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ITEM = "item";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //"CREATE TABLE data(id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT);";
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ITEM + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add a new row to the database
    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM, item);

        if(db.insert(TABLE_NAME, null, contentValues) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    //Delete data from the database
    public boolean removeData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_NAME, COLUMN_ITEM + "=?", new String[] {item}); SQL injection proof
        //db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM + "=\"" + item + "\";");
        if(db.delete(TABLE_NAME, COLUMN_ITEM + "=" + item, null) > 0) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public Cursor getListContnets() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
