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
import android.widget.ListView;

import com.example.matmap.adapters.SingleRecordAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordGroup extends ActionBarActivity {
    private ListView listView;
    private List<JSONObject> items;
    private SQLiteDatabase matMapDatabase;
    private Cursor constantsCursor;
    private String roomName = "";
    private int groupId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_group);

         init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rename_record) {
            openRenameRecord();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        /*
         * Vytiahne id skupiny prave odkliknuteho zaznamu
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.groupId = Integer.valueOf(extras.getString("groupId"));
        }

        listView = (ListView)findViewById(R.id.singleRecordListView);
        items = new ArrayList<>();

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT SSID, STRENGTH, BSSID, _id, room_name " +
                                                  "FROM search_data WHERE group_id = '" + groupId +
                                                   "' ORDER BY _id DESC", null);

        constantsCursor.moveToFirst();
        this.roomName = constantsCursor.getString(4);
        getSupportActionBar().setTitle(roomName);

        while(!constantsCursor.isAfterLast()) {
            JSONObject json = new JSONObject();

            try {
                json.put("ssid", constantsCursor.getString(0));
                json.put("strength", constantsCursor.getString(1));
                json.put("bssid", constantsCursor.getString(2));
                json.put("id", constantsCursor.getString(3));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            items.add(json);
            constantsCursor.moveToNext();
        }


        constantsCursor.close();

        listView.setAdapter(new SingleRecordAdapter(this, Arrays.copyOf(items.toArray(),
                                                    items.toArray().length, JSONObject[].class), this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String recordId = String.valueOf(view.getTag());

                Intent i = new Intent(getApplicationContext(), DetailInfo.class);
                i.putExtra("recordId", recordId);

                startActivity(i);

            }

        });
    }

    private void openRenameRecord() {
        Intent intent = new Intent(this, RenameRecord.class);
        intent.putExtra("roomName", roomName);
        intent.putExtra("groupId", String.valueOf(groupId));
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
    }
}
