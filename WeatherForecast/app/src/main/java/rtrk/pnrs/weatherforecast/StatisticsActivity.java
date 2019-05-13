package rtrk.pnrs.weatherforecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import rtrk.pnrs.weatherforecast.MyLittleHelpers.DBWeatherHelper;
import rtrk.pnrs.weatherforecast.MyLittleHelpers.Forecast;
import rtrk.pnrs.weatherforecast.MyLittleHelpers.MyTableAdapter;

public class StatisticsActivity extends AppCompatActivity {

    private static String CITY;

    private DBWeatherHelper db;
    private ListView table1, table2;
    private MyTableAdapter tableAdapter1, tableAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView city = findViewById(R.id.textViewStatsCity);

        CITY = getCity();
        city.setText(CITY);

        db = new DBWeatherHelper(this);

        table1 = findViewById(R.id.listViewStatsBasic);
        table2 = findViewById(R.id.listViewStatsExtremes);

        tableAdapter1 = new MyTableAdapter();
        tableAdapter2 = new MyTableAdapter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                fillBasicData(0);
                fillExtremesData();
            }
        }).start();

        ImageButton sun = findViewById(R.id.imageButtonStatsSun);
        ImageButton snowflake = findViewById(R.id.imageButtonStatsSnowflake);

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillBasicData(1);
            }
        });

        snowflake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillBasicData(2);
            }
        });
    }

    private String getCity() {

        String city = "Novi Sad";

        try {
            String KEY = "city";
            if (getIntent().hasExtra(KEY))
                city = getIntent().getStringExtra(KEY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return city;
    }

    private void fillBasicData(int x) {
        Forecast forecast;
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        tableAdapter1.clear();

        for (int i = 0; i < 7; i++) {
            forecast = db.getItemByWeekDay(CITY, days[i], x);
            if (forecast != null)
                tableAdapter1.addItem(days[i], String.valueOf(forecast.getTemperature()), String.valueOf(forecast.getPressure()), String.valueOf(forecast.getHumidity()));
            else
                tableAdapter1.addItem(days[i], "", "", "");
        }

        table1.setAdapter(tableAdapter1);
    }

    private void fillExtremesData() {
        Forecast[] forecasts;

        tableAdapter2.clear();

        forecasts = db.getItems(CITY, 1);
        tableAdapter2.addItem("Min temp:", forecasts[0].getWeekDay(), "", String.valueOf(forecasts[0].getTemperature()));
        for (int i = 1; i < forecasts.length; i++)
            tableAdapter2.addItem("", forecasts[i].getWeekDay(), "", String.valueOf(forecasts[i].getTemperature()));

        forecasts = db.getItems(CITY, 2);
        tableAdapter2.addItem("Max temp:", forecasts[0].getWeekDay(), "", String.valueOf(forecasts[0].getTemperature()));
        for (int i = 1; i < forecasts.length; i++)
            tableAdapter2.addItem("", forecasts[i].getWeekDay(), "", String.valueOf(forecasts[i].getTemperature()));

        table2.setAdapter(tableAdapter2);
    }
}
