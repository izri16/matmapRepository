package com.example.myfirstapp;

public class WifiInfo {
		
	private String SSID;
	private String BSSID;
	private String capabilities;
	private String level;
		
	public WifiInfo(String SSID, String BSSID, String capabilities, String level) {
		this.SSID = SSID;
		this.BSSID = BSSID;
		this.capabilities = capabilities;
		this.level = level;
	}
		
	public String getSSID() {
		return this.SSID;
	}
		
	public String getBSSID() {
		return this.BSSID;
	}
		
	public String getCapabilities() {
		return this.capabilities;
	}
		
	public String getLevel() {
		return this.level;
	}

}
