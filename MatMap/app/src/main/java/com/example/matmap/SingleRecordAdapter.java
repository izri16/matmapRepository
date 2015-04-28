package com.example.matmap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by richard on 26.4.2015.
 */
public class SingleRecordAdapter extends BaseAdapter {

    private Context context;
    private String[] data;
    private Activity activity;

    public SingleRecordAdapter(Context context, String[] data, Activity activity) {
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (vi == null)
            vi = inflater.inflate(R.layout.three_items_row, parent, false);

        String[] elements = data[position].split("-del-i-mi-ner-");

        TextView ssid = (TextView) vi.findViewById(R.id.singleRecordSSID);
        ssid.setText(elements[0]);
        TextView strength = (TextView) vi.findViewById(R.id.singleRecordBSSID);
        strength.setText(elements[1]);
        TextView bssid = (TextView) vi.findViewById(R.id.singleRecordStrength);
        bssid.setText(elements[2]);

        int id = Integer.valueOf(elements[3]);
        vi.setTag(id);

        return vi;
    }

}
