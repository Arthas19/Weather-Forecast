package rtrk.pnrs.weatherforecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private static final String KEY = "location";

    private static final String BASE_URL = "https://api.darksky.net/forecast/";
    private static final String SECRET_KEY = "1300b6a561b5e3fe758d6ba61fed2701/";
    private static final String COORDINATES = "45.25167, 19.83694";
    private static final String EXTRAS = "?units=ca&exclude=hourly,minutely,alerts,flags";
    private static final String URL = BASE_URL + SECRET_KEY + COORDINATES + EXTRAS;

    private HttpHelper httpHelper;

    LinearLayout linearLayoutTemperature, linearLayoutSns, linearLayoutWind;
    TextView day, location;
    TextView textViewTemperature, textViewPressure, textViewHumidity;
    TextView textViewSunrise, textViewSunset;
    TextView textViewWindSpeed, textViewWindDirection;
    Button buttonTemperature, buttonSns, buttonWind;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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

        httpHelper = new HttpHelper();
        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        }).start();

        day = findViewById(R.id.textViewDetailsDay);
        day.setText(String.format("%s %s", getString(R.string.textViewDetailsDay), Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())));

        location = findViewById(R.id.textViewDetailsLocation);
        location.setText(String.format("%s %s", getString(R.string.textViewDetailsLocation), getLocationFromMain()));

        linearLayoutTemperature = findViewById(R.id.temperatureLinearLayout);
        linearLayoutSns = findViewById(R.id.snsLinearLayout);
        linearLayoutWind = findViewById(R.id.windLinearLayout);

        spinner = findViewById(R.id.spinnerDetailsTemperature);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerDetailsCF, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();
                if (linearLayoutTemperature.getVisibility() == View.VISIBLE)
                    Toast.makeText(parent.getContext(), str, Toast.LENGTH_SHORT).show();

                try {
                    double temp = Double.parseDouble(textViewTemperature.getText().toString().split(" ")[1]);

                    if (parent.getItemAtPosition(position).toString().equals("F"))
                        temp = temp * 1.8 + 32;
                    else
                        temp = (temp - 32) / 1.8;

                    textViewTemperature.setText(String.format("%s %s", getString(R.string.textViewDetailsTemperature), (int) temp));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

    private String getLocationFromMain() {

        String location = "Novi Sad";

        try {
            if (getIntent().hasExtra(KEY))
                location = getIntent().getStringExtra(KEY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return location;
    }

    private void refreshData() {

        try {
            JSONObject jsonObject = httpHelper.getJSONObjectFromURL(URL);
            JSONObject currently = jsonObject.getJSONObject("currently");
            JSONObject daily = jsonObject.getJSONObject("daily");
            JSONArray data = daily.getJSONArray("data");

            String temperature = currently.getString("temperature");
            String humidity = currently.getString("humidity");
            String pressure = currently.getString("pressure");
            String windSpeed = currently.getString("windSpeed");

            final String sunrise = data.getJSONObject(0).getString("sunriseTime");
            final String sunset = data.getJSONObject(0).getString("sunsetTime");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewSunrise.setText(convertUnixTime(sunrise));
                    textViewSunset.setText(convertUnixTime(sunset));
                }
            });


            textViewTemperature.setText(String.format("%s %s", getString(R.string.textViewDetailsTemperature), String.valueOf(temperature)));
            textViewHumidity.setText(String.format("%s %s", getString(R.string.textViewDetailsHumidity), humidity));
            textViewPressure.setText(String.format("%s %s", getString(R.string.textViewDetailsPressure), pressure));
            textViewWindSpeed.setText(String.format("%s %s", getString(R.string.textViewDetailsWindSpeed), windSpeed));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String convertUnixTime(String time) {
        long ut = Long.parseLong(time);
        Date date = new Date(ut * 1000L);

        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(date);

    }
}
