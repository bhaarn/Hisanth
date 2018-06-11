package com.padhuga.hishanth.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.padhuga.hishanth.R;
import com.padhuga.hishanth.utils.Constants;
import com.padhuga.hishanth.utils.Utils;

public class ContactUsFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    MapView mapView;
    View rootView;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        TextView mobileNumber1 = rootView.findViewById(R.id.mobile_number1);
        mobileNumber1.setOnClickListener(viewClickListener);
        TextView mobileNumber2 = rootView.findViewById(R.id.mobile_number2);
        mobileNumber2.setOnClickListener(viewClickListener);
        TextView emailId = rootView.findViewById(R.id.email_id);
        emailId.setOnClickListener(viewClickListener);
        TextView facebook = rootView.findViewById(R.id.facebook);
        facebook.setOnClickListener(viewClickListener);
        TextView whatsapp = rootView.findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(viewClickListener);
        TextView youtube = rootView.findViewById(R.id.youtube);
        youtube.setOnClickListener(viewClickListener);
        return rootView;
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Utils utils = new Utils(getActivity());
            switch (v.getId()) {
                case R.id.mobile_number1:
                    utils.callNumber(getActivity().getResources().getString(R.string.static_mobile_number1));
                    break;
                case R.id.mobile_number2:
                    utils.callNumber(getActivity().getResources().getString(R.string.static_mobile_number2));
                    break;
                case R.id.email_id:
                    utils.sendEmail("");
                    break;
                case R.id.facebook:
                    utils.openFacebook(getActivity().getResources().getString(R.string.static_facebook));
                    break;
                case R.id.whatsapp:
                    utils.openWhatsapp(getActivity().getResources().getString(R.string.static_whatsapp));
                    break;
                case R.id.youtube:
                    utils.openYoutube(getActivity().getResources().getString(R.string.static_youtube));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(Constants.LATITUDE,Constants.LONGITUDE)).title(getString(R.string.map_title)).snippet(getString(R.string.map_snippet)));
        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(Constants.LATITUDE,Constants.LONGITUDE))
                .zoom(Constants.ZOOM_VALUE).bearing(Constants.BEARING).tilt(Constants.TILT).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}