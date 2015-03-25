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
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class TemporaryLocalization extends ActionBarActivity {
    private List<WifiInfo> wifiInfoList = new ArrayList<WifiInfo>(); //Vsetky udaje o wifi
    private WifiManager wifi;
    private WifiReceiver wifiReceiver;
    private static EditText roomName;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private TemporaryLocalization ref = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_localization);

        matMapDatabase = (new MatMapDatabase(this)).getWritableDatabase();
        wifiReceiver = new WifiReceiver();
        roomName = (EditText) findViewById(R.id.roomName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temporary_localization, menu);
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

    public void putNewRecord(View view) {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        //Test ci je wifi zapnuta
        if (!wifi.isWifiEnabled()) {
            //textView.setText("STE OFF");
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
            wifi.startScan();
        }

    }

    public void deleteRecords(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Really want to delete all records?").setPositiveButton("Yes", deleteRecordsClickListener)
                .setNegativeButton("No", deleteRecordsClickListener).show();

    }

    public void deleteRecord(View view) {
        boolean exists = false;
        String roomToDelete = roomName.getText().toString();

        constantsCursor = matMapDatabase.rawQuery("SELECT room_name FROM search_data WHERE room_name = '" + roomToDelete + "'", null);

        constantsCursor.moveToFirst();

        while(!constantsCursor.isAfterLast()) {
            exists = true;
            break;
        }

        if (exists) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Really want to delete selected record?").setPositiveButton("Yes", deleteRecordClickListener)
                    .setNegativeButton("No", deleteRecordClickListener).show();

        }
        else {

            new AlertDialog.Builder(this)
                    .setTitle("Room does not exists in database!")
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

        }

    }

    public void putRecordsIntoFile(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Really want to rewrite file?").setPositiveButton("Yes", saveChangesClickListener)
                .setNegativeButton("No", saveChangesClickListener).show();

    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void changeFile() {
        if (isExternalStorageWritable()) {

            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/AndroidFiles");
            directory.mkdirs();

            //Now create the file in the above directory and write the contents into it
            File file = new File(directory, "records.txt");
            try {
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
                Log.d("FILE", "CHYBa");
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

    private void deleteSelectedRecord() {
        String[] args = new String[]{roomName.getText().toString()};
        matMapDatabase.delete("search_data", "room_name=?", args);

        new AlertDialog.Builder(this)
                .setTitle("Successfully deleted!")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private String getRecordsData() {
        constantsCursor = matMapDatabase.rawQuery("SELECT room_name, BSSID, strength, device, SSID FROM search_data ORDER BY room_name", null);

        String answer = "";

        constantsCursor.moveToFirst();
        while(!constantsCursor.isAfterLast()) {
            for (int i = 0; i < constantsCursor.getColumnCount(); i++) {
                answer += constantsCursor.getString(i) + " ";
            }
            answer += "\n";
            constantsCursor.moveToNext();
        }

        constantsCursor.close();
        return answer;
    }

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

    DialogInterface.OnClickListener deleteRecordsClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    matMapDatabase.delete("search_data", null, null);

                    new AlertDialog.Builder(ref)
                            .setTitle("Successfully deleted!")
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Do nothing
                    break;
            }
        }
    };

    DialogInterface.OnClickListener deleteRecordClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    deleteSelectedRecord();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //Do nothing
                    break;
            }
        }
    };

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context c, Intent intent) {
            Log.d("as", "here");
            List<ScanResult> scanList = wifi.getScanResults();
            ContentValues values = new ContentValues();

            for (int i = 0; i < scanList.size(); i++) {
                ScanResult sr = scanList.get(i);

                values.put("room_name", TemporaryLocalization.roomName.getText().toString());
                values.put("BSSID", sr.BSSID);
                values.put("strength", sr.level);
                values.put("device", Build.DEVICE);
                values.put("SSID", sr.SSID);

                matMapDatabase.insert("search_data", "room_name", values);
            }

            unregisterReceiver(wifiReceiver);

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

    @Override
    public void onDestroy() {
        super.onDestroy();

        matMapDatabase.close();
    }
}
