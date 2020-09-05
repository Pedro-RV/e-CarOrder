package com.example.e_carorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_carorder.chargePointInfo.ChargePointInfoActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;


public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView mapView;
    private SearchView searchView;
    private DatabaseReference mDatabase;
    private int ACCES_LOCATION_REQUEST_CODE = 10001;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapsView);
        searchView = view.findViewById(R.id.sv_location);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")){

                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        if(addressList.size() != 0){
                            Address address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;

        // Do not show gmaps' toolbar
        map.getUiSettings().setMapToolbarEnabled(false);

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            enableUserLocation();
            zoomToUserLocation();
        }else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCES_LOCATION_REQUEST_CODE);
            } else{
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCES_LOCATION_REQUEST_CODE);
            }
        }

        DatabaseReference databaseReference;

        databaseReference = mDatabase.child("ChargePoints");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String lat = ds.child("addressInfo").child("latitude").getValue().toString();
                    String lon = ds.child("addressInfo").child("longitude").getValue().toString();
                    String title = ds.child("addressInfo").child("title").getValue().toString();
                    String status = ds.child("statusType").getValue().toString();
                    String id = ds.child("id").getValue().toString();

                    Float color = BitmapDescriptorFactory.HUE_YELLOW;

                    if(status.equals("Available")){
                        color = BitmapDescriptorFactory.HUE_CYAN;
                    }else if(status.equals("Damaged")){
                        color = BitmapDescriptorFactory.HUE_RED;
                    }else if(status.equals("Unkown")){
                        color = BitmapDescriptorFactory.HUE_ORANGE;
                    }


                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
                            .title(title)
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                            .snippet("Id: " + id)
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String chargePointId = marker.getSnippet();

                Intent i = new Intent(getContext(), ChargePointInfoActivity.class);
                i.putExtra("chargePointId", chargePointId);
                startActivity(i);
            }
        });

    }

    private void enableUserLocation() {
        map.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation(){
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ACCES_LOCATION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                enableUserLocation();
                zoomToUserLocation();
            } else{

            }
        }
    }

}