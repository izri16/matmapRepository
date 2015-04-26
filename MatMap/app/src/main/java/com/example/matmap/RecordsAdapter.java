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
public class RecordsAdapter extends BaseAdapter{

    private Context context;
    private String[] data;
    private Activity activity;

    public RecordsAdapter(Context context, String[] data, Activity activity) {
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
            vi = inflater.inflate(R.layout.simple_record_row, parent, false);

        TextView name = (TextView) vi.findViewById(R.id.simpleRecordRoomName);
        name.setText(data[position]);
        TextView time = (TextView) vi.findViewById(R.id.simpleRecordTime);
        time.setText(data[position]);
        return vi;
    }

}
