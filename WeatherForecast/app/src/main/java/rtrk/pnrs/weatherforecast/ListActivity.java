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
    private ListView listView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        button = findViewById(R.id.buttonListActivity);
        editText = findViewById(R.id.editTextListActivity);
        listView = findViewById(R.id.listViewListActivity);

        adapter = new MyAdapter(this);

        adapter.addItem(new MyItem("Copenhagen", null));
        adapter.addItem(new MyItem("Oslo", null));
        adapter.addItem(new MyItem("Novi Sad", null));
        adapter.addItem(new MyItem("Tallinn", null));
        adapter.addItem(new MyItem("Istanbul", null));
        adapter.addItem(new MyItem("Nice", null));
        adapter.addItem(new MyItem("Edinburgh", null));
        adapter.addItem(new MyItem("Naples", null));
        adapter.addItem(new MyItem("Toulouse", null));
        adapter.addItem(new MyItem("Amsterdam", null));
        adapter.addItem(new MyItem("Lisbon", null));

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
                if (!txt.equals("")) {
                    txt = capWords(txt);
                    if (!adapter.addItem(new MyItem(txt, null))) {
                        Toast.makeText(button.getContext(), "City already in the list", Toast.LENGTH_SHORT).show();
                    }
                    editText.setText(null);
                } else {
                    Toast.makeText(button.getContext(), "You need to enter a new city", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String capWords(String string) {
        String[] words = string.trim().toLowerCase().split(" ");
        String rv = "";

        for (String word : words)
            rv += (Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ");

        rv = rv.trim();

        return rv;
    }
}
