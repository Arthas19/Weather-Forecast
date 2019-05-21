package rtrk.pnrs.weatherforecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import rtrk.pnrs.weatherforecast.MyLittleHelpers.DBWeatherHelper;
import rtrk.pnrs.weatherforecast.MyLittleHelpers.Forecast;
import rtrk.pnrs.weatherforecast.MyLittleHelpers.MyListAdapter;

public class ListActivity extends AppCompatActivity {


    private Button button;
    private EditText editText;
    private MyListAdapter myListAdapter;
    private DBWeatherHelper dbWeatherHelper;


    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        }).start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        button = findViewById(R.id.buttonListActivity);
        editText = findViewById(R.id.editTextListActivity);

        /* Just to make IDE happy */
        ListView listView;
        listView = findViewById(R.id.listViewListActivity);

        dbWeatherHelper = new DBWeatherHelper(this);
        myListAdapter = new MyListAdapter(this);

        listView.setAdapter(myListAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (dbWeatherHelper.remove(myListAdapter.getItem(position))) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            refreshData();
                        }
                    }).start();
                }

                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editText.getText().toString();
                city = capWords(city);

                if (!city.equals("")) {
                    if (myListAdapter.isItemInList(city)) {
                        Toast.makeText(button.getContext(), "City already in the list", Toast.LENGTH_SHORT).show();
                    } else {
                        final String finalCity = city;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Forecast forecast = new Forecast(finalCity);
                                if (dbWeatherHelper.insert(forecast)) {
                                    Log.d("Insert in DB", "SUCCESSFUL");
                                    refreshData();
                                }
                            }
                        }).start();
                    }
                } else {
                    Toast.makeText(button.getContext(), "You need to enter a new city", Toast.LENGTH_SHORT).show();
                }

                editText.setText(null);
            }
        });
    }


    private String capWords(String string) {
        String rv = "";

        try {
            String[] words = string.trim().toLowerCase().split(" ");

            StringBuilder sb = new StringBuilder();

            for (String word : words) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1));
                sb.append(" ");
            }

            rv = sb.toString();
            rv = rv.trim();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return rv;
    }

    private void refreshData() {
        final String[] cities = dbWeatherHelper.getCities();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myListAdapter.update(cities);
            }
        });
    }
}