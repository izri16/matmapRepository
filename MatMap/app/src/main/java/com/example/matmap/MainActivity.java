package com.example.matmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Head Activity of application
 */
public class MainActivity extends ActionBarActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
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
        case R.id.action_new_location:
        	openNewLocation();
        	return true;
        case R.id.action_history:
        	openHistory();
        	return true;
        case R.id.action_temporary_localization:
            openTemporaryLocalisation();
            return true;
        case R.id.action_help:
            openHelp();
            return true;
        default:
        	return super.onOptionsItemSelected(item);
        		
        }
    }

    /**
     * Opens Search Activity
     */
    public void openSearch() {
    	Intent intent = new Intent(this, Search.class);
    	startActivity(intent);
    }

    /**
     * Opens TemporaryLocalisation Activity
     */
    public void openTemporaryLocalisation() {
        Intent intent = new Intent(this, RecordsCreator.class);
        startActivity(intent);
    }

    /**
     * Opens NewLocation Activity
     */
    public void openNewLocation() {
    	Intent intent = new Intent(this, NewLocation.class);
    	startActivity(intent);
    }

    /**
     * Opens History Activity
     */
    public void openHistory() {
    	Intent intent = new Intent(this, History.class);
    	startActivity(intent);
    }

    /**
     * Opens Help Activity
     */
    public void openHelp() {
        Intent intent = new Intent(this, Help.class);
        startActivity(intent);
    }

    /**
     * Used to get current time
     *
     * @return time in yyyy-MM-dd HH:mm:ss format
     */
    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
