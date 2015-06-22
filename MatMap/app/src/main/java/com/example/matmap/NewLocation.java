package com.example.matmap;

import android.content.ContentValues;
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
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class NewLocation extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView inputField;
    private SQLiteDatabase matMapDatabase;
    private Cursor constantsCursor;
    private List<String> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //kvoli buggu triedy autocomplete lebo inak nevidno text
        //setTheme(android.R.style.Theme);
        setContentView(R.layout.activity_new_location);

        init();
    }

    private void init() {
        items = new ArrayList<>();
        inputField = (AutoCompleteTextView)findViewById(R.id.newLocation);
        inputField.setOnItemClickListener(this);

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT DISTINCT(room_name) FROM search_data " +
                "ORDER BY room_name", null);

        constantsCursor.moveToFirst();
        while(!constantsCursor.isAfterLast()) {
            items.add(constantsCursor.getString(0));
            constantsCursor.moveToNext();
        }

        inputField.setAdapter(new ArrayAdapter<String>(this, R.layout.auto_complete_text_view_item,
                R.id.autoCompleteItem, items));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_location, menu);
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
        String destination = (String) parent.getAdapter().getItem(position);
        addRoomIntoHistory(destination);

        Intent intent = new Intent(this, PathViewer.class);
        intent.putExtra("destination", destination);
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

        constantsCursor.close();
        matMapDatabase.close();
    }

    public void openHelp(View view) {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }

    private boolean addRoomIntoHistory(String room) {
        ContentValues values = new ContentValues();
        boolean answer = false;

        if (!alreadyExists(room)) {
            values.put("room_name", room);
            values.put("timestamp", MainActivity.getDateTime());
            matMapDatabase.insert("history", "room_name", values);
            answer = true;
        }
        else {
            String[] args = new String[]{room};
            values.put("timestamp", MainActivity.getDateTime());
            matMapDatabase.update("history", values, "room_name=?", args);
        }

        return answer;
    }

    private boolean alreadyExists(String room) {
        boolean answer = false;
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name FROM history " +
                "WHERE room_name = '" + room + "'", null);

        constantsCursor.moveToFirst();

        while(!constantsCursor.isAfterLast()) {
            answer = true;
            break;
        }

        return answer;
    }
}
