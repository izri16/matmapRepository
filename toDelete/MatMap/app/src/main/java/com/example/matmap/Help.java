package com.example.matmap;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.matmap.R;

public class Help extends ActionBarActivity {
    private TextView helpHeader;
    private TextView helpBody;
    private TextView helpMiddle;
    private TextView helpExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        this.helpHeader = (TextView) findViewById(R.id.helpHeader);
        this.helpBody = (TextView) findViewById(R.id.helpBody);
        this.helpMiddle = (TextView) findViewById(R.id.helpMiddle);
        this.helpExample = (TextView) findViewById(R.id.helpExample);

        this.helpHeader.setText("How should I type?");
        this.helpBody.setText("When typing destination name separate every part of it's name by space " +
                "except when typing F1, F2 or some H room like H5. Means that in all other cases " +
                "words and numbers must be separated. Searching will start automatically after an option " +
                "will be selected and then it will be added into history.\n");
        this.helpMiddle.setText("See some correct examples:");
        this.helpExample.setText("M IV \nF1 109 \nH5 \nA \nF1 \nM 217");
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
