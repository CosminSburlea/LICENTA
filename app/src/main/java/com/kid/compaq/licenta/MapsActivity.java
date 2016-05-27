package com.kid.compaq.licenta;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    HttpURLConnection urlConnection= null;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new MarkerTask().execute();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().isCompassEnabled();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);


    }

    class MarkerTask extends AsyncTask<Void, String, Void> {

       // public static final String LOG_TAG = "ExampleApp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
//public static final String SERVICE_URL = "http://192.168.48.14/stations.php";

        // Invoked by execute() method of this object
        @Override
        protected Void doInBackground(Void... params) {

            InputStream is = null;
            String result="";
            try {
                URL url = new URL("http://192.168.43.146/stations.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                is = urlConnection.getInputStream();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line= null;
                while( (line= reader.readLine())!=null){
                    result +=line;
                }
                is.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    LatLng latLng = new LatLng(jsonObj.getJSONArray("latlng").getDouble(0),
                            jsonObj.getJSONArray("latlng").getDouble(1));


                    // Create a marker for each station in the JSON data.
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title(jsonObj.getString("nume"))
                            .position(latLng));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;

        }
            // Executed after the complete execution of doInBackground() method
            @Override
            protected void onPostExecute (Void result){


            }

    }
}
