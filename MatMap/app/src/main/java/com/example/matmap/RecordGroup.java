package com.example.matmap;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordGroup extends ActionBarActivity {
    private ListView listView;
    private List<String> items;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Vytiahne id skupiny prave odkliknuteho zaznamu
         */
        int id = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = Integer.valueOf(extras.getString("groupId"));
        }

        setContentView(R.layout.activity_record_group);

        listView = (ListView)findViewById(R.id.singleRecordListView);
        items = new ArrayList<>();

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT SSID, STRENGTH, BSSID, _id FROM search_data WHERE group_id = '" + id + "' ORDER BY _id DESC", null);

        constantsCursor.moveToFirst();

        while(!constantsCursor.isAfterLast()) {

            items.add(constantsCursor.getString(0) + "-del-i-mi-ner-" +
                      constantsCursor.getString(1) + "-del-i-mi-ner-" +
                      constantsCursor.getString(2) + "-del-i-mi-ner-" +
                      constantsCursor.getString(3));

            constantsCursor.moveToNext();
        }



        listView.setAdapter(new SingleRecordAdapter(this, Arrays.copyOf(items.toArray(), items.toArray().length, String[].class), this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String recordId = String.valueOf(view.getTag());

                Intent i = new Intent(getApplicationContext(), SingleRecord.class);
                i.putExtra("recordId", recordId);
                startActivity(i);

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_record, menu);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
    }
}
