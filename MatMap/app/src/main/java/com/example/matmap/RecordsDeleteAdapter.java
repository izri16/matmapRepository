package com.example.matmap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by richard on 26.4.2015.
 */
public class RecordsDeleteAdapter extends BaseAdapter {

    private Context context;
    private String[] data;
    private Activity activity;
    private boolean[] checked;
    private boolean switchToCheckedAll = false;
    private boolean recentSwitch = false;

    public RecordsDeleteAdapter(Context context, String[] data, Activity activity) {
        this.context = context;
        this.data = data;
        this.activity = activity;
        this.checked = new boolean[data.length];
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

    /**
     * Sets switchToCheckAll variable
     *
     * @param value
     */
    public void setSwitchAll(boolean value) {
        this.switchToCheckedAll = value;
        this.recentSwitch = true;
    }

    /**
     * Set checkbox value at specified position
     *
     * @param position position of checkbox in the list
     * @param value boolean value to be set on
     */
    public void setValueAtPosition(int position, boolean value) {
        this.checked[position] = value;
    }

    /**
     * Notice if the 'Choose all' bottom was switched recently
     *
     * @param value boolean value representing recent switch
     */
    public void setRecentSwitch(boolean value) {
        this.recentSwitch = value;
    }

    /**
     * Set checkbox at current position to opposite value
     *
     * @param position position of checkbox in the list
     */
    public void reCheck(int position) {
        this.checked[position] = !this.checked[position];
    }

    /**
     * Check if any item in the body of delete box is clicked
     *
     * @return true if some item is clicked
     * @return false otherwise
     */
    public boolean checkIfSomeNotSwitched() {
        boolean answer = false;

        for (int i = 0; i < this.checked.length; i++) {
            if (checked[i] == false) {
                answer = true;
                break;
            }
        }

        return answer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (vi == null)
            vi = inflater.inflate(R.layout.two_items_delete_row, parent, false);

        String[] elements = data[position].split("-del-i-mi-ner-");

        TextView name = (TextView) vi.findViewById(R.id.simpleRecordRoomName);
        name.setText(elements[0]);
        TextView time = (TextView) vi.findViewById(R.id.simpleRecordTime);
        time.setText(elements[1]);

        CheckBox cb = (CheckBox) vi.findViewById(R.id.deleteRecordsBox);

        if (this.recentSwitch) {
            cb.setChecked(this.switchToCheckedAll);
        }

        else {
            if (this.checked[position] == true) {
                cb.setChecked(true);
            } else {
                cb.setChecked(false);
            }
        }

        int groupId = Integer.valueOf(elements[2]);

        vi.setTag(groupId);
        cb.setTag(position);

        return vi;
    }


}
