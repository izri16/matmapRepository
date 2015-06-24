package com.example.matmap.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.matmap.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter for RecordsManager Activity
 */
public class AllRecordsAdapter extends BaseAdapter {
    private Context context;
    private JSONObject[] data;
    private Activity activity;

    public AllRecordsAdapter(Context context, JSONObject[] data, Activity activity) {
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        } else {
            return data.length;
        }
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (vi == null)
            vi = inflater.inflate(R.layout.two_items_row, parent, false);

        TextView name = (TextView) vi.findViewById(R.id.simpleRecordRoomName);
        TextView date = (TextView) vi.findViewById(R.id.simpleRecordTime);
        int groupId = 0;
        try {
            name.setText(data[position].getString("room_name"));
            date.setText(data[position].getString("date"));
            groupId = Integer.valueOf(data[position].getString("group_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vi.setTag(groupId);

        return vi;
    }

}
