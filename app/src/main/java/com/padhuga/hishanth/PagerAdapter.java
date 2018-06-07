package com.padhuga.hishanth;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 6;

    PagerAdapter(FragmentManager fragmentManager) {
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
