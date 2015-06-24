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
import java.util.List;

/**
 * Display users location or if unknown displays alternative message
 */
public class Search extends ActionBarActivity {
	private List<WifiInfo> wifiInfoList = new ArrayList<WifiInfo>(); //All info about wifi
	private WifiManager wifi;
	private WifiReceiver wifiReceiver;
	private TextView textView;
    private ImageView unknownLocationImage;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private Locator locator;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);


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


            Log.d("ROOM_NAME", roomName);
            Log.d("BSSID", bssid);
            Log.d("STRENGTH", String.valueOf(strength));

            constantsCursor.moveToNext();
        }
        locator.addLocation(lastLocation);

        textView = (TextView)findViewById(R.id.locationMessage);
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        unknownLocationImage = (ImageView)findViewById(R.id.unknownLocation);
        unknownLocationImage.setVisibility(View.GONE);
		
		wifiReceiver = new WifiReceiver();
		registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

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
        wifi.startScan();
	}

    /**
     * Used to get wifi signal
     */
	class WifiReceiver extends BroadcastReceiver {
			
		@Override
		public void onReceive(Context c, Intent intent) {
                Log.d("Action", "Scanning");
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
                } else {
                    unknownLocationImage.setVisibility(View.GONE);
                }
                textView.setText("You are at " + place);

			}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_history) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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