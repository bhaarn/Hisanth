package com.padhuga.hishanth;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegistrationFragment extends Fragment implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    CardView gpsAddressCardView;
    CardView addressCardView;
    CardView localityCardView;
    CardView cityCardView;
    CardView pincodeCardView;

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    EditText editText7;
    EditText gpsAddress;

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;

    Button button1;
    Button button2;

    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;

    String selectedCity;
    String selectedService;
    String selectedDeliveryMode;
    String details;
    Utils utils;
    String address;

    Double latitude;
    Double longitude;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        myCalendar = Calendar.getInstance();
        utils = new Utils(getActivity());

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        checkLocation();

        gpsAddressCardView = rootView.findViewById(R.id.gpsAddressCardView);
        addressCardView = rootView.findViewById(R.id.addressCardView);
        localityCardView = rootView.findViewById(R.id.localityCardView);
        cityCardView = rootView.findViewById(R.id.cityCardView);
        pincodeCardView = rootView.findViewById(R.id.pincodeCardView);

        editText1 = rootView.findViewById(R.id.editText1);
        editText2 = rootView.findViewById(R.id.editText2);
        editText3 = rootView.findViewById(R.id.editText3);
        editText4 = rootView.findViewById(R.id.editText4);
        editText5 = rootView.findViewById(R.id.editText5);
        editText6 = rootView.findViewById(R.id.editText6);
        editText7 = rootView.findViewById(R.id.editText7);
        editText7.setOnClickListener(viewClickListener);
        gpsAddress = rootView.findViewById(R.id.gpsAddress);

        spinner1 = rootView.findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(this);
        spinner2 = rootView.findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(this);
        spinner3 = rootView.findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener(this);

        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        button1.setOnClickListener(viewClickListener);
        button2.setOnClickListener(viewClickListener);

        address = getAddress(latitude, longitude);
        if(address.equalsIgnoreCase("No Address Returned") ||
                address.equalsIgnoreCase("Cannot Find Address")) {
            enableAddressFields();
        } else {
            disableAddressFields();
        }

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                utils.updateLabel(myCalendar, editText7);
            }
        };

        return rootView;
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editText7:
                    dateSetter();
                    break;

                case R.id.button1:
                    validationCheck();
                    break;

                case R.id.button2:
                    clearFields();
                    break;
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Spinner spinner = (Spinner) adapterView;
        switch (spinner.getId()) {
            case R.id.spinner1:
                selectedCity = adapterView.getItemAtPosition(position).toString();
                break;
            case R.id.spinner2:
                selectedService = adapterView.getItemAtPosition(position).toString();
                break;
            case R.id.spinner3:
                selectedDeliveryMode = adapterView.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        StringBuilder address = null;
        String finalAddress;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                address = new StringBuilder();
                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                    address.append(addresses.get(0).getAddressLine(i)).append("\n");
                }
                finalAddress = address.toString();
            } else {
                finalAddress = "No Address Returned";
            }
        } catch (IOException e) {
            e.printStackTrace();
            finalAddress = "Cannot Find Address";
        }
        return finalAddress;
    }

    private void disableAddressFields() {
        addressCardView.setVisibility(View.GONE);
        localityCardView.setVisibility(View.GONE);
        cityCardView.setVisibility(View.GONE);
        pincodeCardView.setVisibility(View.GONE);
        gpsAddressCardView.setVisibility(View.VISIBLE);
        gpsAddress.setText(address);
    }

    private void enableAddressFields() {
        addressCardView.setVisibility(View.VISIBLE);
        localityCardView.setVisibility(View.VISIBLE);
        cityCardView.setVisibility(View.VISIBLE);
        pincodeCardView.setVisibility(View.VISIBLE);
        gpsAddressCardView.setVisibility(View.GONE);
    }

    private void dateSetter() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void validationCheck() {
        if (editText1.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.name_error, Toast.LENGTH_SHORT).show();
            return;
        } else if (editText2.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.address_error, Toast.LENGTH_SHORT).show();
            return;
        } else if (editText3.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.locality_error, Toast.LENGTH_SHORT).show();
            return;
        } else if (editText4.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.pincode_error, Toast.LENGTH_SHORT).show();
            return;
        } else if (editText5.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.mobile_error, Toast.LENGTH_SHORT).show();
            return;
        } else if (editText6.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.email_error, Toast.LENGTH_SHORT).show();
            return;
        } else if (editText7.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.date_error, Toast.LENGTH_SHORT).show();
            return;
        } else {
            details = "Name : " + editText1.getText().toString() + "\n" + "Address : " + editText2.getText().toString() + "\n" + "Locality : " + editText3.getText().toString() +
                    "\n" + "City : " + selectedCity + "\n" + "PinCode : " + editText4.getText().toString() + "\n" + "MobileNumber : " + editText5.getText().toString() + "\n"
                    + "Email ID : " + editText6.getText().toString() + "\n" + "Service : " + selectedService + "\n" + "Function Date : " + editText7.getText().toString() + "\n"
                    + "Mode of Delivery : " + selectedDeliveryMode + "\n";
            utils.sendEmail(details);
        }
    }

    private void clearFields() {
        editText1.setText("");
        editText2.setText("");
        editText3.setText("");
        editText4.setText("");
        editText5.setText("");
        editText6.setText("");
        editText7.setText("");
        spinner1.setSelection(0);
        spinner2.setSelection(0);
        spinner3.setSelection(0);
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(getActivity(), "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Bharani", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("Bharani", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
