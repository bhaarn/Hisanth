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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.padhuga.hishanth.R;
import com.padhuga.hishanth.utils.Utils;

import java.util.Calendar;

public class CorporatePackagesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner spinner1;

    LinearLayout linearLayout1;

    TextView textView1;

    EditText editText1;
    EditText editText2;

    Button button1;
    Button button2;

    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;

    String selectedService;

    Utils utils;

    public static CorporatePackagesFragment newInstance() {
        return new CorporatePackagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_corporate_packages, container, false);

        myCalendar = Calendar.getInstance();
        utils = new Utils(getActivity());

        spinner1 = rootView.findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, getActivity().getResources().getStringArray(R.array.corporate_services_array));
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setBackgroundResource(R.drawable.spinner_text_selection_style);
        spinner1.setAdapter(spinnerArrayAdapter1);
        spinner1.setOnItemSelectedListener(this);
        linearLayout1 = rootView.findViewById(R.id.linearLayout1);
        textView1 = rootView.findViewById(R.id.textView1);
        editText1 = rootView.findViewById(R.id.editText1);
        editText2 = rootView.findViewById(R.id.editText2);
        button1 = rootView.findViewById(R.id.button1);
        button1.setOnClickListener(viewClickListener);
        button2 = rootView.findViewById(R.id.button2);
        button2.setOnClickListener(viewClickListener);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                utils.updateLabel(myCalendar, editText2);
            }
        };
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
        if (editText1.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.place_value_error, Toast.LENGTH_SHORT).show();
        } else if (editText2.getText().toString().matches("")) {
            Toast.makeText(getActivity(), R.string.date_error, Toast.LENGTH_SHORT).show();
        } else {
            String details = "Service : " + selectedService + "\n" + "Total Count : " + editText1.getText().toString() + "\n" + "Event Date : " + editText2.getText().toString() + "\n";
            utils.showPrice(details, getActivity().getResources().getString(R.string.alert_corporate_services_module_name));
        }
    }

    private void clearFields() {
        editText1.setText("");
        editText2.setText("");
        linearLayout1.setVisibility(View.GONE);
        spinner1.setSelection(0);
    }

    private void manipulateService(String selectedService) {
        switch (selectedService) {
            case "School ID Card Photo Shoot":
            case "College ID Card Photo Shoot":
                textView1.setText(getActivity().getResources().getString(R.string.number_of_students));
                editText1.setHint(getActivity().getResources().getString(R.string.number_of_students_hint));
                linearLayout1.setVisibility(View.VISIBLE);
                break;
            case "Company Employee Card Photo Shoot":
                textView1.setText(getActivity().getResources().getString(R.string.number_of_employees));
                editText1.setText(getActivity().getResources().getString(R.string.number_of_employees_hint));
                linearLayout1.setVisibility(View.VISIBLE);
                break;
            case "Government Specific Cards Photo Shoot":
                textView1.setText(getActivity().getResources().getString(R.string.number_of_people));
                editText1.setText(getActivity().getResources().getString(R.string.number_of_people_hint));
                linearLayout1.setVisibility(View.VISIBLE);
                break;
            case "Visiting Cards Printing":
                textView1.setText(getActivity().getResources().getString(R.string.number_of_cards));
                editText1.setText(getActivity().getResources().getString(R.string.number_of_cards_hint));
                linearLayout1.setVisibility(View.VISIBLE);
                break;
            case "Flex Banners":
                textView1.setText(getActivity().getResources().getString(R.string.number_of_banners));
                editText1.setText(getActivity().getResources().getString(R.string.number_of_banners_hint));
                linearLayout1.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
