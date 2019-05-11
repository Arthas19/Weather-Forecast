package rtrk.pnrs.weatherforecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        setMinTmp();

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

    private void setMinTmp() {
        Forecast forecast = dbWeatherHelper.getItem("Novi Sad");

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = df.parse(forecast.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String d = sdf.format(date);

        minTempDay.setText(d);
        minTempValue.setText(String.valueOf(forecast.getTemperature()));
    }
}
