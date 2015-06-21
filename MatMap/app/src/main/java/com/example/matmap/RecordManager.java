package com.example.matmap;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordManager extends ActionBarActivity {
    private ListView recordsListView;
    private List<String> items;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private TextView noRecords;
    private boolean disableMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_manager);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_manager, menu);

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
     * does all necessary work to initialize all important variables
     */
    private void init() {
        recordsListView = (ListView)findViewById(R.id.recordList);
        items = new ArrayList<>();

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name, timestamp, group_id " +
                                                  "FROM search_data ORDER BY timestamp DESC",
                                                   null);

        constantsCursor.moveToFirst();

        int oldGroupId = -1;
        while(!constantsCursor.isAfterLast()) {
            int groupId = constantsCursor.getInt(2);

            Log.d("String(0)", constantsCursor.getString(0));
            Log.d("String(2)", constantsCursor.getString(2));

            if (groupId != oldGroupId) {
                items.add(constantsCursor.getString(0) + "-del-i-mi-ner-" +
                        constantsCursor.getString(1) + "-del-i-mi-ner-" +
                        constantsCursor.getString(2));
                oldGroupId = groupId;
                Log.d("RR", "new record");
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

        recordsListView.setAdapter(new RecordsAdapter(this, Arrays.copyOf(items.toArray(),
                                   items.toArray().length, String[].class), this));


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
        Intent intent = new Intent(this, RecordsDeleteManager.class);
        intent.putExtra("called_from_history", false);
        startActivity(intent);
    }

    /**
     * When invoked opens new FindRecordByName activity
     */
    private void openFindRecordByName() {
        Intent intent = new Intent(this, FindRecordByName.class);
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
