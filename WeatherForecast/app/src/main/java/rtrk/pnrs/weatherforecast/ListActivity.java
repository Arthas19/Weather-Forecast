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
        myAdapter.addItem(new MyItem("novi sad", null));
        myAdapter.addItem(new MyItem("sombor", null));
        myAdapter.addItem(new MyItem("subotica", null));

        listView.setAdapter(myAdapter);
        //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
