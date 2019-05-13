package rtrk.pnrs.weatherforecast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rtrk.pnrs.weatherforecast.MyLittleHelpers.DBWeatherHelper;
import rtrk.pnrs.weatherforecast.MyLittleHelpers.Forecast;


public class DetailsActivity extends AppCompatActivity {

    private static final String KEY = "city";
    @SuppressLint("ConstantLocale")
    public static final String currDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    LinearLayout linearLayoutTemperature, linearLayoutSns, linearLayoutWind;
    TextView date, location;
    TextView lastUpdated;
    TextView textViewTemperature, textViewPressure, textViewHumidity;
    TextView textViewSunrise, textViewSunset;
    TextView textViewWindSpeed, textViewWindDirection;
    Button buttonTemperature, buttonSns, buttonWind;
    Button buttonStats;
    ImageButton imageButtonRefresh;
    Spinner spinner;

    DBWeatherHelper dbWeatherHelper;
    private static String CITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dbWeatherHelper = new DBWeatherHelper(this);

        CITY = getCity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setLatestData();
                    }
                });
            }
        }).start();


        //dummyValues();

        date = findViewById(R.id.textViewDetailsDay);

        location = findViewById(R.id.textViewDetailsLocation);

        lastUpdated = findViewById(R.id.textViewDetailsLastUpdated);
        imageButtonRefresh = findViewById(R.id.imageButtonDetailsRefresh);

        buttonTemperature = findViewById(R.id.buttonDetailsTemperature);
        buttonSns = findViewById(R.id.buttonDetailsSns);
        buttonWind = findViewById(R.id.buttonDetailsWind);
        buttonStats = findViewById(R.id.buttonDetailsStats);

        textViewTemperature = findViewById(R.id.textViewDetailsTemperature);
        textViewPressure = findViewById(R.id.textViewDetailsPressure);
        textViewHumidity = findViewById(R.id.textViewDetailsHumidity);

        textViewSunrise = findViewById(R.id.textViewDetailsSunrise);
        textViewSunset = findViewById(R.id.textViewDetailsSunset);

        textViewWindSpeed = findViewById(R.id.textViewDetailsWindSpeed);
        textViewWindDirection = findViewById(R.id.textViewDetailsWindDirection);

        linearLayoutTemperature = findViewById(R.id.temperatureLinearLayout);
        linearLayoutSns = findViewById(R.id.snsLinearLayout);
        linearLayoutWind = findViewById(R.id.windLinearLayout);

        spinner = findViewById(R.id.spinnerDetailsTemperature);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerDetailsCF, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                if (linearLayoutTemperature.getVisibility() == View.VISIBLE) {
                    Toast.makeText(parent.getContext(), str, Toast.LENGTH_SHORT).show();

                    try {
                        double temp = Double.parseDouble(textViewTemperature.getText().toString().split(" ")[1]);

                        if (parent.getItemAtPosition(position).toString().equals("F"))
                            temp = temp * 1.8 + 32;
                        else
                            temp = (temp - 32) / 1.8;

                        textViewTemperature.setText(String.format("%s %.2f", getString(R.string.textViewDetailsTemperature), temp));

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        imageButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                }).start();
            }
        });

        buttonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, StatisticsActivity.class);
                intent.putExtra(KEY, CITY);
                startActivity(intent);
            }
        });

        buttonTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutTemperature.getVisibility() == View.INVISIBLE) {
                    linearLayoutTemperature.setVisibility(View.VISIBLE);

                    linearLayoutSns.setVisibility(View.INVISIBLE);
                    linearLayoutWind.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonSns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutSns.getVisibility() == View.INVISIBLE) {
                    linearLayoutSns.setVisibility(View.VISIBLE);

                    linearLayoutTemperature.setVisibility(View.INVISIBLE);
                    linearLayoutWind.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonWind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutWind.getVisibility() == View.INVISIBLE) {
                    linearLayoutWind.setVisibility(View.VISIBLE);

                    linearLayoutTemperature.setVisibility(View.INVISIBLE);
                    linearLayoutSns.setVisibility(View.INVISIBLE);
                }
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

    private void refreshData() {
        Forecast forecast = new Forecast(CITY);

        if (dbWeatherHelper.insert(forecast)) {
            Log.d("Insert in DB", "SUCCESSFUL");
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setLatestData();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void setLatestData() {
        Forecast forecast = dbWeatherHelper.getItem(CITY, currDate);

        location.setText(String.format("%s %s", getString(R.string.textViewDetailsLocation), CITY));
        date.setText(String.format("%s %s", getString(R.string.textViewDetailsDate), forecast.getDate()));

        textViewTemperature.setText(String.format("%s %.2f", getString(R.string.textViewDetailsTemperature), forecast.getTemperature()));
        textViewHumidity.setText(String.format("%s %.2f", getString(R.string.textViewDetailsHumidity), forecast.getHumidity()));
        textViewPressure.setText(String.format("%s %.2f", getString(R.string.textViewDetailsPressure), forecast.getPressure()));

        textViewWindSpeed.setText(String.format("%s %.2f", getString(R.string.textViewDetailsWindSpeed), forecast.getWindSpeed()));
        textViewWindDirection.setText(String.format("%s %s", getString(R.string.textViewDetailsWindDirection), forecast.getWindDirection()));

        textViewSunrise.setText(forecast.getSunrise());
        textViewSunset.setText(forecast.getSunset());

        if (currDate.equals(forecast.getDate()))
            lastUpdated.setVisibility(View.INVISIBLE);
        else
            lastUpdated.setVisibility(View.VISIBLE);
    }

    private void dummyValues() {
        dbWeatherHelper.insert(new Forecast("Novi Sad", "06-05-2019", "Mon", 19.0, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "07-05-2019", "Tue", 15.0, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "08-05-2019", "Wed", 12.0, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "09-05-2019", "Thu", -10.0, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "10-05-2019", "Fri", 31.9, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "11-05-2019", "Sat", 31.9, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "12-05-2019", "Sun", -13, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "13-05-2019", "Mon", 100, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "13-05-2019", "Mon", 100, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
        dbWeatherHelper.insert(new Forecast("Novi Sad", "13-05-2019", "Mon", 100, 0.66, 1.01, "05:00", "21:00", 16.0, "NE"));
    }
}
