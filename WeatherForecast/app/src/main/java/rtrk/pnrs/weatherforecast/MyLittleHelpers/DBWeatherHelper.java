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
    private static final String COLUMN_WEEKDAY = "weekday";
    private static final String COLUMN_TEMPERATURE = "temperature";
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
        final String SQL_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CITY + " TEXT NOT NULL," +
                COLUMN_DATE + " TEXT NOT NULL," +
                COLUMN_WEEKDAY + " TEXT NOT NULL," +
                COLUMN_TEMPERATURE + " DOUBLE NOT NULL," +
                COLUMN_PRESSURE + " DOUBLE NOT NULL," +
                COLUMN_HUMIDITY + " DOUBLE NOT NULL," +
                COLUMN_SUNRISE + " TEXT NOT NULL," +
                COLUMN_SUNSET + " TEXT NOT NULL," +
                COLUMN_WIND_SPEED + " DOUBLE NOT NULL," +
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
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CITY, forecast.getCity());
        contentValues.put(COLUMN_DATE, forecast.getDate());
        contentValues.put(COLUMN_WEEKDAY, forecast.getWeekDay());
        contentValues.put(COLUMN_TEMPERATURE, forecast.getTemperature());
        contentValues.put(COLUMN_PRESSURE, forecast.getPressure());
        contentValues.put(COLUMN_HUMIDITY, forecast.getHumidity());
        contentValues.put(COLUMN_SUNRISE, forecast.getSunrise());
        contentValues.put(COLUMN_SUNSET, forecast.getSunset());
        contentValues.put(COLUMN_WIND_SPEED, forecast.getWindSpeed());
        contentValues.put(COLUMN_WIND_DIR, forecast.getWindDirection());

        if (dbWrite.insert(TABLE_NAME, null, contentValues) == -1) {
            dbWrite.close();
            return false;
        } else {
            dbWrite.close();
            return true;
        }
    }

    //Delete data from the database
    public boolean remove(String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete(TABLE_NAME, COLUMN_CITY + " =? ", new String[]{city}) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    // ok
    public Forecast getItem(String city, String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if (date != null)
            cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=? AND " + COLUMN_DATE + " =? ", new String[]{city, date}, null, null, null, null);
        else
            cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=? ", new String[]{city}, null, null, null, null);

        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToLast();
        Forecast forecast = createForecastItem(cursor);

        cursor.close();
        db.close();

        return forecast;
    }

    public Forecast getItemByWeekDay(String city, String weekday, int x) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        if (x == 0)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city + "\" AND " + COLUMN_WEEKDAY + " = \"" + weekday + "\" ;", null, null);
        if (x == 1)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city + "\" AND " + COLUMN_WEEKDAY + " = \"" + weekday + "\" AND " + COLUMN_TEMPERATURE + " > 10 ;", null, null);
        if (x == 2)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city + "\" AND " + COLUMN_WEEKDAY + " = \"" + weekday + "\" AND " + COLUMN_TEMPERATURE + " <= 10 ;", null, null);

        assert cursor != null;
        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToLast();
        Forecast forecast = createForecastItem(cursor);

        cursor.close();
        db.close();

        return forecast;
    }

    public Forecast[] getItems(String gotham, int batman) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (batman == 0)
            cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?", new String[]{gotham}, null, null, COLUMN_TEMPERATURE, null);
        else if (batman == 1)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + gotham + "\" and " + COLUMN_TEMPERATURE + " = " +
                    "(SELECT MIN(" + COLUMN_TEMPERATURE + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + gotham + "\");", null, null);
        else if (batman == 2)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + gotham + "\" and " + COLUMN_TEMPERATURE + " = " +
                    "(SELECT MAX(" + COLUMN_TEMPERATURE + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + gotham + "\");", null, null);

        assert cursor != null;
        if (cursor.getCount() <= 0)
            return null;

        Forecast[] forecasts = new Forecast[cursor.getCount()];

        int i = 0;
        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            forecasts[i++] = createForecastItem(cursor);
        }

        db.close();

        return forecasts;
    }

    public String[] getCities() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CITY + " FROM " + TABLE_NAME + " GROUP BY " + COLUMN_CITY + " ;", null, null);

        if (cursor.getCount() <= 0)
            return null;

        String[] cities = new String[cursor.getCount()];

        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            cities[i++] = cursor.getString(0);

        db.close();
        cursor.close();

        return cities;
    }

    private Forecast createForecastItem(Cursor cursor) {
        String city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
        String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        String weekday = cursor.getString(cursor.getColumnIndex(COLUMN_WEEKDAY));
        double temperature = cursor.getDouble(cursor.getColumnIndex(COLUMN_TEMPERATURE));
        double humidity = cursor.getDouble(cursor.getColumnIndex(COLUMN_HUMIDITY));
        double pressure = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRESSURE));
        String sunrise = cursor.getString(cursor.getColumnIndex(COLUMN_SUNRISE));
        String sunset = cursor.getString(cursor.getColumnIndex(COLUMN_SUNSET));
        double wind_speed = cursor.getDouble(cursor.getColumnIndex(COLUMN_WIND_SPEED));
        String wind_direction = cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR));

        return new Forecast(city, date, weekday, temperature, humidity, pressure, sunrise, sunset, wind_speed, wind_direction);
    }
}
