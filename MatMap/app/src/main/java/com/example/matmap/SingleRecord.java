package com.example.matmap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SingleRecord extends ActionBarActivity {
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private TextView recordId = null;
    private TextView groupId = null;
    private TextView roomName = null;
    private TextView bssid = null;
    private TextView ssid = null;
    private TextView strength = null;
    private TextView device = null;
    private TextView timestamp = null;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.id = Integer.valueOf(extras.getString("recordId"));
        }

        getTextFields();
        fillTextFields();

        Log.d("IDEDEDE", String.valueOf(id));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_record, menu);
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

    private void fillTextFields() {
        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT _id, group_id, SSID, STRENGTH, BSSID, timestamp, room_name, device FROM search_data WHERE _id = '" + id + "'", null);

        constantsCursor.moveToFirst();

        while(!constantsCursor.isAfterLast()) {

            this.recordId.setText(constantsCursor.getString(0));
            this.groupId.setText(constantsCursor.getString(1));
            this.ssid.setText(constantsCursor.getString(2));
            this.strength.setText(constantsCursor.getString(3));
            this.bssid.setText(constantsCursor.getString(4));
            this.timestamp.setText(constantsCursor.getString(5));
            this.roomName.setText(constantsCursor.getString(6));
            this.device.setText(constantsCursor.getString(7));

            constantsCursor.moveToNext();
        }
    }

    private void getTextFields() {
        this.bssid = (TextView)findViewById(R.id.singleRecordBSSID);
        this.ssid = (TextView)findViewById(R.id.singleRecordSSID);
        this.device = (TextView)findViewById(R.id.singleRecordDevice);
        this.recordId = (TextView)findViewById(R.id.singleRecordID);
        this.strength = (TextView)findViewById(R.id.singleRecordStrength);
        this.timestamp = (TextView)findViewById(R.id.singleRecordTimestamp);
        this.roomName = (TextView)findViewById(R.id.singleRecordRoomName);
        this.groupId = (TextView)findViewById(R.id.singleRecordGroupID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
    }
}
