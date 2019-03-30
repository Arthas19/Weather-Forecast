package rtrk.pnrs.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        button = findViewById(R.id.buttonListActivity);
        editText = findViewById(R.id.editTextListActivity);
        listView = findViewById(R.id.listViewListActivity);

        final MyAdapter myAdapter = new MyAdapter(this);
        myAdapter.addItem(new MyItem("Novi Sad", null));
        myAdapter.addItem(new MyItem("Sombor", null));
        myAdapter.addItem(new MyItem("Subotica", null));

        listView.setAdapter(myAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt = editText.getText().toString().toLowerCase();

                if (!txt.equals("")) {
                    if (!myAdapter.addItem(new MyItem(txt, null))) {
                        Toast.makeText(button.getContext(), "City already in the list", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(button.getContext(), "You need to enter a new city", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!myAdapter.isEmpty()) {
                    myAdapter.removeItem(position);
                }

                return true;
            }
        });
    }
}
