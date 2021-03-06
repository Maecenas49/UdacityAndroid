package com.example.android.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.net.URI;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        if (id==R.id.action_map){
            showMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Function to pass the users preferred location through an implicit intent to view a map
    private void showMap(){
        String location = PreferenceManager.getDefaultSharedPreferences(this).getString("location", getString(R.string.pref_location_default));
        //Create a geo Uri to pass to the map viewer
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q",location).build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoLocation);
        if (mapIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
