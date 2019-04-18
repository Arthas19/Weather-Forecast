package rtrk.pnrs.weatherforecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    //private ListView listView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //DBHelper dbHelper = new DBHelper(this);

        button = findViewById(R.id.buttonListActivity);
        editText = findViewById(R.id.editTextListActivity);

        /* Just to make IDE happy */
        ListView listView;
        listView = findViewById(R.id.listViewListActivity);

        adapter = new MyAdapter(this);

        adapter.addItem(new MyItem("Copenhagen"));
        adapter.addItem(new MyItem("Oslo"));
        adapter.addItem(new MyItem("Novi Sad"));
        adapter.addItem(new MyItem("Tallinn"));
        adapter.addItem(new MyItem("Istanbul"));
        adapter.addItem(new MyItem("Nice"));
        adapter.addItem(new MyItem("Edinburgh"));
        adapter.addItem(new MyItem("Naples"));
        adapter.addItem(new MyItem("Toulouse"));
        adapter.addItem(new MyItem("Amsterdam"));
        adapter.addItem(new MyItem("Lisbon"));

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(adapter.getItem(position));

                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = editText.getText().toString();
                txt = capWords(txt);

                if (!txt.equals("")) {
                    if (!adapter.addItem(new MyItem(txt))) {
                        Toast.makeText(button.getContext(), "City already in the list", Toast.LENGTH_SHORT).show();
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
}
