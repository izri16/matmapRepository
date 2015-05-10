package com.example.matmap;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordsDeleteManager extends Activity {
    private ListView recordsListView;
    private List<String> items;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private RecordsDeleteAdapter recordsAdapter;
    private boolean switchAll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_delete_manager);

        recordsListView = (ListView)findViewById(R.id.recordDeleteList);
        items = new ArrayList<>();

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name, timestamp, group_id FROM search_data ORDER BY timestamp DESC", null);

        constantsCursor.moveToFirst();

        int oldGroupId = -1;
        while(!constantsCursor.isAfterLast()) {
            int groupId = constantsCursor.getInt(2);

            if (groupId != oldGroupId) {
                items.add(constantsCursor.getString(0) + "-del-i-mi-ner-" +
                        constantsCursor.getString(1) + "-del-i-mi-ner-" +
                        constantsCursor.getString(2));
                oldGroupId = groupId;
            }

            constantsCursor.moveToNext();
        }

        this.recordsAdapter = new RecordsDeleteAdapter(this, Arrays.copyOf(items.toArray(), items.toArray().length, String[].class), this);
        this.recordsListView.setAdapter(this.recordsAdapter);

        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                recordsAdapter.setRecentSwitch(false);
                recordsAdapter.reCheck(position);
                recordsAdapter.notifyDataSetChanged();
                Log.d("FIRST ID", String.valueOf(position));

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_records_delete_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void chooseAll(View view) {

        //bol odkliknuty panel
        if (!view.equals(view.findViewById(R.id.boxToCheckAll))) {
            CheckBox c = (CheckBox) view.findViewById(R.id.boxToCheckAll);
            c.setChecked(recordsAdapter.checkIfSomeNotSwitched());
        }
        //bol odkliknuty checkbox
        else {
            CheckBox c = (CheckBox) view;
            c.setChecked(recordsAdapter.checkIfSomeNotSwitched());
        }

        int itemsCount = this.recordsAdapter.getCount();

        if (recordsAdapter.checkIfSomeNotSwitched()) {
            recordsAdapter.setSwitchAll(true);
            this.switchAll = true;
        }
        else {
            recordsAdapter.setSwitchAll(false);
            this.switchAll = false;
        }

        recordsAdapter.setRecentSwitch(true);
        recordsAdapter.notifyDataSetChanged();

        /*
         * Pri premazavani a zvyraznovani nestaci len zmenit na opacnu hodnotu!!!
         */
        for (int i = 0; i < itemsCount; i++) {
            recordsAdapter.setValueAtPosition(i, switchAll);
            recordsAdapter.getView(i, null, null);
        }

    }

    public void checkBoxClicked(View view) {
        Integer l = (Integer) view.getTag();

        recordsAdapter.setRecentSwitch(false);
        recordsAdapter.reCheck(l);
        recordsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
    }
}
