package rtrk.pnrs.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    public static final String KEY = "location";

    LinearLayout linearLayoutTemperature, linearLayoutSns, linearLayoutWind;
    TextView day, location;
    Button temperature, sns, wind;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        temperature = findViewById(R.id.buttonDetailsTemperature);
        sns = findViewById(R.id.buttonDetailsSns);
        wind = findViewById(R.id.buttonDetailsWind);

        day = findViewById(R.id.textViewDetailsDay);
        day.setText(getResources().getString(R.string.textViewDetailsDay) + ' ' + Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));

        location = findViewById(R.id.textViewDetailsLocation);
        location.setText(getResources().getString(R.string.textViewDetailsLocation) + ' ' + getLocationFromMain());

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
                Toast.makeText(parent.getContext(), str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayoutTemperature.getVisibility() == View.INVISIBLE) {
                    linearLayoutTemperature.setVisibility(View.VISIBLE);

                    linearLayoutSns.setVisibility(View.INVISIBLE);
                    linearLayoutWind.setVisibility(View.INVISIBLE);
                }
            }
        });

        sns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayoutSns.getVisibility() == View.INVISIBLE) {
                    linearLayoutSns.setVisibility(View.VISIBLE);

                    linearLayoutTemperature.setVisibility(View.INVISIBLE);
                    linearLayoutWind.setVisibility(View.INVISIBLE);
                }
            }
        });

        wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayoutWind.getVisibility() == View.INVISIBLE) {
                    linearLayoutWind.setVisibility(View.VISIBLE);

                    linearLayoutTemperature.setVisibility(View.INVISIBLE);
                    linearLayoutSns.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private String getLocationFromMain() {

        String location = "Novi Sad";

        if(getIntent().hasExtra(KEY))
            location = getIntent().getStringExtra(KEY);
        else
            throw new IllegalArgumentException("Activity cannot find extras " + KEY);

        return location;
    }
}
