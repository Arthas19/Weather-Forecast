package rtrk.pnrs.weatherforecast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private static String KEY = "location";

    private Context context;
    private ArrayList<MyItem> arrayList;

    MyAdapter(Context context) {
        this.context = context;
        arrayList = new ArrayList<>();
    }

    boolean addItem(MyItem item) {
        if (!isItemInList(item)) {
            arrayList.add(item);

            notifyDataSetChanged();

            return true;
        }
        return false;
    }

    void remove(MyItem item) {
        arrayList.remove(item);
        notifyDataSetChanged();
    }

    private boolean isItemInList(MyItem item) {
        for (MyItem it :
                arrayList) {
            if (it.getText().equalsIgnoreCase(item.getText()))
                return true;
        }

        return false;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public MyItem getItem(int position) {
        MyItem rv = null;

        try {
            rv = arrayList.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return rv;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // convertView is providing us with Recycle Mechanism, and he is a good friend of our RAM
        View rowView;
        // Adding view on empty spot, there is still empty screen space!
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.element_row, null /*parent*/);

            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        } else {
            rowView = convertView;
        }

        final MyItem item = getItem(position);
        final ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        viewHolder.textView.setText(item.getText());
        viewHolder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(KEY, item.getText());

                    viewHolder.radioButton.setChecked(false);

                    context.startActivity(intent);
                }
            }
        });

        return rowView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        private TextView textView;
        private RadioButton radioButton;

        private ViewHolder(View view) {
            this.textView = view.findViewById(R.id.textViewElementRow);
            this.radioButton = view.findViewById(R.id.radioButtonElementRow);
        }
    }
}
