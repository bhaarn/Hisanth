package com.padhuga.hishanth.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.padhuga.hishanth.R;

public class CorporatePackagesFragment extends Fragment {

    public static CorporatePackagesFragment newInstance() {
        return new CorporatePackagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_corporate_packages, container, false);
    }
}
