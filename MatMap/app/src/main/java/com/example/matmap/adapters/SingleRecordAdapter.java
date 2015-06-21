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
 * Adapter for RecordGroup Activity
 */
public class SingleRecordAdapter extends BaseAdapter {

    private Context context;
    private JSONObject[] data;
    private Activity activity;

    public SingleRecordAdapter(Context context, JSONObject[] data, Activity activity) {
        this.context = context;
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return data.length;
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
            vi = inflater.inflate(R.layout.three_items_row, parent, false);

        TextView ssid = (TextView) vi.findViewById(R.id.singleRecordSSID);
        TextView strength = (TextView) vi.findViewById(R.id.singleRecordBSSID);
        TextView bssid = (TextView) vi.findViewById(R.id.singleRecordStrength);
        int id = 0;

        try {
            ssid.setText(data[position].getString("ssid"));
            strength.setText(data[position].getString("strength"));
            bssid.setText(data[position].getString("bssid"));
            id = Integer.valueOf(data[position].getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vi.setTag(id);

        return vi;
    }

}
