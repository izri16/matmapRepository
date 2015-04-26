package com.example.matmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class History extends ActionBarActivity {
    private ListView deleteListView;
    private ListView historyListView;
    private Button back;
    private Button remove;
    private TextView historyEmpty;
    private LinearLayout removeMenu;
    private List<String> items;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private ArrayAdapter<String> deleteAdapter;
    private ArrayAdapter<String> historyAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

        removeMenu = (LinearLayout) findViewById(R.id.removeMenu);
        deleteListView = (ListView)findViewById(R.id.deleteList);
        historyListView = (ListView)findViewById(R.id.historyList);
        back = (Button) findViewById(R.id.back);
        remove = (Button) findViewById(R.id.remove);
        historyEmpty = (TextView) findViewById(R.id.historyEmpty);
        items = new ArrayList<>();

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name FROM history ORDER BY timestamp DESC", null);

        constantsCursor.moveToFirst();
        while(!constantsCursor.isAfterLast()) {
            items.add(constantsCursor.getString(0));
            constantsCursor.moveToNext();
        }

        deleteAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);
        historyAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);

        deleteListView.setAdapter(deleteAdapter);
        historyListView.setAdapter(historyAdapter);

        deleteListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(parent.getContext())
                        .setTitle("Search")
                        .setMessage("Will start search in future")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        });

        checkIfEmpty();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {

            case R.id.action_delete:
                removeMode();
                return true;
            case R.id.action_delete_all:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
	}

    @Override
    public void onDestroy() {
        super.onDestroy();

        constantsCursor.close();
        matMapDatabase.close();
    }

    private void removeMode() {
        if (checkIfEmpty()) {
            removeMenu.setVisibility(View.GONE);
        }
        else {
            removeMenu.setVisibility(View.VISIBLE);
        }
        historyListView.setVisibility(View.GONE);
        deleteListView.setVisibility(View.VISIBLE);
    }

    private void historyMode() {
        historyListView.setVisibility(View.VISIBLE);
        deleteListView.setVisibility(View.GONE);
        removeMenu.setVisibility(View.GONE);
    }

    private void deleteAll() {
        int count = deleteListView.getAdapter().getCount();
        int offset = 0;

        for (int i = 0; i < count; i++) {
                String toDelete = deleteAdapter.getItem(i - offset);
                deleteAdapter.remove(toDelete);
                offset++;

                String[] args = new String[]{toDelete};
                matMapDatabase.delete("history", "room_name=?", args);

         }
        //Oznami adapteru ze boli odobrane polozky
        deleteAdapter.notifyDataSetChanged();
        historyAdapter.notifyDataSetChanged();

        checkIfEmpty();
    }

    public void remove(View view) {

        SparseBooleanArray checked = deleteListView.getCheckedItemPositions();
        List<Integer> toRemove = new ArrayList<>();
        int count = deleteListView.getAdapter().getCount();

        int offset = 0;
        for (int i = 0; i < count; i++) {
            if (checked.get(i)) {
                String toDelete = deleteAdapter.getItem(i - offset);
                deleteAdapter.remove(toDelete);
                offset++;

                String[] args = new String[]{toDelete};
                matMapDatabase.delete("history", "room_name=?", args);

            }
        }

        //Oznami adapteru ze boli odobrane polozky
        deleteAdapter.notifyDataSetChanged();
        historyAdapter.notifyDataSetChanged();
        //zrusi oznacenie ostatnych checkboxov
        deleteListView.clearChoices();

        checkIfEmpty();
    }

    public void back(View view) {

        //zrusi oznacenie ostatnych checkboxov
        deleteListView.clearChoices();

        historyMode();
    }

    private boolean checkIfEmpty() {
        if (historyListView.getAdapter().isEmpty()) {
            historyEmpty.setVisibility(View.VISIBLE);
            removeMenu.setVisibility(View.GONE);
            return true;
        }
        else {
            historyEmpty.setVisibility(View.GONE);
            return false;
        }
    }

}
