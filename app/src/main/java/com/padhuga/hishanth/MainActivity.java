package com.padhuga.hishanth;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
       // implements AppBarLayout.OnOffsetChangedListener {

  /*  private ImageView mProfileImage;
    private int mMaxScrollSize;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;*/

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.materialup_tabs);
        ViewPager viewPager = findViewById(R.id.viewPager);
      /*  AppBarLayout appbarLayout = findViewById(R.id.materialup_appbar);
        mProfileImage = findViewById(R.id.materialup_profile_image);

        Toolbar toolbar = findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();*/
        tabLayout.setupWithViewPager(viewPager);
        FragmentPagerAdapter fragmentPagerAdapter = new pagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        initializeAds(this);
    }

 /*   @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 /*   SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+919790495863", null, RegistrationFragment.details, null, null);*/

                    /* 	Intent sendIntent = new Intent(Intent.ACTION_VIEW);
	                    sendIntent.putExtra("sms_body", "default content");
	                    sendIntent.setType("vnd.android-dir/mms-sms");
	                    startActivity(sendIntent);*/

                    Toast.makeText(this, "SMS sent.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;
            default:
                break;
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
        Boolean b = adRequest.isTestDevice(this);
        Log.d("Bharani", b.toString());
    }

    public static class pagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 6;

        pagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AboutFragment.newInstance();
                case 1:
                    return RegistrationFragment.newInstance();
                case 2:
                    return PersonalPackagesFragment.newInstance();
                case 3:
                    return CorporatePackagesFragment.newInstance();
                case 4:
                    return HelpFragment.newInstance();
                case 5:
                    return ContactUsFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "About Us";
                case 1:
                    return "Registration";
                case 2:
                    return "Personal Packages";
                case 3:
                    return "Corporate Packages";
                case 4:
                    return "Help";
                case 5:
                    return "Contact Us";
                default:
                    return "Page " + position;
            }
        }
    }
}
