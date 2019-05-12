package rtrk.pnrs.weatherforecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import rtrk.pnrs.weatherforecast.MyLittleHelpers.DBWeatherHelper;
import rtrk.pnrs.weatherforecast.MyLittleHelpers.Forecast;

public class StatisticsActivity extends AppCompatActivity {

    private static String KEY = "city";

    private ImageButton sun;
    private ImageButton snowflake;

    private DBWeatherHelper dbWeatherHelper;
    private TextView city;
    private TableLayout table;
    private TextView minTempDay, minTempValue, maxTempDay, maxTempValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbWeatherHelper = new DBWeatherHelper(this);

        minTempDay = findViewById(R.id.textViewStatsTableMinTempDay);
        maxTempDay = findViewById(R.id.textViewStatsTableMaxTempDay);
        minTempValue = findViewById(R.id.textViewStatsTableMinTempValue);
        maxTempValue = findViewById(R.id.textViewStatsTableMaxTempValue);

        city = findViewById(R.id.textViewStatsCity);
        city.setText(getCity());

        table = findViewById(R.id.tableLayoutStats);

        sun = findViewById(R.id.imageButtonStatsSun);
        snowflake = findViewById(R.id.imageButtonStatsSnowflake);

        setMinTemp();
        setMaxTemp();

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        snowflake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String getCity() {

        String city = "Novi Sad";

        try {
            if (getIntent().hasExtra(KEY))
                city = getIntent().getStringExtra(KEY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return city;
    }

    private void setMinTemp() {
        Forecast[] forecasts = dbWeatherHelper.getItems(getCity(), 1);

        minTempDay.setText(forecasts[0].getWeekDay());
        minTempValue.setText(String.valueOf(forecasts[0].getTemperature()));

    }

    private void setMaxTemp() {
        Forecast[] forecasts = dbWeatherHelper.getItems(getCity(), 2);

        maxTempDay.setText(forecasts[0].getWeekDay());
        maxTempValue.setText(String.valueOf(forecasts[0].getTemperature()));

    }
}
