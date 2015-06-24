package com.example.matmap.record_managers;

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
import android.widget.TextView;

import com.example.matmap.MatMapDatabase;
import com.example.matmap.R;
import com.example.matmap.adapters.AllRecordsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manager for all records in application
 */
public class RecordsManager extends ActionBarActivity {
    private ListView recordsListView;
    private List<JSONObject> items;
    private SQLiteDatabase matMapDatabase;
    private Cursor constantsCursor;
    private TextView noRecords;
    private boolean disableMenu = false;
    private boolean filter = false;
    private String filterRoom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_manager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.filter = extras.getBoolean("filter");
            this.filterRoom = extras.getString("filter_room");
        }

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_records_manager, menu);

        MenuItem del = menu.findItem(R.id.action_delete_records);
        MenuItem find = menu.findItem(R.id.action_find_record);

        if (this.disableMenu) {
            del.setVisible(false);
            find.setVisible(false);
        }
        else {
            del.setVisible(true);
            find.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_records) {
            openRecordsDeleteManager();
        }
        else if (id == R.id.action_find_record) {
            openFindRecordByName();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Does all necessary work to initialize all important variables
     */
    private void init() {
        recordsListView = (ListView)findViewById(R.id.recordList);
        items = new ArrayList<>();

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();

        String query= "";
        if (!filter) {
            query = "SELECT room_name, timestamp, group_id " +
                    "FROM search_data ORDER BY timestamp DESC";
        } else {
            query = "SELECT room_name, timestamp, group_id " +
                    "FROM search_data WHERE room_name = '" + filterRoom + "'" +
                    "ORDER BY timestamp DESC";
        }

        constantsCursor = matMapDatabase.rawQuery(query, null);

        constantsCursor.moveToFirst();

        int oldGroupId = -1;
        while(!constantsCursor.isAfterLast()) {
            int groupId = constantsCursor.getInt(2);

            if (groupId != oldGroupId) {
                JSONObject json = new JSONObject();

                try {
                    json.put("room_name", constantsCursor.getString(0));
                    json.put("date", constantsCursor.getString(1));
                    json.put("group_id", constantsCursor.getString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                items.add(json);
                oldGroupId = groupId;
            }


            constantsCursor.moveToNext();
        }

        this.noRecords = (TextView) findViewById(R.id.noRecords);

        if (!items.isEmpty()) {
            this.noRecords.setVisibility(View.GONE);
            this.disableMenu = false;
            invalidateOptionsMenu();
        }
        else {
            this.noRecords.setVisibility(View.VISIBLE);
            this.disableMenu = true;
            invalidateOptionsMenu();
        }

        recordsListView.setAdapter(new AllRecordsAdapter(this, Arrays.copyOf(items.toArray(),
                                   items.toArray().length, JSONObject[].class), this));


        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String groupId = String.valueOf(view.getTag());

                Intent i = new Intent(getApplicationContext(), RecordGroup.class);
                i.putExtra("groupId", groupId);

                startActivity(i);

            }

        });
    }

    /**
     * When invoked opens new RecordsDeleteManager activity
     */
    private void openRecordsDeleteManager() {
        Intent intent = new Intent(this, DeleteManager.class);
        intent.putExtra("called_from_history", false);
        startActivity(intent);
    }

    /**
     * When invoked opens new FindRecordByName activity
     */
    private void openFindRecordByName() {
        Intent intent = new Intent(this, FindRecordsByName.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
        init();
    }

}
