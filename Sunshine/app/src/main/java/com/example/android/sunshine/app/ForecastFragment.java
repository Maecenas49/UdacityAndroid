package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Add this line for this fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_refresh){
            FetchWeatherTask WeatherTask =  new FetchWeatherTask();
            WeatherTask.execute("32608");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R. layout.fragment_main, container, false);

        //Fake forecast data for populating our adapter
        String[] weekForecast = {"Today - Sunny - 88/63", "Tomorrow - Foggy - 70/46",
                "Weds - Cloudy - 72/63","Thurs - Rainy - 64/51","Fri - Foggy - 70/46",
                "Sat - Sunny - 76/68"};



        ArrayAdapter<String> mForecastAdapter =
                new ArrayAdapter<String>(
                        //The current context (this fragment's parent activity
                        getActivity(),
                        // ID of list item layout
                        R.layout.list_item_forcast,
                        // ID of the textview to population
                        R.id.list_item_forecast_textview,
                        // Forecast_data
                        weekForecast);

        //Creating a ListView
        ListView mListView = (ListView) rootView.findViewById(R.id.listview_forcast);
        //Bind the adapter to the ListView
        mListView.setAdapter(mForecastAdapter);

        return rootView;
    }



    public class FetchWeatherTask extends AsyncTask<String,Integer,String> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params){


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        String format = "json";
        String units = "metric";
        int numDays = 7;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String QUERY_PARAM = "q";
            final String UNITS_PARAM = "units";
            final String FORMAT_PARAM = "mode";
            final String DAYS_PARAM = "cnt";
            Uri.Builder builder = new Uri.Builder();

            builder.scheme("http").authority("api.openweathermap.org")
                    .appendPath("data").appendPath("2.5").appendPath("forecast").appendPath("daily")
                    .appendQueryParameter(QUERY_PARAM,params[0])
                    .appendQueryParameter(FORMAT_PARAM,format)
                    .appendQueryParameter(UNITS_PARAM,units)
                    .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays));

            URL url = new URL(builder.build().toString());
            Log.v(LOG_TAG,"Built URI: " +builder.build().toString());
            //Previous URL without input parameters
            // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=32608&mode=json&units=metric&cnt=7");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Forecast JSON String: "+forecastJsonStr);

        } catch (IOException e) {
            Log.e("ForecastFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }
    return forecastJsonStr;
        }
    }
}
