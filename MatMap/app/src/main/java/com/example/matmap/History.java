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
import com.example.matmap.adapters.HistoryAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class History extends ActionBarActivity {
    private ListView recordsListView;
    private List<JSONObject> items;
    private SQLiteDatabase matMapDatabase;
    private Cursor constantsCursor;
    private TextView noRecords;
    private boolean disableMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

        init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
        MenuItem del = menu.findItem(R.id.action_delete);

        if (this.disableMenu) {
            del.setVisible(false);
        }
        else {
            del.setVisible(true);
        }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_delete:
                openDeleteManager();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        constantsCursor.close();
        matMapDatabase.close();
    }

    /**
     * does all necessary work to initialize all important properties
     */
    private void init() {
        recordsListView = (ListView)findViewById(R.id.recordList);
        items = new ArrayList<>();

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name, timestamp FROM history " +
                "ORDER BY timestamp DESC", null);

        constantsCursor.moveToFirst();

        while(!constantsCursor.isAfterLast()) {
            JSONObject json = new JSONObject();
            try {
                json.put("room_name", constantsCursor.getString(0));
                json.put("date", constantsCursor.getString(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            items.add(json);
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

        recordsListView.setAdapter(new HistoryAdapter(this, Arrays.copyOf(items.toArray(),
                items.toArray().length, JSONObject[].class), this));


        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String destination = String.valueOf(view.getTag());

                /*Intent i = new Intent(getApplicationContext(), RecordGroup.class);
                i.putExtra("groupId", groupId);

                startActivity(i);*/
                Log.d("Destination", destination);
            }
        });
    }

    /**
     * Opens DeleteManager Activity to manipulate delete actions
     */
    private void openDeleteManager() {
        Intent intent = new Intent(this, DeleteManager.class);
        intent.putExtra("called_from_history", true);
        startActivity(intent);
    }
}
