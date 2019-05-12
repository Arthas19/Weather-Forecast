package rtrk.pnrs.weatherforecast.MyLittleHelpers;

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
import java.util.Collections;

import rtrk.pnrs.weatherforecast.DetailsActivity;
import rtrk.pnrs.weatherforecast.R;

public class MyListAdapter extends BaseAdapter {

    private static String KEY = "city";

    private Context mContext;
    private ArrayList<String> mList;

    public MyListAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    public void update(String[] cities) {
        mList.clear();
        if (cities != null)
            Collections.addAll(mList, cities);

        notifyDataSetChanged();
    }

    public boolean isItemInList(String item) {
        for (String str :
                mList) {
            if (item.equalsIgnoreCase(str))
                return true;
        }

        return false;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        String city = null;

        try {
            city = mList.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return city;
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        final String item = getItem(position);
        final ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        viewHolder.textView.setText(item);
        viewHolder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra(KEY, item);

                    viewHolder.radioButton.setChecked(false);

                    mContext.startActivity(intent);
                }
            }
        });

        return rowView;
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
