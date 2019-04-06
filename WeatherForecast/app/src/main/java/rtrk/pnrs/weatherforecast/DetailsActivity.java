package rtrk.pnrs.weatherforecast;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {

    private static final String KEY = "location";

    private static final String BASE_URL = "https://api.darksky.net/forecast/";
    private static final String SECRET_KEY = "1300b6a561b5e3fe758d6ba61fed2701/";
    private static final String COORDINATES = "45.25167, 19.83694";
    private static final String UNITS = "?units=ca";
    private static final String URL = BASE_URL + SECRET_KEY + COORDINATES + UNITS;

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

        day = findViewById(R.id.textViewDetailsDay);
        day.setText( String.format( "%s %s", getString(R.string.textViewDetailsDay), Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) ) );

        location = findViewById(R.id.textViewDetailsLocation);
        location.setText( String.format( "%s %s", getString(R.string.textViewDetailsLocation), getLocationFromMain() ) );

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

                if (parent.getItemAtPosition(position).toString().equals("F")) {
                    // TODO
                } else {
                    // TODO
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        }).start();
    }

    private String getLocationFromMain() {

        String location = "Novi Sad";

        if (getIntent().hasExtra(KEY)) {
            location = getIntent().getStringExtra(KEY);
            location = capWords(location);
        } else {
            throw new IllegalArgumentException("Activity cannot find extras " + KEY);
        }

        return location;
    }

    private String capWords(String string) {
        String[] words = string.trim().toLowerCase().split(" ");
        String rv = "";

        for (String word : words)
            rv += (Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ");

        rv = rv.trim();

        return rv;
    }

    private void refreshData() {

        try {
            JSONObject jsonObject = httpHelper.getJSONObjectFromURL( URL );
            JSONObject currently = jsonObject.getJSONObject("currently");

            String temperature = currently.getString("temperature");
            String humidity = currently.getString("humidity");
            String pressure = currently.getString("pressure");
            String windSpeed = currently.getString("windSpeed");

            textViewTemperature.setText( String.format("%s %s", getString(R.string.textViewDetailsTemperature), temperature) );
            textViewHumidity.setText( String.format("%s %s", getString(R.string.textViewDetailsHumidity), humidity) );
            textViewPressure.setText( String.format("%s %s", getString(R.string.textViewDetailsPressure), pressure) );
            textViewWindSpeed.setText( String.format("%s %s", getString(R.string.textViewDetailsWindSpeed), windSpeed) );

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
