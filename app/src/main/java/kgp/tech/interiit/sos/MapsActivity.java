package kgp.tech.interiit.sos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Vector;

import kgp.tech.interiit.sos.Utils.NetworkLocationService;
import kgp.tech.interiit.sos.Utils.People;


public class MapsActivity extends FragmentActivity implements LocationListener {
    String TAG = "YOLO : Maps: ";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public NetworkLocationService appLocationServiceNet = null;
    public Vector<People> getHelpers() {
        // We have to get it from the server
        Vector<People> helpers = new Vector<People>(2);

        helpers.addElement(new People("Home", 22.321813, 87.304682));
        helpers.addElement(new People("Work", 22.315974, 87.316367));
        return helpers;
    }

    public Vector<MarkerOptions> getMarkers() {
        Vector<People> helpers = getHelpers();
        Vector<MarkerOptions> markers = new Vector<MarkerOptions>(helpers.size());
        MarkerOptions mp;
        for(int i=0;i<helpers.size();i++)
        {
            mp = new MarkerOptions();
            mp.title(helpers.get(i).getName());
            mp.position(helpers.get(i).getLat_lng());
            mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            markers.add(mp);
        }
         return markers;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (appLocationServiceNet == null)
                       appLocationServiceNet = new NetworkLocationService();
        //Log.e("", "OnCreate");
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if(Build.VERSION.SDK_INT >=23) {
            if (!(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//             TODO: Consider calling
//                public void requestPermissions(@NonNull String[] permissions, int requestCode)
//
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
//             to handle the case where the user grants the permission. See the documentation
//             for Activity#requestPermissions for more details.

                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                return;
            }
        }
        else {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void openchat(View v)
    {
        Intent i = new Intent(MapsActivity.this, Chatlist.class);
        startActivity(i);
    }
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();

        MarkerOptions mp = new MarkerOptions();

        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

        mp.title("Your Location");

        mMap.addMarker(mp);
        ////// Adding Markers for helpers
        Vector<MarkerOptions> markers = getMarkers();
        for (int i=0;i<markers.size();i++)
            mMap.addMarker(markers.get(i));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
