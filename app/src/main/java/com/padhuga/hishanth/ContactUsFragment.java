package com.padhuga.hishanth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactUsFragment extends Fragment {
    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        TextView mobileNumber = rootView.findViewById(R.id.mobile_number);
        mobileNumber.setOnClickListener(viewClickListener);
        TextView emailId = rootView.findViewById(R.id.email_id);
        emailId.setOnClickListener(viewClickListener);
        return rootView;
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Utils utils = new Utils(getActivity());
            switch (v.getId()) {
                case R.id.mobile_number:
                    utils.callNumber();
                    break;
                case R.id.email_id:
                    utils.sendEmail("");
                    break;
                default:
                    break;
            }
        }
    };
}
