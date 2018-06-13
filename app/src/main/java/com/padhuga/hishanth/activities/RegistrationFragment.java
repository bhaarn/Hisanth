package com.padhuga.hishanth.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.padhuga.hishanth.R;
import com.padhuga.hishanth.utils.Utils;

import java.util.Calendar;
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
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,getActivity().getResources().getStringArray(R.array.city_array));
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setBackgroundResource(R.drawable.spinner_text_selection_style);
        spinner1.setAdapter(spinnerArrayAdapter1);
        spinner1.setOnItemSelectedListener(this);
        spinner2 = rootView.findViewById(R.id.spinner2);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,getActivity().getResources().getStringArray(R.array.service_array));
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setBackgroundResource(R.drawable.spinner_text_selection_style);
        spinner2.setAdapter(spinnerArrayAdapter2);
        spinner2.setOnItemSelectedListener(this);
        spinner3 = rootView.findViewById(R.id.spinner3);
        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,getActivity().getResources().getStringArray(R.array.mode_of_delivery_array));
        spinnerArrayAdapter3.setDropDownViewResource(R.layout.spinner_item);
        spinner3.setBackgroundResource(R.drawable.spinner_text_selection_style);
        spinner3.setAdapter(spinnerArrayAdapter3);
        spinner3.setOnItemSelectedListener(this);
        button1 = rootView.findViewById(R.id.button1);
        button1.setOnClickListener(viewClickListener);
        button2 = rootView.findViewById(R.id.button2);
        button2.setOnClickListener(viewClickListener);
        date = utils.setDate(date, myCalendar, editText7);
        return rootView;
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editText7:
                    utils.dateSetter(date, myCalendar);
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

    private void validationCheck() {
        if (editText1.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.name_error, Toast.LENGTH_SHORT).show();
        } else if (editText2.getText().toString().matches("") && addressCardView.getVisibility() == View.VISIBLE) {
            Toast.makeText(getActivity(), R.string.address_error, Toast.LENGTH_SHORT).show();
        } else if (editText3.getText().toString().matches("") && localityCardView.getVisibility() == View.VISIBLE) {
            Toast.makeText(getActivity(), R.string.locality_error, Toast.LENGTH_SHORT).show();
        } else if (editText4.getText().toString().matches("") && pincodeCardView.getVisibility() == View.VISIBLE) {
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
            utils.showPrice(details, getActivity().getResources().getString(R.string.alert_registration_module_name));
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
        address = utils.getAddress(latitude, longitude);
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
                    getActivity().getResources().getString(R.string.alert_location_positive_button), getActivity().getResources().getString(android.R.string.cancel),
                    getActivity().getResources().getString(R.string.alert_location_module_name), "");
        isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
