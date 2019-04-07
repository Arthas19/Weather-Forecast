package rtrk.pnrs.weatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String KEY = "location";

    Button show;
    EditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = findViewById(R.id.buttonMainShow);
        location = findViewById(R.id.editTextMainLocation);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = location.getText().toString();
                txt = capWords(txt);

                if (!txt.equals(""))
                {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra(KEY, txt);

                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(show.getContext(), "You need to enter location", Toast.LENGTH_SHORT).show();
                    location.setText(null);
                }
            }
        });
    }

    private String capWords(String string) {
        String rv = "";

        try {
            String[] words = string.trim().toLowerCase().split(" ");

            for (String word : words)
                rv += (Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ");

            rv = rv.trim();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return rv;
    }
}
