package com.padhuga.hishanth.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.padhuga.hishanth.activities.AboutFragment;
import com.padhuga.hishanth.activities.ContactUsFragment;
import com.padhuga.hishanth.activities.CorporatePackagesFragment;
import com.padhuga.hishanth.activities.CustomizedPackagesFragment;
import com.padhuga.hishanth.activities.HelpFragment;
import com.padhuga.hishanth.activities.PersonalPackagesFragment;
import com.padhuga.hishanth.activities.RegistrationFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 7;

    public PagerAdapter(FragmentManager fragmentManager) {
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
                return CustomizedPackagesFragment.newInstance();
            case 5:
                return HelpFragment.newInstance();
            case 6:
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
                return "Customized Packages";
            case 5:
                return "Help / FAQ";
            case 6:
                return "Contact Us";
            default:
                return "Page " + position;
        }
    }
}
