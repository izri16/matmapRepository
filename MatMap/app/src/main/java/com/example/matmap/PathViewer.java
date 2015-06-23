package com.example.matmap;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class PathViewer extends ActionBarActivity {
    private List<WifiInfo> wifiInfoList = new ArrayList<WifiInfo>(); //Vsetky udaje o wifi
    private WifiManager wifi;
    private WifiReceiver wifiReceiver;
    private ImageView unknownLocationImage;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private Locator locator;
    private String destination;
    private String currentPosition;
    private TextView headerMessage;
    private TextView bodyMessage;
    private TextView pointsToFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_viewer);

        Bundle extras = getIntent().getExtras();
        this.destination = extras.getString("destination");
        Log.d("Destination", destination);

        this.headerMessage = (TextView) findViewById(R.id.pathViewHeadString);
        this.bodyMessage = (TextView) findViewById(R.id.pathViewMessageString);
        this.pointsToFollow = (TextView) findViewById(R.id.pathFollowPoints);
        this.unknownLocationImage = (ImageView) findViewById(R.id.unknownLocation);
        init();

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        checkIfWifiOn();
        wifi.startScan();
    }

    private void init() {
        locator = new Locator();
        matMapDatabase = (new MatMapDatabase(this)).getReadableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT d.room_name, d.bssid, avg(d.strength) " +
                "FROM search_data d " +
                "GROUP BY d.room_name, d.bssid", null);
        constantsCursor.moveToFirst();
        String lastRoomName = "";
        Location lastLocation = null;
        while(!constantsCursor.isAfterLast()) {
            String roomName = constantsCursor.getString(0);
            String bssid = constantsCursor.getString(1);
            Double strength = Locator.dBmToQuality(constantsCursor.getDouble(2));

            if (lastLocation == null) {
                lastRoomName = roomName;
                lastLocation = new Location().setName(roomName);
            } else if (!lastRoomName.equals(roomName)) {
                // after we finished adding stuff for one location (room) we can add it
                // to the Locator
                locator.addLocation(lastLocation);

                lastRoomName = roomName;
                lastLocation = new Location().setName(roomName);
            }

            lastLocation.addAP(bssid, strength);

            //Log.d("ROOM_NAME", roomName);
            //Log.d("BSSID", bssid);
            //Log.d("STRENGTH", String.valueOf(strength));

            constantsCursor.moveToNext();
        }
        locator.addLocation(lastLocation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_path_viewer, menu);
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

    private void checkIfWifiOn() {
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
    }

    public void viewPath() {
        Map<String, String> previous = new HashMap<>();
        Map<String, Boolean> visited = new HashMap<>();
        String current = "";

        Queue<String> queue = new LinkedList<>();
        List<String> directions = new ArrayList();
        queue.add(this.currentPosition);

        while(!queue.isEmpty()) {
            current = queue.remove();
            if (current.equals(this.destination)) {
                break;
            } else {
                String query = "SELECT neighbor FROM neighbors WHERE room_name = '" + current + "'";
                constantsCursor = matMapDatabase.rawQuery(query, null);
                constantsCursor.moveToFirst();

                while (!constantsCursor.isAfterLast()) {
                    Log.d("ZACYKLIL", "SA");
                    String neighbor = constantsCursor.getString(0);

                    if (!visited.containsKey(neighbor)) {
                        visited.put(neighbor, true);
                        queue.add(neighbor);
                        previous.put(neighbor, current);
                    }
                    constantsCursor.moveToNext();
                }
                constantsCursor.close();
            }
        }
        if (!current.equals(this.destination)) {
            this.headerMessage.setText("No way found. Maybe you have forgotten to create " +
                    "some neighbor");
            this.bodyMessage.setText("");
            this.pointsToFollow.setVisibility(View.GONE);
        } else {
            String s = this.destination;
            while(!s.equals(this.currentPosition)) {
                directions.add(s);
                s = previous.get(s);
                Log.d("CYKLUS", "CYKLUS");
            }
            directions.add(s);
            Collections.reverse(directions);
            this.headerMessage.setText("You are at: " + this.currentPosition);
            this.pointsToFollow.setVisibility(View.VISIBLE);
            this.bodyMessage.setText(toMyFormat(directions));
        }

    }

    private String toMyFormat(List<String> l) {
        String answer = "";

        for (int i = 0; i < l.size(); i++) {
            answer += (i + 1) + " " + l.get(i);
            if (i + 1 < l.size()) {
                answer += "\n";
            }
        }

        return answer;
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context c, Intent intent) {
            //Log.d("as", "here");
            List<ScanResult> scanList = wifi.getScanResults();
            List<WifiInfo> tempDates = new ArrayList<WifiInfo>();

            for (int i = 0; i < scanList.size(); i++) {
                ScanResult sr = scanList.get(i);
                WifiInfo wifiInfo = new WifiInfo(sr.SSID, sr.BSSID, sr.capabilities, String.valueOf(sr.level));
                tempDates.add(wifiInfo);
            }

            wifiInfoList = tempDates;

            String place = locator.localize(tempDates);
            if (place.equals("")) {
                place = "a location we do not know about yet";
                unknownLocationImage.setVisibility(View.VISIBLE);
                headerMessage.setText("You are at " + place);
                pointsToFollow.setVisibility(View.GONE);
                bodyMessage.setText("");
            } else {
                unknownLocationImage.setVisibility(View.GONE);
                currentPosition = place;
                viewPath();
            }


        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (matMapDatabase != null) {
            matMapDatabase.close();
        }
    }
}
