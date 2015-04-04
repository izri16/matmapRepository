package com.example.myfirstapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
	public static final String EXTRA_MESSAGE="com.example.myfirstapp.MESSAGE";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
        case R.id.action_search:
        	openSearch();
        	return true;
        case R.id.action_settings:
        	openSettings();
        	return true;
        case R.id.action_new_location:
        	openNewLocation();
        	return true;
        case R.id.action_my_history:
        	openHistory();
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        		
        }
    }
        
    public void openSearch() {
    	Intent intent = new Intent(this, Search.class);
    	startActivity(intent);
    }
    
    public void openNewLocation() {
    	Intent intent = new Intent(this, NewLocation.class);
    	startActivity(intent);
    }
    
    public void openSettings() {
    	Intent intent = new Intent(this, MySettings.class);
    	startActivity(intent);
    }
    
    public void openHistory() {
    	Intent intent = new Intent(this, MyHistory.class);
    	startActivity(intent);
    }
}
