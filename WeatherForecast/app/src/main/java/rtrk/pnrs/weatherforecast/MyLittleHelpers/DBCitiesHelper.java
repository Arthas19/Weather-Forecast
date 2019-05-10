package rtrk.pnrs.weatherforecast.MyLittleHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBCitiesHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "data";
    private static final String COLUMN_CITY = "city";

    public DBCitiesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //"CREATE TABLE data(id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT);";
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CITY + " TEXT NOT NULL" + ");";
        db.execSQL(SQL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add a new row to the database
    public boolean insert(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CITY, city);

        if (db.insert(TABLE_NAME, null, contentValues) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    //Delete data from the database
    public boolean remove(String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete(TABLE_NAME, COLUMN_CITY + "=?", new String[]{city}) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public MyCityItem[] getItems() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        MyCityItem[] myItems = new MyCityItem[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            myItems[i++] = createMyItem(cursor);
        }

        db.close();

        return myItems;
    }

    public MyCityItem getItem(String city) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?", new String[]{city}, null, null, null, null);

        cursor.moveToFirst();
        MyCityItem item = createMyItem(cursor);

        db.close();

        return item;
    }

    private MyCityItem createMyItem(Cursor cursor) {
        String string = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));

        return new MyCityItem(string);
    }
}
