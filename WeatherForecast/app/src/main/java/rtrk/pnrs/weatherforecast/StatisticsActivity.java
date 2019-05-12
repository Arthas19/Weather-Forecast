package rtrk.pnrs.weatherforecast;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private TextView tvMonTemp, tvMonHumidity, tvMonPressure;
    private TextView tvTueTemp, tvTueHumidity, tvTuePressure;
    private TextView tvWedTemp, tvWedHumidity, tvWedPressure;
    private TextView tvThuTemp, tvThuHumidity, tvThuPressure;
    private TextView tvFriTemp, tvFriHumidity, tvFriPressure;
    private TextView tvSatTemp, tvSatHumidity, tvSatPressure;
    private TextView tvSunTemp, tvSunHumidity, tvSunPressure;

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

        tvMonTemp = findViewById(R.id.textViewStatsTableMonTemp);
        tvMonHumidity = findViewById(R.id.textViewStatsTableMonHumidity);
        tvMonPressure = findViewById(R.id.textViewStatsTableMonPressure);

        tvTueTemp = findViewById(R.id.textViewStatsTableTueTemp);
        tvTueHumidity = findViewById(R.id.textViewStatsTableTueHumidity);
        tvTuePressure = findViewById(R.id.textViewStatsTableTuePressure);

        tvWedTemp = findViewById(R.id.textViewStatsTableWedTemp);
        tvWedHumidity = findViewById(R.id.textViewStatsTableWedHumidity);
        tvWedPressure = findViewById(R.id.textViewStatsTableWedPressure);

        tvThuTemp = findViewById(R.id.textViewStatsTableThuTemp);
        tvThuHumidity = findViewById(R.id.textViewStatsTableThuHumidity);
        tvThuPressure = findViewById(R.id.textViewStatsTableThuPressure);

        tvFriTemp = findViewById(R.id.textViewStatsTableFriTemp);
        tvFriHumidity = findViewById(R.id.textViewStatsTableFriHumidity);
        tvFriPressure = findViewById(R.id.textViewStatsTableFriPressure);

        tvSatTemp = findViewById(R.id.textViewStatsTableSatTemp);
        tvSatHumidity = findViewById(R.id.textViewStatsTableSatHumidity);
        tvSatPressure = findViewById(R.id.textViewStatsTableSatPressure);

        tvSunTemp = findViewById(R.id.textViewStatsTableSunTemp);
        tvSunHumidity = findViewById(R.id.textViewStatsTableSunHumidity);
        tvSunPressure = findViewById(R.id.textViewStatsTableSunPressure);

        fillTable();
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

    private void fillTable() {
        Forecast[] forecasts = dbWeatherHelper.getItems(getCity(), 0);

        TextView tvBold;
        String currDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        boolean[] isChecked = {false, false, false, false, false, false, false};
        for (Forecast f : forecasts)
            switch (f.getWeekDay()) {
                case "Mon":
                    if (!isChecked[0]) {
                        if (currDate.equals(f.getDate())) {
                            tvBold = findViewById(R.id.textViewStatsTableMon);
                            tvBold.setTextColor(getColor(R.color.colorPrimary));
                        }
                        tvMonTemp.setText(String.valueOf(f.getTemperature()));
                        tvMonPressure.setText(String.valueOf(f.getPressure()));
                        tvMonHumidity.setText(String.valueOf(f.getHumidity()));
                        isChecked[0] = true;
                    }
                    break;
                case "Tue":
                    if (!isChecked[1]) {
                        if (currDate.equals(f.getDate())) {
                            tvBold = findViewById(R.id.textViewStatsTableTue);
                            tvBold.setTextColor(getColor(R.color.colorPrimary));
                        }
                        tvTueTemp.setText(String.valueOf(f.getTemperature()));
                        tvTuePressure.setText(String.valueOf(f.getPressure()));
                        tvTueHumidity.setText(String.valueOf(f.getHumidity()));
                        isChecked[1] = true;
                    }
                    break;
                case "Wed":
                    if (!isChecked[2]) {
                        if (currDate.equals(f.getDate())) {
                            tvBold = findViewById(R.id.textViewStatsTableWed);
                            tvBold.setTextColor(getColor(R.color.colorPrimary));
                        }
                        tvWedTemp.setText(String.valueOf(f.getTemperature()));
                        tvWedPressure.setText(String.valueOf(f.getPressure()));
                        tvWedHumidity.setText(String.valueOf(f.getHumidity()));
                        isChecked[2] = true;
                    }
                    break;
                case "Thu":
                    if (!isChecked[3]) {
                        if (currDate.equals(f.getDate())) {
                            tvBold = findViewById(R.id.textViewStatsTableThu);
                            tvBold.setTextColor(getColor(R.color.colorPrimary));
                        }
                        tvThuTemp.setText(String.valueOf(f.getTemperature()));
                        tvThuPressure.setText(String.valueOf(f.getPressure()));
                        tvThuHumidity.setText(String.valueOf(f.getHumidity()));
                        isChecked[3] = true;
                    }
                    break;
                case "Fri":
                    if (!isChecked[4]) {
                        if (currDate.equals(f.getDate())) {
                            tvBold = findViewById(R.id.textViewStatsTableFri);
                            tvBold.setTextColor(getColor(R.color.colorPrimary));
                        }
                        tvFriTemp.setText(String.valueOf(f.getTemperature()));
                        tvFriPressure.setText(String.valueOf(f.getPressure()));
                        tvFriHumidity.setText(String.valueOf(f.getHumidity()));
                        isChecked[4] = true;
                    }
                    break;
                case "Sat":
                    if (!isChecked[5]) {
                        if (currDate.equals(f.getDate())) {
                            tvBold = findViewById(R.id.textViewStatsTableSat);
                            tvBold.setTextColor(getColor(R.color.colorPrimary));
                        }
                        tvSatTemp.setText(String.valueOf(f.getTemperature()));
                        tvSatPressure.setText(String.valueOf(f.getPressure()));
                        tvSatHumidity.setText(String.valueOf(f.getHumidity()));
                        isChecked[5] = true;
                    }
                    break;
                case "Sun":
                    if (!isChecked[6]) {
                        if (currDate.equals(f.getDate())) {
                            tvBold = findViewById(R.id.textViewStatsTableSun);
                            tvBold.setTextColor(getColor(R.color.colorPrimary));
                        }
                        tvSunTemp.setText(String.valueOf(f.getTemperature()));
                        tvSunPressure.setText(String.valueOf(f.getPressure()));
                        tvSunHumidity.setText(String.valueOf(f.getHumidity()));
                        isChecked[6] = true;
                    }
                    break;
            }

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

    private void addTableRow(String weekday, double temp) {
        LayoutInflater inflater = (LayoutInflater) table.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.table_row, null);

        TextView tv1 = findViewById(R.id.tableRowCel1);
        TextView tv2 = findViewById(R.id.tableRowCel2);
        TextView tv3 = findViewById(R.id.tableRowCel3);
        TextView tv4 = findViewById(R.id.tableRowCel4);

        tv1.setText("");
        tv2.setText(weekday);
        tv3.setText("");
        tv4.setText(String.valueOf(temp));

        table.addView(view);
    }
}
