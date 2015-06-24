package com.example.matmap;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.matmap.record_managers.RecordsManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Used as a handy locator for user
 * Can save records into both database and files and open RecordManager Activity
 */
public class RecordsCreator extends ActionBarActivity {
    private WifiManager wifi;
    private WifiReceiver wifiReceiver;
    private static EditText roomName;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private RecordsCreator ref = this;
    private ProgressBar spinner;
    private Button temporaryLocalisationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_creator);

        spinner = (ProgressBar) findViewById(R.id.spinningProgressBar);
        spinner.setVisibility(View.GONE);

        temporaryLocalisationButton = (Button) findViewById(R.id.temporaryLocalizationButton);
        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        roomName = (EditText) findViewById(R.id.roomName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_records_creator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            openRecordManager();
            return true;
        } else if (id == R.id.action_put_records_into_file) {
            putRecordsIntoFile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens RecordManager Activity
     */
    public void openRecordManager() {
        Intent intent = new Intent(this, RecordsManager.class);
        startActivity(intent);
    }

    /**
     * Puts new record into search_data database
     *
     * @param view pressed button
     */
    public void putNewRecord(View view) {
        wifiReceiver = new WifiReceiver();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        //Test if wifi is on
        if (!wifi.isWifiEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("No connection!")
                    .setIcon(R.drawable.no_wifi)
                    .setMessage("Please turn your wifi on")
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
        else if (roomName.getText().toString().equals("")) {
            if (wifiReceiver != null) {
                unregisterReceiver(wifiReceiver);
                wifiReceiver = null;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Room name is empty!")
                    .setMessage("Please type correct name")
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        else {
            temporaryLocalisationButton.setEnabled(false);
            activateSpinner();
            wifi.startScan();
        }

    }

    /**
     * Asks if really want to put records into file and then performs action
     */
    public void putRecordsIntoFile() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Really want to rewrite file?")
                .setPositiveButton("Yes", saveChangesClickListener)
                .setNegativeButton("No", saveChangesClickListener).show();

    }

    /**
     *  Checks if external storage is available for read and write
     */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Rewrites content on text file which stores records data
     */
    private void changeFile() {
        if (isExternalStorageWritable()) {

            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);

            File file = new File(path, "matmap_records.txt");
            try {
                path.mkdirs();

                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                osw.write(getRecordsData());
                osw.flush();
                osw.close();

                new AlertDialog.Builder(this)
                        .setTitle("Successfully added!")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

            } catch (IOException e) {
                Log.d("ERROR", e.toString());
                e.printStackTrace();
            }

        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Not enough space!")
                    .setIcon(R.drawable.no_wifi)
                    .setMessage("Please delete some files")
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    /**
     * Called to get data for text file
     *
     * @return Data which will be put into file
     */
    private String getRecordsData() {
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name, group_id, timestamp, BSSID, " +
                                                  "strength, device, SSID FROM search_data " +
                                                  "ORDER BY room_name", null);

        String answer = "";

        constantsCursor.moveToFirst();
        while(!constantsCursor.isAfterLast()) {
            for (int i = 0; i < constantsCursor.getColumnCount(); i++) {
                answer += constantsCursor.getString(i) + "\t";
            }
            answer += "\n";
            constantsCursor.moveToNext();
        }

        constantsCursor.close();
        return answer;
    }

    /**
     * Activates spinner to signalize that record is being added
     */
    private void activateSpinner() {
        temporaryLocalisationButton.setText("Scanning, please wait");
        this.spinner.setVisibility(View.VISIBLE);

    }

    /**
     * Deactivates spinner when adding record is finished
     */
    private void deactivateSpinner() {
        temporaryLocalisationButton.setText("Add new record");
        this.spinner.setVisibility(View.GONE);
    }

    /**
     * Dialog for save changed to file action
     */
    DialogInterface.OnClickListener saveChangesClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    changeFile();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Do nothing
                    break;
            }
        }
    };

    /**
     * Used to get wifi signal
     */
    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context c, Intent intent) {

            Log.d("Action", "Scanning");
            List<ScanResult> scanList = wifi.getScanResults();
            ContentValues values = new ContentValues();

            int groupId = 0;
            for (int i = 0; i < scanList.size(); i++) {
                ScanResult sr = scanList.get(i);

                if (i == 0) {
                    groupId = incrementGroupIdRecord();
                }

                Log.d("GROUP ID", String.valueOf(groupId));

                values.put("room_name", RecordsCreator.roomName.getText().toString());
                values.put("group_id", groupId);
                values.put("timestamp", MainActivity.getDateTime());
                values.put("BSSID", sr.BSSID);
                values.put("strength", sr.level);
                values.put("device", Build.DEVICE);
                values.put("SSID", sr.SSID);

                matMapDatabase.insert("search_data", "room_name", values);
            }

            unregisterReceiver(wifiReceiver);
            wifiReceiver = null;

            deactivateSpinner();
            temporaryLocalisationButton.setEnabled(true);

            new AlertDialog.Builder(ref)
                    .setTitle("Added!")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    /**
     * Increment group_id in record_group_id database
     * This method and database should not exist in future
     *
     * @return old group_id
     */
    private int incrementGroupIdRecord() {
        constantsCursor = matMapDatabase.rawQuery("SELECT record_group_id FROM record_group", null);

        int groupIdRecord = 0;

        constantsCursor.moveToFirst();
        while(!constantsCursor.isAfterLast()) {
            groupIdRecord = constantsCursor.getInt(0);
            constantsCursor.moveToNext();
        }
        constantsCursor.close();


        String args[] = new String[]{String.valueOf(groupIdRecord)};
        ContentValues values = new ContentValues();
        values.put("record_group_id", groupIdRecord + 1);
        matMapDatabase.update("record_group", values, "record_group_id=?", args);

        return groupIdRecord;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        matMapDatabase.close();
        if (wifiReceiver != null) {
            unregisterReceiver(wifiReceiver);
            wifiReceiver = null;
        }
    }
}
