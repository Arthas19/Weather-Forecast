package rtrk.pnrs.weatherforecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private ListView listView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        button = findViewById(R.id.buttonListActivity);
        editText = findViewById(R.id.editTextListActivity);
        listView = findViewById(R.id.listViewListActivity);

        myAdapter = new MyAdapter(this);
        myAdapter.addItem(new MyItem("Novi Sad", null, null));
        myAdapter.addItem(new MyItem("Sombor", null, null));
        myAdapter.addItem(new MyItem("Subotica", null, null));

        listView.setAdapter(myAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt = editText.getText().toString();

                if (!txt.equals("")) {
                    if (!myAdapter.addItem(new MyItem(txt, null, null))) {
                        Toast.makeText(button.getContext(), "City already in the list", Toast.LENGTH_SHORT).show();
                    }
                    editText.setText(null);
                } else {
                    Toast.makeText(button.getContext(), "You need to enter a new city", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
