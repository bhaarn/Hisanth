package com.padhuga.hishanth;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegistrationFragment extends Fragment implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
         com.google.android.gms.location.LocationListener {

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

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        myCalendar = Calendar.getInstance();
        utils = new Utils(getActivity());

        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getActivity()))
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

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
        StringBuilder address;
        String finalAddress;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                address = new StringBuilder();
                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void validationCheck() {
        if (editText1.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.name_error, Toast.LENGTH_SHORT).show();
        } else if (editText2.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.address_error, Toast.LENGTH_SHORT).show();
        } else if (editText3.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.locality_error, Toast.LENGTH_SHORT).show();
        } else if (editText4.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.pincode_error, Toast.LENGTH_SHORT).show();
        } else if (editText5.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.mobile_error, Toast.LENGTH_SHORT).show();
        } else if (editText6.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.email_error, Toast.LENGTH_SHORT).show();
        } else if (editText7.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.date_error, Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            enableAddressFields();
            return;
        }
        startLocationUpdates();
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {
        } else {
            Toast.makeText(getActivity(), "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
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
        long UPDATE_INTERVAL = 2 * 1000;
        long FASTEST_INTERVAL = 2000;
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            enableAddressFields();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        address = getAddress(latitude, longitude);
        if (address.equalsIgnoreCase("No Address Returned") ||
                address.equalsIgnoreCase("Cannot Find Address")) {
            enableAddressFields();
        } else {
            disableAddressFields();
        }
    }

    private void checkLocation() {
        if (!isLocationEnabled())
            utils.showAlert(Objects.requireNonNull(getActivity()).getResources().getString(R.string.alert_location_title), getActivity().getResources().getString(R.string.alert_location_message),
                    getActivity().getResources().getString(R.string.alert_location_positive_button), getActivity().getResources().getString(android.R.string.cancel));
        isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
