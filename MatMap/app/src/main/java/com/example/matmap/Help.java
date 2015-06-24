package com.example.matmap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Help section for new users
 */
public class Help extends ActionBarActivity {
    private TextView helpBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        this.helpBody = (TextView) findViewById(R.id.helpBody);

        this.helpBody.setText("To create records of specific places simply click on 'Records creator'" +
                " button in menu. Then type name and click 'Add new record'. Record will be " +
                "added into 'Records manager' which you can opened by clicking on pen picture at " +
                "the top of 'Records creator'. You can then delete, rename and find any records you " +
                "want. Do not forget to create neighbors for all records to find path between them. " +
                "To do this click on record you want and in the menu press 'Neighbor creator'. " +
                "\n\nIf you want to know you location just click left button in the top of the main menu bar. " +
                "If you want to find path to some destination click the middle button in the same bar. " +
                "To open the History click right button in same bar, too. You can delete records " +
                "from history of course when click on delete in the history menu. When you click on any " +
                "record in the 'History' it will find path. \n\nIf you want to put you records into file " +
                "open 'Records creator' and press 'Put changes into file' the in menu. File will be" +
                " created in your downloads folder." +
                "If you delete some " +
                "records and you want them to deleted from history and neighbors too go to delete " +
                "section and in menu press 'Clear neighbors and history'. If you delete all the " +
                "records that will happen automatically. \n\nHave a nice locating with MatMap application.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
}
