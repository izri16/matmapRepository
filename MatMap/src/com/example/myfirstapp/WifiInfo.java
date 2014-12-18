package com.example.myfirstapp;

public class WifiInfo {
		
	private String SSID;
	private String BSSID;
	private String capabilities;
	private String level;
    private Integer strength;
		
	public WifiInfo(String SSID, String BSSID, String capabilities, String level) {
		this.SSID = SSID;
		this.BSSID = BSSID;
		this.capabilities = capabilities;
		this.level = level;

        this.strength = 100 + Integer.parseInt(level);

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

    public Integer getStrength() {
        return this.strength;
    }

}
