package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import com.example.myfirstapp.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class Search extends ActionBarActivity {
	private List<WifiInfo> wifiInfoList = new ArrayList<WifiInfo>(); //Vsetky udaje o wifi
	private WifiManager wifi;
	private WifiReceiver wifiReceiver;
	private TextView textView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		wifiReceiver = new WifiReceiver();
		registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifi.startScan();
		
		textView = new TextView(this);
        textView.setTextSize(20);		
	}
	
	class WifiReceiver extends BroadcastReceiver {
			
		@Override
		public void onReceive(Context c, Intent intent) {
				List<ScanResult> scanList = wifi.getScanResults();
				List<WifiInfo> tempDates = new ArrayList<WifiInfo>();
				
				String testString = "";
				
				for (int i = 0; i < scanList.size(); i++) {
					ScanResult sr = scanList.get(i);
					WifiInfo wifiInfo = new WifiInfo(sr.SSID, sr.BSSID, sr.capabilities, String.valueOf(sr.level));
					tempDates.add(wifiInfo);
					
					//Toto neskor nahradi vypis polohy
					testString += i + ": " + sr.SSID + " " + sr.BSSID + " " + sr.capabilities + " ";
					testString += String.valueOf(sr.level) + "\n";
				}
				
				wifiInfoList = tempDates;				
				
				textView.setText(testString);
		        setContentView(textView);
				
			}
	}
	
	protected void onPause() {
        unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }
	
}