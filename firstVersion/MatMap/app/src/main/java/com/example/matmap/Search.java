package com.example.matmap;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Search extends ActionBarActivity {
	private List<WifiInfo> wifiInfoList = new ArrayList<WifiInfo>(); //Vsetky udaje o wifi
	private WifiManager wifi;
	private WifiReceiver wifiReceiver;
	private TextView textView;
    private ImageView unknownLocationImage;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

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
				
				String testString = "";
				
				for (int i = 0; i < scanList.size(); i++) {
					ScanResult sr = scanList.get(i);
					WifiInfo wifiInfo = new WifiInfo(sr.SSID, sr.BSSID, sr.capabilities, String.valueOf(sr.level));
					tempDates.add(wifiInfo);

                    testString += i + ": " + sr.SSID + " " + sr.BSSID + " " + sr.capabilities + " ";
                    testString += String.valueOf(sr.level) + "\n";
				}
				
				wifiInfoList = tempDates;

                Locator locator = new Locator();

                locator.addLocation()
                        .setName("A")
                        .addAP("0E:27:22:F3:23:85", 42.2857142857)
                        .addAP("C2:9F:DB:2B:AC:F0", 40.2857142857)
                        .addAP("00:19:07:34:63:91", 67.7777777778)
                        .addAP("00:19:07:34:63:90", 69.8571428571)
                        .addAP("00:19:07:34:63:92", 71.1428571429)
                        .addAP("00:18:90:30:2E:DB", 25.7142857143)
                        .addAP("0A:27:22:F3:19:04", 35.7142857143)
                        .addAP("0E:27:22:57:36:B3", 27.8571428571)
                        .addAP("90:E6:BA:BE:B6:3F", 28.5714285714)
                        .addAP("CC:5D:4E:46:15:E5", 30.0)
                        .addAP("0A:27:22:F3:23:85", 44.8571428571)
                        .addAP("0E:27:22:AD:60:32", 30.0)
                        .addAP("0A:27:22:AD:60:32", 32.8571428571)
                        .addAP("0E:27:22:F3:1D:E8", 28.5714285714)
                        .addAP("C6:9F:DB:2B:AC:F0", 38.0952380952)
                        .addAP("00:19:07:34:C9:81", 42.1428571429)
                        .addAP("00:19:07:34:C9:80", 40.0)
                        .addAP("CA:9F:DB:2B:AC:F0", 40.1428571429)
                        .addAP("00:19:07:34:C9:82", 38.5714285714)
                        .addAP("0A:E6:0F:86:88:DD", 37.380952381)
                        .addAP("06:27:22:AD:60:32", 35.7142857143)
                        .addAP("06:27:22:F3:23:85", 45.4285714286)
                        .addAP("0E:27:22:F3:21:29", 36.4285714286)
                        .addAP("00:4F:62:1D:EB:41", 25.7142857143)
                        .addAP("06:27:22:F3:1D:E8", 48.5714285714)
                        .addAP("64:66:B3:F7:97:78", 28.5714285714)
                        .addAP("06:27:22:F3:21:29", 33.5714285714)
                        .addAP("60:A4:4C:C7:4A:E8", 30.0);
                locator.addLocation()
                        .setName("schody-inf")
                        .addAP("0A:27:22:F3:18:58", 64.693877551)
                        .addAP("06:27:22:F3:18:58", 66.7346938776)
                        .addAP("00:19:07:34:C9:81", 31.2244897959)
                        .addAP("00:19:07:34:C9:80", 33.3333333333)
                        .addAP("0E:27:22:F3:18:58", 62.6530612245)
                        .addAP("00:0D:54:9E:06:5B", 47.9591836735)
                        .addAP("0A:27:22:55:F2:A4", 27.1428571429)
                        .addAP("00:19:07:34:C9:82", 41.4285714286)
                        .addAP("12:27:22:F3:18:58", 63.2653061224)
                        .addAP("00:1F:C6:82:0E:E7", 42.2448979592)
                        .addAP("F8:1A:67:AC:DD:86", 34.2857142857)
                        .addAP("00:1E:8C:72:34:AD", 33.3333333333)
                        .addAP("00:0F:F7:2E:16:A1", 28.5714285714)
                        .addAP("00:0F:F7:2E:16:A0", 30.0)
                        .addAP("06:27:22:55:F2:A4", 27.1428571429);
                locator.addLocation()
                        .setName("uniba-MVII")
                        .addAP("00:22:55:F2:A1:32", 39.5238095238)
                        .addAP("00:22:55:F2:A1:30", 37.619047619)
                        .addAP("00:22:55:F2:A1:31", 34.2857142857)
                        .addAP("94:0C:6D:A7:7A:0A", 31.4285714286)
                        .addAP("00:19:07:34:C9:82", 34.2857142857)
                        .addAP("00:19:07:34:C9:80", 31.4285714286)
                        .addAP("00:1E:8C:72:34:AD", 34.7619047619)
                        .addAP("0E:27:22:F3:18:57", 49.5238095238)
                        .addAP("0A:27:22:F3:22:3F", 28.5714285714)
                        .addAP("06:27:22:F3:18:57", 48.0952380952)
                        .addAP("00:21:91:73:E6:60", 28.5714285714)
                        .addAP("0A:27:22:F3:21:29", 34.2857142857)
                        .addAP("0A:27:22:F3:26:0D", 65.2380952381)
                        .addAP("00:1D:60:46:96:6A", 36.6666666667)
                        .addAP("0A:27:22:F3:18:57", 50.4761904762)
                        .addAP("06:27:22:F3:26:0D", 76.6666666667)
                        .addAP("06:27:22:F3:22:3F", 30.0)
                        .addAP("0E:27:22:F3:26:0D", 75.2380952381)
                        .addAP("00:0F:F7:2E:16:A2", 42.380952381)
                        .addAP("00:0F:F7:2E:16:A1", 40.4761904762)
                        .addAP("00:0F:F7:2E:16:A0", 40.4761904762)
                        .addAP("0E:27:22:F3:22:3F", 35.7142857143);
                locator.addLocation()
                        .setName("studijne")
                        .addAP("0A:27:22:F3:1D:E8", 38.5714285714)
                        .addAP("CA:9F:DB:2B:AC:F0", 40.0)
                        .addAP("C6:9F:DB:2B:AC:F0", 34.2857142857)
                        .addAP("00:22:55:F2:A1:32", 39.2857142857)
                        .addAP("00:22:55:F2:A1:30", 40.3571428571)
                        .addAP("00:22:55:F2:A1:31", 42.8571428571)
                        .addAP("06:27:22:55:F2:A4", 50.7142857143)
                        .addAP("00:0D:54:9E:06:5B", 32.8571428571)
                        .addAP("0A:27:22:F3:23:85", 28.5714285714)
                        .addAP("C2:9F:DB:2B:AC:F0", 35.7142857143)
                        .addAP("12:27:22:F3:18:58", 27.1428571429)
                        .addAP("00:1F:C6:82:0E:E7", 34.6428571429)
                        .addAP("06:27:22:F3:18:58", 40.0)
                        .addAP("00:19:07:34:C9:81", 66.2857142857)
                        .addAP("00:19:07:34:C9:80", 65.4285714286)
                        .addAP("0E:27:22:F3:18:58", 34.2857142857)
                        .addAP("00:19:07:34:C9:82", 65.7142857143)
                        .addAP("0A:27:22:55:F2:A4", 63.9285714286)
                        .addAP("0A:E6:0F:86:88:DD", 38.5714285714)
                        .addAP("0E:27:22:F3:1D:E8", 28.5714285714)
                        .addAP("0E:27:22:F3:21:29", 40.0)
                        .addAP("00:4F:62:1D:EB:41", 40.0)
                        .addAP("00:0D:54:9E:03:A7", 25.7142857143)
                        .addAP("06:27:22:F3:1D:E8", 31.4285714286)
                        .addAP("0A:27:22:F3:18:58", 22.8571428571)
                        .addAP("0E:27:22:55:F2:A4", 60.0)
                        .addAP("06:27:22:F3:21:29", 31.4285714286)
                        .addAP("F8:1A:67:AC:DD:86", 34.0)
                        .addAP("00:0F:F7:2E:16:A1", 40.7142857143)
                        .addAP("00:0F:F7:2E:16:A0", 34.8571428571)
                        .addAP("00:0F:F7:2E:16:A2", 34.2857142857);
                locator.addLocation()
                        .setName("fayn-food")
                        .addAP("00:14:A9:75:3B:10", 34.2857142857)
                        .addAP("00:14:A9:75:3B:11", 40.7142857143)
                        .addAP("C8:D3:A3:34:E8:DC", 79.0476190476)
                        .addAP("06:27:22:F3:17:C0", 42.8571428571)
                        .addAP("00:1D:46:27:35:D2", 46.1904761905)
                        .addAP("00:1D:46:27:35:D1", 43.2142857143)
                        .addAP("00:1D:46:27:35:D0", 48.5714285714)
                        .addAP("0A:27:22:F3:17:C0", 33.8095238095)
                        .addAP("0E:27:22:F3:17:C0", 42.8571428571)
                        .addAP("06:27:22:F3:17:B8", 27.1428571429)
                        .addAP("00:1B:D4:EA:BF:E0", 35.3571428571);
                locator.addLocation()
                        .setName("inf-ostertag")
                        .addAP("0A:27:22:F3:18:58", 24.2857142857)
                        .addAP("06:27:22:F3:18:58", 51.4285714286)
                        .addAP("F8:1A:67:AC:DD:86", 28.5714285714)
                        .addAP("0E:27:22:F3:18:58", 28.5714285714)
                        .addAP("00:1E:8C:72:34:AD", 40.9523809524)
                        .addAP("00:0D:54:9E:06:5B", 34.7619047619)
                        .addAP("0A:27:22:F3:17:3E", 30.0)
                        .addAP("12:27:22:F3:18:58", 37.619047619)
                        .addAP("06:27:22:F3:21:29", 36.4285714286)
                        .addAP("00:1F:C6:82:0E:E7", 35.7142857143)
                        .addAP("0E:27:22:F3:21:29", 35.7142857143)
                        .addAP("00:23:CD:15:8E:10", 24.2857142857)
                        .addAP("0E:27:22:F3:17:3E", 30.0)
                        .addAP("00:0D:54:9E:03:A7", 63.3333333333)
                        .addAP("0A:27:22:F3:21:29", 32.1428571429);
                locator.addLocation()
                        .setName("spojovacka")
                        .addAP("0E:27:22:F3:23:85", 28.2857142857)
                        .addAP("0A:27:22:F3:1D:E8", 38.0952380952)
                        .addAP("00:19:07:34:63:91", 42.8571428571)
                        .addAP("00:19:07:34:63:90", 41.4285714286)
                        .addAP("00:19:07:34:63:92", 41.4285714286)
                        .addAP("06:27:22:55:F2:A4", 38.5714285714)
                        .addAP("90:E6:BA:BE:B6:3F", 25.7142857143)
                        .addAP("C2:9F:DB:2B:AC:F0", 36.6666666667)
                        .addAP("0A:27:22:F3:23:85", 27.1428571429)
                        .addAP("C6:9F:DB:2B:AC:F0", 40.7142857143)
                        .addAP("00:19:07:34:C9:81", 40.2857142857)
                        .addAP("00:19:07:34:C9:80", 41.1904761905)
                        .addAP("CA:9F:DB:2B:AC:F0", 47.8571428571)
                        .addAP("00:19:07:34:C9:82", 38.8095238095)
                        .addAP("0A:27:22:55:F2:A4", 32.8571428571)
                        .addAP("0A:E6:0F:86:88:DD", 41.4285714286)
                        .addAP("0E:27:22:F3:1D:E8", 36.2857142857)
                        .addAP("06:27:22:F3:23:85", 32.0)
                        .addAP("0E:27:22:F3:21:29", 31.4285714286)
                        .addAP("00:0D:54:9E:03:A7", 25.7142857143)
                        .addAP("06:27:22:F3:1D:E8", 41.9047619048)
                        .addAP("0E:27:22:55:F2:A4", 40.0)
                        .addAP("06:27:22:F3:21:29", 33.2142857143)
                        .addAP("60:A4:4C:C7:4A:E8", 32.8571428571)
                        .addAP("F8:1A:67:AC:DD:86", 30.0)
                        .addAP("00:0F:F7:2E:16:A1", 24.2857142857)
                        .addAP("00:0F:F7:2E:16:A2", 32.8571428571);
                locator.addLocation()
                        .setName("f-vstup")
                        .addAP("06:27:22:F3:17:C0", 35.7142857143)
                        .addAP("00:1B:FC:57:6C:52", 43.5714285714)
                        .addAP("0E:27:22:F3:17:B8", 37.1428571429)
                        .addAP("00:1D:46:27:35:D1", 44.6428571429)
                        .addAP("00:1D:46:27:35:D0", 46.0714285714)
                        .addAP("00:24:8C:CE:5E:DB", 32.8571428571)
                        .addAP("00:50:7F:52:2A:F8", 41.7857142857)
                        .addAP("00:19:07:34:D7:40", 44.6428571429)
                        .addAP("00:19:07:34:D7:41", 40.3571428571)
                        .addAP("00:19:07:34:D7:42", 44.2857142857)
                        .addAP("0A:27:22:F3:17:B8", 36.7857142857);
                locator.addLocation()
                        .setName("zachody")
                        .addAP("00:0F:F7:2E:16:A1", 32.1428571429)
                        .addAP("0E:27:22:F3:22:3F", 28.5714285714)
                        .addAP("00:1E:8C:72:34:AD", 45.0)
                        .addAP("0A:27:22:F3:26:0D", 57.8571428571)
                        .addAP("00:21:91:73:E6:60", 32.8571428571)
                        .addAP("06:27:22:F3:26:0D", 57.8571428571)
                        .addAP("06:27:22:F3:22:3F", 27.1428571429)
                        .addAP("0E:27:22:F3:26:0D", 57.8571428571)
                        .addAP("00:22:55:F2:A1:32", 54.2857142857)
                        .addAP("00:22:55:F2:A1:31", 51.4285714286)
                        .addAP("00:1D:60:46:96:6A", 46.4285714286)
                        .addAP("00:0F:F7:2E:16:A0", 33.5714285714)
                        .addAP("00:22:55:F2:A1:30", 51.4285714286)
                        .addAP("00:0F:F7:2E:16:A2", 39.2857142857);
                locator.addLocation()
                        .setName("inf-rovan")
                        .addAP("0A:27:22:F3:18:58", 46.4285714286)
                        .addAP("06:27:22:F3:18:58", 48.3333333333)
                        .addAP("F8:1A:67:AC:DD:86", 32.380952381)
                        .addAP("0E:27:22:F3:18:58", 46.9047619048)
                        .addAP("00:0D:54:9E:06:5B", 68.5714285714)
                        .addAP("00:1E:8C:72:34:AD", 28.0952380952)
                        .addAP("00:19:07:34:C9:82", 28.5714285714)
                        .addAP("12:27:22:F3:18:58", 42.8571428571)
                        .addAP("00:1F:C6:82:0E:E7", 55.0)
                        .addAP("00:23:CD:15:8E:10", 31.4285714286)
                        .addAP("00:0D:54:9E:03:A7", 46.4285714286);
                locator.addLocation()
                        .setName("schody-studijne")
                        .addAP("0A:27:22:F3:18:58", 45.7142857143)
                        .addAP("06:27:22:F3:18:58", 48.3333333333)
                        .addAP("00:19:07:34:C9:81", 51.1904761905)
                        .addAP("00:19:07:34:C9:80", 48.8095238095)
                        .addAP("0E:27:22:F3:18:58", 46.6666666667)
                        .addAP("00:0D:54:9E:06:5B", 40.4761904762)
                        .addAP("0A:27:22:55:F2:A4", 28.5714285714)
                        .addAP("0E:27:22:55:F2:A4", 44.2857142857)
                        .addAP("00:19:07:34:C9:82", 51.4285714286)
                        .addAP("06:27:22:55:F2:A4", 28.5714285714)
                        .addAP("0E:27:22:F3:17:3E", 25.7142857143)
                        .addAP("12:27:22:F3:18:58", 47.8571428571)
                        .addAP("00:1F:C6:82:0E:E7", 39.7619047619)
                        .addAP("00:1E:8C:72:34:AD", 28.5714285714)
                        .addAP("00:0F:F7:2E:16:A1", 30.7142857143)
                        .addAP("00:0F:F7:2E:16:A0", 30.0)
                        .addAP("00:22:55:F2:A1:30", 28.5714285714)
                        .addAP("00:0F:F7:2E:16:A2", 30.0);
                locator.addLocation()
                        .setName("uniba-IX")
                        .addAP("0A:27:22:F3:18:57", 47.1428571429)
                        .addAP("00:0F:F7:2E:16:A1", 34.7619047619)
                        .addAP("00:19:07:34:C9:81", 38.5714285714)
                        .addAP("0A:27:22:F3:26:0D", 83.8095238095)
                        .addAP("00:1E:8C:72:34:AD", 44.2857142857)
                        .addAP("00:22:55:F2:A1:32", 63.8095238095)
                        .addAP("94:0C:6D:A7:7A:0A", 34.7619047619)
                        .addAP("00:19:07:34:C9:82", 38.5714285714)
                        .addAP("06:27:22:F3:18:57", 31.4285714286)
                        .addAP("00:21:91:73:E6:60", 40.9523809524)
                        .addAP("00:0F:F7:2E:16:A0", 34.7619047619)
                        .addAP("06:27:22:F3:22:3F", 41.9047619048)
                        .addAP("0E:27:22:F3:22:3F", 37.1428571429)
                        .addAP("0E:27:22:F3:18:57", 51.4285714286)
                        .addAP("0E:27:22:F3:26:0D", 84.2857142857)
                        .addAP("00:0F:F7:2E:16:A2", 38.0952380952)
                        .addAP("00:1D:60:46:96:6A", 32.8571428571)
                        .addAP("06:27:22:F3:26:0D", 83.3333333333)
                        .addAP("00:22:55:F2:A1:30", 64.7619047619)
                        .addAP("00:22:55:F2:A1:31", 63.8095238095);

                //textView.setText(testString);

                String place = locator.localize(tempDates);
                if (place.equals("")) {
                    place = "a location we do not know about yet";
                    unknownLocationImage.setVisibility(View.VISIBLE);
                }
                textView.setText("You are at " + place);

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