package com.example.win_10.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener ,LocationListener {



        TextView txtLocation;
        Button btnFetch;

        LocationManager locationManager;
        ProgressDialog progressDialog;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initViews();
        }

        void initViews() {
            txtLocation = (TextView) findViewById(R.id.textViewLocation);
            btnFetch = (Button) findViewById(R.id.buttonFetch);
            btnFetch.setOnClickListener(this);

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetching Location...");
            progressDialog.setCancelable(false);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.buttonFetch) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Please Grant Permissions from Settings",Toast.LENGTH_LONG).show();
                }else{
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 10, this);
                    progressDialog.show();
                }

            }
        }

        // After every 5 or 10
        @Override
        public void onLocationChanged(Location location) {

            // Geocoding
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            txtLocation.setText("Location: "+latitude+" : "+longitude);

            progressDialog.dismiss();

            // No More Location Updates Required
            locationManager.removeUpdates(this);

            // Reverse Geocoding
            // We fetch the address from latitude and longitude

            try {
                Geocoder geocoder = new Geocoder(this);
                List<Address> adrsList = geocoder.getFromLocation(latitude, longitude, 2);
                if(adrsList!=null && adrsList.size()>0){
                    Address address = adrsList.get(0);

                    StringBuffer buffer = new StringBuffer();
                    for(int i=0;i<address.getMaxAddressLineIndex();i++){
                        buffer.append(address.getAddressLine(i)+"\n");
                    }

                    txtLocation.setText(buffer.toString());
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        locationManager.removeUpdates(this);
//    }



        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
