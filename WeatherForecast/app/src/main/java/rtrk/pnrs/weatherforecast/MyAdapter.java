package rtrk.pnrs.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter implements AdapterView.OnItemLongClickListener {

    private static String KEY = "location";

    private Context context;
    private ArrayList<MyItem> myItems;

    public MyAdapter(Context context) {
        this.context = context;
        myItems = new ArrayList<MyItem>();
    }

    public boolean addItem(MyItem item) {
        if (!isItemInList(item)) {
            myItems.add(item);
            notifyDataSetChanged();

            return true;
        }

        return false;
    }

    public void remove(MyItem item) {
        myItems.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myItems.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;

        try {
            rv = myItems.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return rv;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // convertView is providing us with Recycle Mechanism, and he is a good friend of our RAM
        View view = convertView;
        ViewHolder holder;

        // There is still screen space!
        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.element_row, null);
            holder = new ViewHolder();

            holder.textView = view.findViewById(R.id.textViewElementRow);
            holder.radioButton = view.findViewById(R.id.radioButtonElementRow);
            view.setTag(holder);
        }

        final MyItem item = (MyItem) getItem(position);

        holder = (ViewHolder) view.getTag();

        holder.textView.setText(item.getText());
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!myItems.isEmpty()) {
                    remove(item);
                    notifyDataSetChanged();
                }

                return true;
            }
        });

        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(KEY, item.getText());

                    context.startActivity(intent);
                }
            }
        });

        return view;
    }

    private boolean isItemInList(MyItem item) {
        for (MyItem i : myItems)
            if (i.getText().equals(item.getText()))
                return true;

        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        myItems.remove(position);

        return true;
    }

    private class ViewHolder {
        private TextView textView = null;
        private RadioButton radioButton = null;
    }
}
