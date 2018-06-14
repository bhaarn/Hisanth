package com.padhuga.hishanth.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.padhuga.hishanth.R;
import com.padhuga.hishanth.utils.Utils;

import java.util.Calendar;

public class PersonalPackagesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner spinner1;
    LinearLayout linearLayout1;
    TextView textView1;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    Button button1;
    Button button2;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    String selectedService;
    Utils utils;

    public static PersonalPackagesFragment newInstance() {
        return new PersonalPackagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_packages, container, false);
        myCalendar = Calendar.getInstance();
        utils = new Utils(getActivity());
        spinner1 = rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, getActivity().getResources().getStringArray(R.array.personal_services_array));
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setBackgroundResource(R.drawable.spinner_text_selection_style);
        spinner1.setAdapter(spinnerArrayAdapter1);
        spinner1.setOnItemSelectedListener(this);
        linearLayout1 = rootView.findViewById(R.id.linearLayout1);
        textView1 = rootView.findViewById(R.id.textView1);
        editText1 = rootView.findViewById(R.id.editText1);
        editText2 = rootView.findViewById(R.id.editText2);
        editText2.setOnClickListener(viewClickListener);
        editText3 = rootView.findViewById(R.id.editText3);
        editText4 = rootView.findViewById(R.id.editText4);
        editText5 = rootView.findViewById(R.id.editText5);
        button1 = rootView.findViewById(R.id.button1);
        button1.setOnClickListener(viewClickListener);
        button2 = rootView.findViewById(R.id.button2);
        button2.setOnClickListener(viewClickListener);
        date = utils.setDate(myCalendar, editText2);
        return rootView;
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editText2:
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
                selectedService = adapterView.getItemAtPosition(position).toString();
                manipulateService(selectedService);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void validationCheck() {
        if (editText1.getText().toString().matches("") && editText1.getVisibility() == View.VISIBLE) {
            Toast.makeText(getActivity(), R.string.place_value_error, Toast.LENGTH_SHORT).show();
        } else if (editText2.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.event_date_error, Toast.LENGTH_SHORT).show();
        } else if (editText3.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.venue_error, Toast.LENGTH_SHORT).show();
        } else if (editText4.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.mobile_error, Toast.LENGTH_SHORT).show();
        } else if (editText5.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.email_error, Toast.LENGTH_SHORT).show();
        } else if (editText4.getText().toString().trim().length() < 10) {
            Toast.makeText(getActivity(), R.string.mobile_validate_error, Toast.LENGTH_SHORT).show();
        } else if (!utils.isEmailValid(editText5.getText().toString())) {
            Toast.makeText(getActivity(), R.string.email_validate_error, Toast.LENGTH_SHORT).show();
        } else {
            String details;
            if (editText1.getVisibility() == View.VISIBLE) {
                details = "Service : " + selectedService + "\n" + "Total Count : " + editText1.getText().toString() + "\n" + "Event Date : " + editText2.getText().toString() + "\n" +
                        "Venue : " + editText2.getText().toString() + "\n" + "MobileNumber : " + editText4.getText().toString() + "\n"
                        + "Email ID : " + editText5.getText().toString() + "\n";
            } else {
                details = "Service : " + selectedService + "\n" + "Event Date : " + editText2.getText().toString() + "\n" +
                        "Venue : " + editText2.getText().toString() + "\n" + "MobileNumber : " + editText4.getText().toString() + "\n"
                        + "Email ID : " + editText5.getText().toString() + "\n";
            }
            utils.showPrice(details, getActivity().getResources().getString(R.string.alert_corporate_services_module_name));
        }
    }

    private void clearFields() {
        editText1.setText("");
        editText2.setText("");
        editText3.setText("");
        editText4.setText("");
        editText5.setText("");
        linearLayout1.setVisibility(View.GONE);
        spinner1.setSelection(0);
    }

    private void manipulateService(String selectedService) {
        switch (selectedService) {
            default:
                linearLayout1.setVisibility(View.VISIBLE);
                break;
        }
    }
}
