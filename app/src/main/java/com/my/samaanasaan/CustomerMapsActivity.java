package com.my.samaanasaan;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.my.samaanasaan.directionshelpers.FetchURL;
import com.my.samaanasaan.directionshelpers.TaskLoadedCallback;
import com.my.samaanasaan.model.MyLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CustomerMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, TaskLoadedCallback {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    FusedLocationProviderClient mFusedLocationProivideClient;
    LocationCallback mlocationCallback;
    Button confirmLocation;
    PlacesClient placesClient;///// This is used to get create Client
    LatLng pickUplatLng; // This is use to save the search latlng Pick Up location
    LatLng dropOffLatLng;// This is use to save the search latlng Dropoff location
    String Distance;//This is use to save the distance between two places
    String Duration;//This is use to save the time duration to travel from on place to other
    Polyline polyline;
    Marker destinationMarker;
    Marker pickUpMarker;
    public static final String MY_PREFS_NAME = "MyPrefsFile";//used in shared prefrences
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_maps);

        mFusedLocationProivideClient = LocationServices.getFusedLocationProviderClient(this);
        mlocationCallback= new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult==null)
                {
                    return;
                }
                else
                {
                    lastLocation=locationResult.getLastLocation();
                    //Toast.makeText(CustomerMapsActivity.this, "My Location Updates "+lastLocation.getLongitude()+"/n"+lastLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                }
            }
        };
/*

*/

//////////////=====Google Places api
       // String apiKey="AIzaSyDYoQybddM6c-Daz0bHBe7h2tuyzxHmW1k";
       // String  apiKey = "AIzaSyCyczR1R6cl3fakz_oGYSbDpzWH9OL0ZD8";
        String apiKey="AIzaSyCU0r8gqys1r78L5M13EEbYvfnKdopI7u4";
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), apiKey);

        }
        ////////////////////================Auto Complete from Places Api for Dropoff Location===================

        placesClient=Places.createClient(CustomerMapsActivity.this);
        final AutocompleteSupportFragment autocompleteSupportFragment=(AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                mMap.clear();
                pickUplatLng = place.getLatLng();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(pickUplatLng));
                if (pickUpMarker != null) {
                    pickUpMarker.remove();
                    pickUpMarker = mMap.addMarker(new MarkerOptions()
                            .position(pickUplatLng)
                            .title(place.getName())
                            .snippet(place.getName()));

                } else {
                    pickUpMarker = mMap.addMarker(new MarkerOptions()
                            .position(pickUplatLng)
                            .title(place.getName())
                            .snippet(place.getName()));

                }


            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        ////////////////////================Auto Complete from Places Api for Dropoff Location===================
        final AutocompleteSupportFragment autocompleteSupportFragmentDropoff=(AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_dropoff);
        autocompleteSupportFragmentDropoff.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,Place.Field.NAME));

        autocompleteSupportFragmentDropoff.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if(polyline!=null)
                {
                    polyline.remove();
                }
                dropOffLatLng=place.getLatLng();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(dropOffLatLng));
                if(destinationMarker!=null)
                {
                    destinationMarker.remove();
                    destinationMarker=mMap.addMarker(new MarkerOptions()
                            .position(dropOffLatLng)
                            .title(place.getName())
                            .snippet(place.getName()));

                }
                else
                {
                    destinationMarker=mMap.addMarker(new MarkerOptions()
                            .position(dropOffLatLng)
                            .title(place.getName())
                            .snippet(place.getName()));

                }

///////================This is to take polyline on map==========
                //Location.distanceBetween(pickUplatLng.latitude, pickUplatLng.longitude, dropOffLatLng.latitude, dropOffLatLng.longitude, Distance); //This line calculate the distance between two palaces and store it to distance array
                //String url=getUrl(pickUplatLng, dropOffLatLng, "driving");
                //new FetchURL(CustomerMapsActivity.this).execute(url,"driving");
                getDestinationInfo(dropOffLatLng, pickUplatLng);


            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

            ///////


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        confirmLocation=(Button)findViewById(R.id.confirmLocation);
        mapFragment.getMapAsync(this);

        confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerMapsActivity.this, "Distance "+Distance, Toast.LENGTH_SHORT).show();
                Intent goToNext=new Intent(CustomerMapsActivity.this,LuggageEstimateActivity.class);
                goToNext.putExtra("Distance", Distance);
                goToNext.putExtra("Duration", Duration);

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("Distance", Distance);
                editor.putString("Duration", Duration);
                editor.apply();
                startActivity(goToNext);
               // Goto the next Activity
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleAPIClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        else
        {
            mFusedLocationProivideClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null)
                    {
                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                        mMap.addMarker(new MarkerOptions().position(loc).title("You"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CustomerMapsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
        //mMap.setMyLocationEnabled(true);

    }

    ////////===============This method makes a string of url to fetch the data===========================

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyCU0r8gqys1r78L5M13EEbYvfnKdopI7u4";
        return url;
    }
//////////////-===========End of url string making method======================================
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        else {
            mFusedLocationProivideClient.requestLocationUpdates(locationRequest,mlocationCallback, null);
             //latLng= new LatLng(lastLocation.getLongitude(), lastLocation.getLatitude());
          //  LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            //mMap.addMarker(new MarkerOptions().position(latLng).title("I am Here"));

        }
        //LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation=location;
        LatLng latLng=new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    protected synchronized void buildGoogleAPIClient()
    {

        googleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();

    }
///////////================This is interface implemented method that use to drew ployline on route on map
    @Override
    public void onTaskDone(Object... values) {
        if (polyline!=null)
        {
            polyline.remove();
            polyline=mMap.addPolyline((PolylineOptions) values[0]);
        }
        else
        {
            polyline=mMap.addPolyline((PolylineOptions) values[0]);
        }
    }


    /**
     * Draw polyline on map, get distance and duration of the route
     *
     * @param latLngDestination LatLng of the destination
     */
    private void getDestinationInfo(LatLng latLngDestination,LatLng latLngOrigign) {

        String serverKey = "AIzaSyCU0r8gqys1r78L5M13EEbYvfnKdopI7u4"; // Api Key For Google Direction API \\
        final LatLng origin = latLngOrigign;
        final LatLng destination = latLngDestination;
        //-------------Using AK Exorcist Google Direction Library---------------\\
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                       // dismissDialog();
                        String status = direction.getStatus();
                        if (status.equals(RequestResult.OK)) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String d=distanceInfo.getValue();
                            String t=durationInfo.getValue();
                            Toast.makeText(CustomerMapsActivity.this, d, Toast.LENGTH_SHORT).show();
                            Distance = d;
                            //Duration= takeTimeInMinutes(t);
                          // parts=t.split(" ");

                          //  Duration = Float.parseFloat(t);

                            //------------Displaying Distance and Time-----------------\\
                           // showingDistanceTime(distance, duration); // Showing distance and time to the user in the UI \\
//                            String message = "Total Distance is " + distance + " and Estimated Time is " + duration;
//                            StaticMethods.customSnackBar(consumerHomeActivity.parentLayout, message,
//                                    getResources().getColor(R.color.colorPrimary),
//                                    getResources().getColor(R.color.colorWhite), 3000);

                            //--------------Drawing Path-----------------\\
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(CustomerMapsActivity.this,
                                    directionPositionList,
                                    5, getResources().getColor(R.color.colorPrimary));
                            if (polyline!=null)
                            {
                                polyline.remove();
                                polyline=mMap.addPolyline(polylineOptions);
                            }
                            else
                            {
                                polyline=mMap.addPolyline(polylineOptions);
                            }
                            //--------------------------------------------\\

                            //-----------Zooming the map according to marker bounds-------------\\
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(origin);
                            builder.include(destination);
                            LatLngBounds bounds = builder.build();

                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                            mMap.animateCamera(cu);
                            //------------------------------------------------------------------\\

                        } else if (status.equals(RequestResult.NOT_FOUND)) {
                            Toast.makeText(CustomerMapsActivity.this, "No routes exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });
        //-------------------------------------------------------------------------------\\

    }



}
