package rtrk.pnrs.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MyItem> arrayList;

    public MyAdapter(Context context) {
        this.context = context;
        arrayList = new ArrayList<MyItem>();
    }

    public boolean addItem(MyItem item) {
        if (!isItemInList(item)) {
            arrayList.add(item);
            notifyDataSetChanged();

            return true;
        } else {
            return false;
        }
    }

    public void removeItem(int i) {
        arrayList.remove(i);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;

        try {
            rv = arrayList.get(position);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        // convertView is providing us with Recycle Mechanism, and he is a good friend of our RAM
        View view = convertView;
        ViewHolder holder;

        // There is still screen space!
        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.element_row, null);
            holder = new ViewHolder();

            holder.textView = (TextView) view.findViewById(R.id.textViewElementRow);
            holder.radioButton = (RadioButton) view.findViewById(R.id.radioButtonElementRow);
            view.setTag(holder);
        }

        MyItem item = (MyItem) getItem(position);
        holder = (ViewHolder) view.getTag();

        holder.textView.setText(item.getText());
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    
                }
            }
        });

        return view;
    }

    private boolean isItemInList(MyItem myItem) {
        for (MyItem item : arrayList) {
            if (item.getText().equals(myItem.getText()))
                return true;
        }

        return false;
    }

    private class ViewHolder {
        private TextView textView = null;
        private RadioButton radioButton = null;
    }
}
