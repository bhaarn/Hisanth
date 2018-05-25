package com.padhuga.hishanth;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    EditText editText7;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Button button1;
    Button button2;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    String selectedCity;
    String selectedService;
    String selectedDeliveryMode;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    String details;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        myCalendar = Calendar.getInstance();

        editText1 = rootView.findViewById(R.id.editText1);
        editText2 = rootView.findViewById(R.id.editText2);
        editText3 = rootView.findViewById(R.id.editText3);
        editText4 = rootView.findViewById(R.id.editText4);
        editText5 = rootView.findViewById(R.id.editText5);
        editText6 = rootView.findViewById(R.id.editText6);
        editText7 = rootView.findViewById(R.id.editText7);
        editText7.setOnClickListener(viewClickListener);

        spinner1 = rootView.findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(this);
        spinner2 = rootView.findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(this);
        spinner3 = rootView.findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener(this);

        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        button1.setOnClickListener(viewClickListener);
        button2.setOnClickListener(viewClickListener);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, editText7);
            }
        };

        return rootView;
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editText7:
                    DatePickerDialog datePickerDialog;
                    datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                    break;

                case R.id.button1:
                    if (editText1.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), R.string.name_error, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editText2.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), R.string.address_error, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editText3.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), R.string.locality_error, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editText4.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), R.string.pincode_error, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editText5.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), R.string.mobile_error, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editText6.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), R.string.email_error, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editText7.getText().toString().matches("")) {
                        Toast.makeText(getActivity(), R.string.date_error, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        details = "Name : " + editText1.getText().toString() + "\n" + "Address : " + editText2.getText().toString() + "\n" + "Locality : " + editText3.getText().toString() +
                                "\n" + "City : " + selectedCity + "\n" + "PinCode : " + editText4.getText().toString() + "\n" + "MobileNumber : " + editText5.getText().toString() + "\n"
                                + "Email ID : " + editText6.getText().toString() + "\n" + "Service : " + selectedService + "\n" + "Function Date : " + editText7.getText().toString() + "\n"
                                + "Mode of Delivery : " + selectedDeliveryMode + "\n";
                        sendEmail(details);
                        Log.d("Bharani", details);
                    }
                    break;

                case R.id.button2:
                    editText1.setText("");
                    editText2.setText("");
                    editText3.setText("");
                    editText4.setText("");
                    editText5.setText("");
                    editText6.setText("");
                    editText7.setText("");
                    spinner1.setSelection(0);
                    spinner2.setSelection(0);
                    spinner3.setSelection(0);
                    break;
            }
        }
    };

    private void updateLabel(Calendar myCalendar, EditText editText) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    protected void sendSMSMessage(String details) {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {

        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            String phoneNumber = "+919790495863";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
            intent.putExtra("sms_body", details);
            startActivity(intent);
            Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
        }
    }

    protected void sendEmail(String details) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"hisanthphotography@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hisanth Photography Registration Query");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, details);

        try {
            startActivity(Intent.createChooser(emailIntent,
                    "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "No email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
        sendSMSMessage(details);
    }

@Override
public void onItemSelected(AdapterView<?> adapterView,View view,int position,long id){
        Spinner spinner=(Spinner)adapterView;
        switch(spinner.getId()){
        case R.id.spinner1:
        selectedCity=adapterView.getItemAtPosition(position).toString();
        break;
        case R.id.spinner2:
        selectedService=adapterView.getItemAtPosition(position).toString();
        break;
        case R.id.spinner3:
        selectedDeliveryMode=adapterView.getItemAtPosition(position).toString();
        break;
        }
        }

@Override
public void onNothingSelected(AdapterView<?> adapterView){

        }
        }
