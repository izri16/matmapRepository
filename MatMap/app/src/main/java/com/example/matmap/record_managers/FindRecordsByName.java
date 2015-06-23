package com.example.matmap.record_managers;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.matmap.MatMapDatabase;
import com.example.matmap.R;

import java.util.ArrayList;
import java.util.List;

public class FindRecordsByName extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView inputField;
    private SQLiteDatabase matMapDatabase;
    private Cursor constantsCursor;
    private List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_records_by_name);

        init();
    }

    private void init() {
        items = new ArrayList<>();
        inputField = (AutoCompleteTextView)findViewById(R.id.findRecordsByName);
        inputField.setOnItemClickListener(this);

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT DISTINCT(room_name) FROM search_data " +
                "ORDER BY room_name", null);

        constantsCursor.moveToFirst();
        while(!constantsCursor.isAfterLast()) {
            items.add(constantsCursor.getString(0));
            constantsCursor.moveToNext();
        }

        inputField.setAdapter(new ArrayAdapter<>(this, R.layout.auto_complete_text_view_item,
                R.id.autoCompleteItem, items));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_records_by_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String record = (String) parent.getAdapter().getItem(position);
        Log.d("Record name", record);

        Intent intent = new Intent(this, RecordsManager.class);
        intent.putExtra("filter", true);
        intent.putExtra("filter_room", record);
        startActivity(intent);

        finish();
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
