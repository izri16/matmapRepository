package com.example.matmap.record_managers;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.matmap.MatMapDatabase;
import com.example.matmap.R;

public class RenameRecord extends ActionBarActivity {
    private EditText editText;
    private SQLiteDatabase matMapDatabase = null;
    private String oldName = "";
    private String groupId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_record);

        this.editText = (EditText) findViewById(R.id.renameRecordTextField);
        this.matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.oldName = extras.getString("roomName");
            this.groupId = extras.getString("groupId");
        }
        editText.setText(oldName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rename_record, menu);
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
     * Invoked when rename is clicked
     *
     * @param view
     */
    public void renameRecord(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apply to all records with name " + editText.getText().toString() +"?")
                .setPositiveButton("Yes", renameRecordsClickListener)
                .setNegativeButton("No", renameRecordsClickListener).show();
    }

    /**
     * Listener for alert dialog when rename is clicked then finish activity
     */
    DialogInterface.OnClickListener renameRecordsClickListener =
            new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            renameAllRecords();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            renameSingleRecord();
                            break;
                    }
                    finish();
                }
            };

    /**
     * Renames just record with specified recordId
     */
    public void renameSingleRecord() {
        ContentValues values = new ContentValues();

        values.put("room_name", this.editText.getText().toString());
        String[] args = new String[]{this.groupId};

        this.matMapDatabase.update("search_data", values, "group_id = ?", args);
        values.clear();
    }

    /**
     * Renames all records with selected name
     */
    public void renameAllRecords() {
        ContentValues values = new ContentValues();

        values.put("room_name", this.editText.getText().toString());
        String[] args = new String[]{this.oldName};

        this.matMapDatabase.update("search_data", values, "room_name = ?", args);
        values.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
    }
}
