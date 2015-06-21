package com.example.matmap.adapters;

/**
 * Created by richard on 21.6.2015.
 */

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
 * Created by richard on 26.4.2015.
 */
public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private JSONObject[] data;
    private Activity activity;

    public HistoryAdapter(Context context, JSONObject[] data, Activity activity) {
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
        try {
            name.setText(data[position].getString("room_name"));
            date.setText(data[position].getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        vi.setTag(name.getText());

        return vi;
    }

}
