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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Search extends ActionBarActivity {
	private List<WifiInfo> wifiInfoList = new ArrayList<WifiInfo>(); //Vsetky udaje o wifi
	private WifiManager wifi;
	private WifiReceiver wifiReceiver;
	private TextView textView;
    private ImageView unknownLocationImage;
    private SQLiteDatabase matMapDatabase = null;
    private Cursor constantsCursor = null;
    private Locator locator;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);


        locator = new Locator();
        matMapDatabase = (new MatMapDatabase(this)).getReadableDatabase();
        constantsCursor = matMapDatabase.rawQuery("SELECT d.room_name, d.bssid, avg(d.strength) " +
                                                  "FROM search_data d GROUP BY d.room_name", null);
        constantsCursor.moveToFirst();
        String lastRoomName = "";
        Location lastLocation = null;
        while(!constantsCursor.isAfterLast()) {
            String roomName = constantsCursor.getString(0);
            String bssid = constantsCursor.getString(1);
            Double strength = constantsCursor.getDouble(2);

            if (lastLocation == null) {
                lastRoomName = roomName;
                lastLocation = new Location();
            } else if (!lastRoomName.equals(roomName)) {
                // after we finished adding stuff for one location (room) we can add it
                // to the Locator
                locator.addLocation(lastLocation);

                lastRoomName = roomName;
                lastLocation = new Location();
            }

            lastLocation.addAP(bssid, strength);


            Log.d("ROOM_NAME", constantsCursor.getString(0));
            Log.d("BSSID", constantsCursor.getString(1));
            Log.d("STRENGTH", String.valueOf(constantsCursor.getDouble(2)));

            constantsCursor.moveToNext();
        }

        textView = (TextView)findViewById(R.id.locationMessage);
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        unknownLocationImage = (ImageView)findViewById(R.id.unknownLocation);
        unknownLocationImage.setVisibility(View.GONE);
		
		wifiReceiver = new WifiReceiver();
		registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


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

        wifi.startScan();
		

	}
	
	class WifiReceiver extends BroadcastReceiver {
			
		@Override
		public void onReceive(Context c, Intent intent) {
                Log.d("as", "here");
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
                }
                textView.setText("You are at " + place);

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