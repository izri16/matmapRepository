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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordManager extends ActionBarActivity {
    private ListView recordsListView;
    private List<String> items;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private RecordsAdapter recordsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_manager);

        recordsListView = (ListView)findViewById(R.id.recordList);
        items = new ArrayList<>();

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name, timestamp, group_id FROM search_data ORDER BY timestamp DESC", null);

        constantsCursor.moveToFirst();

        int oldGroupId = -1;
        while(!constantsCursor.isAfterLast()) {
            int groupId = constantsCursor.getInt(2);

            if (groupId != oldGroupId) {
                items.add(constantsCursor.getString(0) + "   " + constantsCursor.getString(1) +
                            constantsCursor.getString(2));
                oldGroupId = groupId;
            }

            constantsCursor.moveToNext();
        }

        recordsListView.setAdapter(new RecordsAdapter(this, Arrays.copyOf(items.toArray(), items.toArray().length, String[].class), this));

        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), SingleRecord.class);
                i.putExtra("id", 8);
                startActivity(i);

                /*new AlertDialog.Builder(parent.getContext())
                        .setTitle("Search")
                        .setMessage(String.valueOf(parent.getItemAtPosition(position)).toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        })
                        .show();*/
            }


        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_manager, menu);
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
        matMapDatabase.close();
    }

}
