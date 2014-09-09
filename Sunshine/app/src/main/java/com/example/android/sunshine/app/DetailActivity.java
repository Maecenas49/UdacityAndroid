package com.example.android.sunshine.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    private static String detailText;
    private final String LOG_TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get intent and create a variable that identifies which text to display
        Intent startIntent = getIntent();
        if (startIntent !=null && startIntent.hasExtra(Intent.EXTRA_TEXT)) {
            detailText = startIntent.getStringExtra(Intent.EXTRA_TEXT);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.detail, menu);
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
        return super.onOptionsItemSelected(item);
    }

    /**
    * Created by Mike on 9/8/2014.
    */
    public static class DetailFragment extends Fragment {

        private ShareActionProvider mSharedActionProvider;
        private final String LOG_TAG = "DetailFragment";
        private String mForecastStr;

        public DetailFragment(){
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            //Setting detail text as identified in detailActivity
            DetailActivity detailActivity = (DetailActivity)getActivity();
            //Create TextView to display weather details received from intent
            Intent startIntent = getActivity().getIntent();
            if (startIntent !=null && startIntent.hasExtra(Intent.EXTRA_TEXT)) {
                mForecastStr = startIntent.getStringExtra(Intent.EXTRA_TEXT);
            }
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastStr);
            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
            menuInflater.inflate(R.menu.detailfragment, menu);
            //Retrieved the sharedActionProvider
            MenuItem item = menu.findItem(R.id.menu_item_share);
            mSharedActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            Intent shareIntent = new Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, mForecastStr + " #SunshineApp")
                    .setType("text/plain").setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            if (mSharedActionProvider !=null){
                mSharedActionProvider.setShareIntent(shareIntent);
            }else {
                Log.d(LOG_TAG, "Share Action Provider is null.");
            }
        }
    }
}
