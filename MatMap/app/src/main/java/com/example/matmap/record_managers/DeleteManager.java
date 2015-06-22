package com.example.matmap.record_managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.matmap.MatMapDatabase;
import com.example.matmap.R;
import com.example.matmap.adapters.DeleteAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteManager extends Activity {
    private ListView recordsListView;
    private List<JSONObject> items;
    private SQLiteDatabase matMapDatabase;
    private Cursor constantsCursor;
    private DeleteAdapter adapter;
    private boolean switchAll = true;
    private Button deleteButton;
    private boolean calledFromHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_manager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.calledFromHistory = extras.getBoolean("called_from_history");
        }
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_manager, menu);
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

    /**
     * Does all necessary work to initialize all important variables
     */
    private void init() {
        recordsListView = (ListView)findViewById(R.id.recordDeleteList);
        items = new ArrayList<>();
        deleteButton = (Button) findViewById(R.id.deleteRecordsDeleteButton);
        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();

        // determine from which Activity was this Activity called (created)
        if (calledFromHistory) {
            initDeleteHistory();
        } else {
            initDeleteRecords();
        }

        if (items.isEmpty()) {
            finish();
        }

        this.adapter = new DeleteAdapter(this, Arrays.copyOf(items.toArray(),
                items.toArray().length, JSONObject[].class), this, this.calledFromHistory);
        this.recordsListView.setAdapter(this.adapter);

        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.setRecentSwitch(false);
                adapter.reCheck(position);
                adapter.notifyDataSetChanged();

                deleteButton.setEnabled(adapter.checkIfSomeSwitched());
            }

        });
    }

    /**
     * Invoked when top panel with 'Choose all' text is being clicked
     *
     * @param view view of clicked element
     */
    public void chooseAll(View view) {

        //top panel with 'choose all' text was clicked
        if (!view.equals(view.findViewById(R.id.boxToCheckAll))) {
            CheckBox c = (CheckBox) view.findViewById(R.id.boxToCheckAll);
            c.setChecked(adapter.checkIfSomeNotSwitched());
        }
        //checkbox in 'choose all' panel was clicked
        else {
            CheckBox c = (CheckBox) view;
            c.setChecked(adapter.checkIfSomeNotSwitched());
        }

        int itemsCount = this.adapter.getCount();

        if (adapter.checkIfSomeNotSwitched()) {
            adapter.setSwitchAll(true);
            this.switchAll = true;
            deleteButton.setEnabled(true);
        }
        else {
            adapter.setSwitchAll(false);
            this.switchAll = false;
            deleteButton.setEnabled(false);
        }

        adapter.setRecentSwitch(true);
        adapter.notifyDataSetChanged();

        for (int i = 0; i < itemsCount; i++) {
            adapter.setValueAtPosition(i, switchAll);
            adapter.getView(i, null, null);
        }

    }

    /**
     * Invoked when checkbox in the body of the delete list is clicked
     *
     * @param view currently clicked checkbox
     */
    public void checkBoxClicked(View view) {
        Integer l = (Integer) view.getTag();

        adapter.setRecentSwitch(false);
        adapter.reCheck(l);
        adapter.notifyDataSetChanged();
        deleteButton.setEnabled(adapter.checkIfSomeSwitched());
    }

    /**
     * Quits current activity
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * Invoked when delete is clicked
     *
     * @param view
     */
    public void deleteRecords(View view) {
        if (this.calledFromHistory) { // when deleting from history alert dialog is not needed
            delete();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Really want to delete selected records?")
                    .setPositiveButton("Yes", deleteRecordsClickListener)
                    .setNegativeButton("No", deleteRecordsClickListener).show();
        }
    }

    /**
     * Listener for alert dialog when delete is clicked
     */
    DialogInterface.OnClickListener deleteRecordsClickListener =
            new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    delete();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Do nothing
                    break;
            }
        }
    };

    /**
     * Used in init function when Activity is called from RecordManager Activity
     */
    private void initDeleteRecords() {
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name, timestamp, group_id " +
                "                           FROM search_data ORDER BY timestamp DESC", null);

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
    }

    /**
     * Used in init function when Activity is called from History Activity
     */
    private void initDeleteHistory() {
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name, timestamp, _id" +
                "                           FROM history ORDER BY timestamp DESC", null);

        constantsCursor.moveToFirst();

        while(!constantsCursor.isAfterLast()) {
            JSONObject json = new JSONObject();
            try {
                json.put("room_name", constantsCursor.getString(0));
                json.put("date", constantsCursor.getString(1));
                json.put("id", constantsCursor.getString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            items.add(json);
            constantsCursor.moveToNext();
        }
    }

    /**
     * Deletes selected records from SQLite database
     */
    private void delete() {

        if (adapter.checkIfSomeNotSwitched()) {

            List<String> itemsToDelete = new ArrayList<>();
            StringBuilder whereClause = new StringBuilder();
            deleteButton.setEnabled(false);

            if (this.calledFromHistory) {
                whereClause.append("_id in (");

                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getCheckBoxValue(i)) {
                        try {
                            itemsToDelete.add(adapter.getItem(i).getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        whereClause.append("?,");
                    }
                }

                whereClause.delete(whereClause.length() - 1, whereClause.length());
                whereClause.append(")");

                Log.d("WHERE CLAUSE", whereClause.toString());

                String[] pom = Arrays.copyOf(itemsToDelete.toArray(), itemsToDelete.toArray().length,
                        String[].class);


                this.matMapDatabase.delete("history", whereClause.toString(), pom);
            } else {

                whereClause.append("group_id in (");

                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getCheckBoxValue(i)) {
                        try {
                            itemsToDelete.add(adapter.getItem(i).getString("group_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        whereClause.append("?,");
                    }
                }

                whereClause.delete(whereClause.length() - 1, whereClause.length());
                whereClause.append(")");

                String[] pom = Arrays.copyOf(itemsToDelete.toArray(), itemsToDelete.toArray().length,
                        String[].class);


                this.matMapDatabase.delete("search_data", whereClause.toString(), pom);
            }
        }
        else {
            if (this.calledFromHistory) {
                this.matMapDatabase.delete("history", null, null);
            }
            else {
                this.matMapDatabase.delete("search_data", null, null);
            }
        }

        adapter.notifyDataSetChanged();
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
