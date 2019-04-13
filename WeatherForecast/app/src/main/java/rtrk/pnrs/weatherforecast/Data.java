package rtrk.pnrs.weatherforecast;

import android.provider.BaseColumns;

public class Data {

    private Data() {
    }

    public static final class DataEntry implements BaseColumns {

        public static final String TABLE_NAME = "data";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_COORDINATES = "coordinates";
    }
}
