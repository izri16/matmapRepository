package com.example.matmap.record_managers;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.matmap.MatMapDatabase;
import com.example.matmap.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Responsible for creating neighbor which are just as adjacency matrix in PathViewer Activity
 */
public class NeighborCreator extends ActionBarActivity {
    private EditText neighbors;
    private String roomName;
    private SQLiteDatabase matMapDatabase;
    private Cursor constantsCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbor_creator);

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        Bundle extras = getIntent().getExtras();
        this.roomName = extras.getString("roomName");

        this.neighbors = (EditText) findViewById(R.id.neighbours);
        TextView hint = (TextView) findViewById(R.id.neighboursHint);
        hint.setText("Every neighbour of the current room has to be on a new line!");

        neighbors.setText(getNeighbors());
        getSupportActionBar().setTitle(roomName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_neighbor_creator, menu);
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

    /**
     * Get all neighbors for current room
     *
     * @return neighbors for current room
     */
    private String getNeighbors() {
        String query = "SELECT neighbor FROM neighbors WHERE room_name = '" + roomName + "'";
        constantsCursor = matMapDatabase.rawQuery(query, null);
        StringBuilder builder = new StringBuilder();

        constantsCursor.moveToFirst();

        while(!constantsCursor.isAfterLast()) {
            builder.append(constantsCursor.getString(0) + "\n");
            constantsCursor.moveToNext();
        }

        if (builder.length() >= 1) {
            builder.delete(builder.length() - 1, builder.length());
        }

        constantsCursor.close();

        return builder.toString();
    }

    /**
     * Save new neighbors for current room
     *
     * @param view pressed button
     */
    public void confirmChanges(View view) {
        String whereClause = "room_name = ?";
        String[] delValues = {roomName};
        Set<String> usedNames = new HashSet<>();
        this.matMapDatabase.delete("neighbors", whereClause, delValues);
        boolean success = true;

        ContentValues values = new ContentValues();
        String[] splitNeighbors = this.neighbors.getText().toString().split("\n");

        // checks if all neighbors already exists in search_data table to avoid missing data
        for (int i = 0; i < splitNeighbors.length; i++) {
            splitNeighbors[i] = splitNeighbors[i].trim();

            if (!splitNeighbors[i].equals("")) {
                if (!existInSearchData(splitNeighbors[i]))  {
                    showNegativeMessage();
                    success = false;
                    break;
                }
                else if (roomName.equals(splitNeighbors[i])) {
                    showNegativeMessageTwo();
                    success = false;
                    break;
                }
            }
        }

        if (success) {

            for (int i = 0; i < splitNeighbors.length; i++) {

                if (!splitNeighbors[i].equals("") && !usedNames.contains(splitNeighbors[i])) {
                    usedNames.add(splitNeighbors[i]);
                    values.put("room_name", roomName);
                    values.put("neighbor", splitNeighbors[i]);
                    matMapDatabase.insert("neighbors", "roomName", values);
                }
            }

            values.clear();
            showPositiveMessage();
        }
    }

    /**
     * Checks if neighbor exist in search_data database
     *
     * @param room
     * @return true if exists else if not
     */
    private boolean existInSearchData(String room) {
        String query = "SELECT room_name FROM search_data WHERE room_name = '" + room + "'";

        constantsCursor = matMapDatabase.rawQuery(query, null);
        constantsCursor.moveToFirst();
        while (!constantsCursor.isAfterLast()) {
            return true;
        }

        return false;
    }

    /**
     * Shows success message
     */
    private void showPositiveMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Successfully changed")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    /**
     * Shows fail message when neighbor do not exist in search_data
     */
    private void showNegativeMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Wrong input")
                .setMessage("Some neighbors do not already exist. Please create " +
                        "records with same names first.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * Shows fail message when neighbor has same name as current room
     */
    private void showNegativeMessageTwo() {
        new AlertDialog.Builder(this)
                .setTitle("Wrong input")
                .setMessage("Neighbor can not have same name as current room.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
    }
}
