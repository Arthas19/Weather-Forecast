package rtrk.pnrs.weatherforecast.MyLittleHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBWeatherHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "weather";

    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TEMP = "temperature";
    private static final String COLUMN_PRESSURE = "pressure";
    private static final String COLUMN_HUMIDITY = "humidity";
    private static final String COLUMN_SUNRISE = "sunrise";
    private static final String COLUMN_SUNSET = "sunset";
    private static final String COLUMN_WIND_SPEED = "wind_speed";
    private static final String COLUMN_WIND_DIR = "wind_direction";


    public DBWeatherHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_TABLE = "CREATE TABLE " +  TABLE_NAME + " (" +
                                                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    COLUMN_CITY + " TEXT NOT NULL," +
                                                    COLUMN_DATE + " TEXT NOT NULL," +
                                                    COLUMN_TEMP + " TEXT NOT NULL," +
                                                    COLUMN_PRESSURE + " TEXT NOT NULL," +
                                                    COLUMN_HUMIDITY + " TEXT NOT NULL," +
                                                    COLUMN_SUNRISE + " TEXT NOT NULL," +
                                                    COLUMN_SUNSET + " TEXT NOT NULL," +
                                                    COLUMN_WIND_SPEED + " TEXT NOT NULL," +
                                                    COLUMN_WIND_DIR + " TEXT NOT NULL" +
                                                    ");";
        db.execSQL(SQL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add a new row to the database
    public boolean insert(Forecast forecast) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        /*
        TODO : contentValues.put(COLUMN_CITY, forecast.getCity());
               contentValues.put(COLUMN_DATE, forecast.getDate());
        */
        contentValues.put(COLUMN_TEMP, forecast.getTemperature());
        contentValues.put(COLUMN_PRESSURE, forecast.getPressure());
        contentValues.put(COLUMN_HUMIDITY, forecast.getHumidity());
        contentValues.put(COLUMN_SUNRISE, forecast.getSunrise());
        contentValues.put(COLUMN_SUNSET, forecast.getSunset());
        contentValues.put(COLUMN_WIND_SPEED, forecast.getWindSpeed());
        contentValues.put(COLUMN_WIND_DIR, forecast.getWindDirection());

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

    public MyItem[] getItems(String column, String selection) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        MyItem[] myItems = new MyItem[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            myItems[i++] = createMyItem(cursor);
        }

        db.close();

        return myItems;
    }

    public MyItem getItem(String city) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?", new String[]{city}, null, null, null, null);

        cursor.moveToFirst();
        MyItem myItem = createMyItem(cursor);

        db.close();

        return myItem;
    }

    private MyItem createMyItem(Cursor cursor) {
        String string = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));

        return new MyItem(string);
    }
}
