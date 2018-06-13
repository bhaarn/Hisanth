package com.padhuga.hishanth.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.padhuga.hishanth.R;

public class CustomizedPackagesFragment extends Fragment {

    public static CustomizedPackagesFragment newInstance() {
        return new CustomizedPackagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customized_packages, container, false);
    }
}
