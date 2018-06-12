package com.padhuga.hishanth.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.padhuga.hishanth.adapters.PagerAdapter;
import com.padhuga.hishanth.R;
import com.padhuga.hishanth.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static boolean all_permission = false;
    Utils utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.materialup_tabs);
        ViewPager viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        FragmentPagerAdapter fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.launcher);
        initializeAds(this);
        utils = new Utils(this);
        if (utils.checkAndRequestPermissions()) {
            all_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        all_permission = true;
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            utils.showDialogOK(getResources().getString(R.string.alert_permission_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    utils.checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.go_to_settings), Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        }
                    }
                }
            }
        }
    }

    void initializeAds(Activity activity) {
        MobileAds.initialize(this, getResources().getString(R.string.ad_id));
        AdView mAdView = activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("D61A47C78CD8E1F5ECD16B2D1BD211C6")
                .build();
        mAdView.loadAd(adRequest);
    }
}