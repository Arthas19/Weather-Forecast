package rtrk.pnrs.weatherforecast;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import static rtrk.pnrs.weatherforecast.MyLittleHelpers.Forecast.BASE_URL;
import static rtrk.pnrs.weatherforecast.MyLittleHelpers.Forecast.EXTRAS;
import static rtrk.pnrs.weatherforecast.MyLittleHelpers.Forecast.SECRET_KEY;

public class DetailsActivity extends AppCompatActivity {

    private static final String KEY = "location";

    LinearLayout linearLayoutTemperature, linearLayoutSns, linearLayoutWind;
    TextView date, location;
    TextView lastUpdated;
    TextView textViewTemperature, textViewPressure, textViewHumidity;
    TextView textViewSunrise, textViewSunset;
    TextView textViewWindSpeed, textViewWindDirection;
    Button buttonTemperature, buttonSns, buttonWind;
    ImageButton imageButtonRefresh;
    Spinner spinner;

    Forecast forecast;
    DBWeatherHelper dbWeatherHelper;

    String currDate = new SimpleDateFormat("EE", Locale.getDefault()).format(new Date());
    // String currDate = "11-05-2019";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        date = findViewById(R.id.textViewDetailsDay);
        //date.setText(String.format("%s %s", getString(R.string.textViewDetailsDate), currDate));

        location = findViewById(R.id.textViewDetailsLocation);
        //location.setText(String.format("%s %s", getString(R.string.textViewDetailsLocation), getLocation()));

        lastUpdated = findViewById(R.id.textViewDetailsLastUpdated);
        imageButtonRefresh = findViewById(R.id.imageButtonDetailsRefresh);

        buttonTemperature = findViewById(R.id.buttonDetailsTemperature);
        buttonSns = findViewById(R.id.buttonDetailsSns);
        buttonWind = findViewById(R.id.buttonDetailsWind);

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        }).start();

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

    private String getLocation() {

        String location = "Novi Sad";

        try {
            if (getIntent().hasExtra(KEY))
                location = getIntent().getStringExtra(KEY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return location;
    }

    @SuppressLint("DefaultLocale")
    private void refreshData() {

        final String CITY = getLocation();
        final String URL = BASE_URL + CITY + EXTRAS + SECRET_KEY;

        dbWeatherHelper = new DBWeatherHelper(this);

        forecast = dbWeatherHelper.getItem(CITY);

        if (forecast == null)
            forecast = new Forecast(URL);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                location.setText(String.format("%s %s", getString(R.string.textViewDetailsLocation), CITY));
                date.setText(String.format("%s %s", getString(R.string.textViewDetailsDate), forecast.getDate()));

                textViewTemperature.setText(String.format("%s %.2f", getString(R.string.textViewDetailsTemperature), forecast.getTemperature()));
                textViewHumidity.setText(String.format("%s %.2f", getString(R.string.textViewDetailsHumidity), forecast.getHumidity()));
                textViewPressure.setText(String.format("%s %.2f", getString(R.string.textViewDetailsPressure), forecast.getPressure()));

                textViewWindSpeed.setText(String.format("%s %.2f", getString(R.string.textViewDetailsWindSpeed), forecast.getWindSpeed()));
                textViewWindDirection.setText(String.format("%s %s", getString(R.string.textViewDetailsWindDirection), forecast.getWindDirection()));

                textViewSunrise.setText(forecast.getSunrise());
                textViewSunset.setText(forecast.getSunset());

                if (!currDate.equals(forecast.getDate()))
                    lastUpdated.setVisibility(View.VISIBLE);
                else
                    lastUpdated.setVisibility(View.INVISIBLE);
            }
        });
    }
}
