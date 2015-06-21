package com.example.matmap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import java.util.Date;
import java.util.List;

public class NewLocation extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private AutoCompleteTextView inputField;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private List<String> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //kvoli buggu triedy autocomplete lebo inak nevidno text
        //setTheme(android.R.style.Theme);
        setContentView(R.layout.activity_new_location);

        items = new ArrayList<>();
        inputField = (AutoCompleteTextView)findViewById(R.id.newLocation);
        inputField.setOnItemClickListener(this);

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name FROM rooms ORDER BY room_name", null);

        constantsCursor.moveToFirst();
        while(!constantsCursor.isAfterLast()) {
            items.add(constantsCursor.getString(0));
            constantsCursor.moveToNext();
        }

        inputField.setAdapter(new ArrayAdapter<String>(this, R.layout.auto_complete_text_view_item, R.id.autoCompleteItem,
                items));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        constantsCursor.close();
        matMapDatabase.close();
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
        String chosenRoom = (String) parent.getAdapter().getItem(position);
        String title = "Added into history";
        String message = "Will find shortest path in the future";

        if (!addRoomIntoHistory(chosenRoom)) {
            title = "Moved to first place in history";
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
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
            values.put("timestamp", new Date().getTime());
            matMapDatabase.insert("history", "room_name", values);
            answer = true;
        }
        else {
            String[] args = new String[]{room};
            values.put("timestamp", new Date().getTime());
            matMapDatabase.update("history", values, "room_name=?", args);
        }

        return answer;
    }

    private boolean alreadyExists(String room) {
        boolean answer = false;
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name FROM history WHERE room_name = '" + room + "'", null);

        constantsCursor.moveToFirst();

        while(!constantsCursor.isAfterLast()) {
            answer = true;
            break;
        }

        return answer;
    }
}
