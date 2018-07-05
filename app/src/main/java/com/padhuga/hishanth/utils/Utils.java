package com.padhuga.hishanth.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.padhuga.hishanth.R;
import com.padhuga.hishanth.activities.MainActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Utils {
    private Activity activity;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public Utils(Context context) {
        activity = (Activity) context;
    }

    public boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);
        int permissionCallPhone = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        int permissionGetCoarseLocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionGetFineLocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCallPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permissionGetCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionGetFineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void callNumber(String mobileNumber) {
        if (MainActivity.all_permission) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(activity.getResources().getString(R.string.static_tel) + mobileNumber));
            activity.startActivity(callIntent);
        }
    }

    public void sendEmail(String details) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{activity.getResources().getString(R.string.static_email_id)});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, activity.getString(R.string.static_mail_subject));
        if (!details.equals("")) {
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, details);
        }
        try {
            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.static_mail_select_title)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, activity.getString(R.string.static_mail_error), Toast.LENGTH_SHORT).show();
        }
        if (!details.equals("")) {
            sendSMSMessage(details);
        }
    }

    private void sendSMSMessage(String details) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
        if (MainActivity.all_permission) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getResources().getString(R.string.static_sms) + activity.getResources().getString(R.string.static_mobile_number1)));
            intent.putExtra(Constants.ARG_SMS_BODY, details);
            activity.startActivity(intent);
        }
    }

    public void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(activity.getResources().getString(android.R.string.ok), okListener)
                .setNegativeButton(activity.getResources().getString(android.R.string.cancel), okListener)
                .create()
                .show();
    }

    public void updateLabel(Calendar myCalendar, EditText editText) {
        SimpleDateFormat sdf = new SimpleDateFormat(activity.getResources().getString(R.string.simple_date_format), Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    public void showAlert(String title, String message, String positiveButton, String negativeButton, final String parent, final String details, final String packageType) {
        final View view = activity.getLayoutInflater().inflate(R.layout.alert_registration_price_details, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title)
                .setMessage(message)
                .setView(R.layout.activity_main)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        alertPositiveButtonOnClick(parent, details);
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        alertNegativeButtonOnClick(parent);
                    }
                });
        if (!details.equals("")) {
            dialog.setView(view);
            calculatePrice(view, packageType);
        }
        dialog.show();
    }

    private void alertPositiveButtonOnClick(String parent, String details) {
        switch (parent) {
            case "location":
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(myIntent);
                break;
            case "registration":
            case "corporate_packages":
            case "personal_packages":
            case "customized_packages":
                sendEmail(details);
                break;
        }
    }

    private void alertNegativeButtonOnClick(String parent) {
        switch (parent) {
            case "location":
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(myIntent);
                break;
            case "registration":
            case "corporate_packages":
                break;
        }
    }

    public void openFacebook(String pageName) {
        // String YourPageURL = "https://www.facebook.com/n/?"+pageName;
        String YourPageURL = "https://www.facebook.com/" + "scrimmagesuren";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
        activity.startActivity(browserIntent);
    }

    public void openWhatsapp(String whatsappContact) {
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Enquiry");
            sendIntent.putExtra("jid", whatsappContact + "@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(activity, "Error\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openYoutube(String youtubeChannel) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.youtube.com/channel/" + youtubeChannel));
        activity.startActivity(intent);
    }

    public void showPrice(String details, String moduleName, String packageType) {
        showAlert(activity.getResources().getString(R.string.alert_registration_submit_title), activity.getResources().getString(R.string.alert_registration_submit_message),
                activity.getResources().getString(R.string.alert_registration_submit_positive_button), activity.getResources().getString(android.R.string.cancel),
                moduleName, details, packageType);
    }

    private void calculatePrice(View view, String packageType) {
        TextView price = view.findViewById(R.id.price);
        TextView tax = view.findViewById(R.id.tax);
        TextView discount = view.findViewById(R.id.discount);
        TextView final_price = view.findViewById(R.id.final_price);
        switch (packageType) {
            case "Traditional Package":
                price.setText("50000.00");
                tax.setText("256.00");
                discount.setText("0.00");
                final_price.setText("50256.00");
                break;
            case "Premium Package":
                price.setText("75000.00");
                tax.setText("256.00");
                discount.setText("0.00");
                final_price.setText("75256.00");
                break;
            case "Personal Package":
                price.setText("50000.00");
                tax.setText("256.00");
                discount.setText("0.00");
                final_price.setText("50256.00");
                break;
            case "Corporate Package":
                price.setText("75000.00");
                tax.setText("256.00");
                discount.setText("0.00");
                final_price.setText("75256.00");
                break;
            case "Customized Package":
                price.setText("20000.00");
                tax.setText("256.00");
                discount.setText("0.00");
                final_price.setText("20256.00");
                break;
            default:
                price.setText("75000.00");
                tax.setText("256.00");
                discount.setText("0.00");
                final_price.setText("75256.00");
                break;
        }
    }

    public DatePickerDialog.OnDateSetListener setDate(final Calendar myCalendar, final EditText editText7) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, editText7);
            }
        };
        return date;
    }

    public void dateSetter(DatePickerDialog.OnDateSetListener date, Calendar myCalendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(activity), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());
        StringBuilder address;
        String finalAddress;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                address = new StringBuilder();
                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    address.append(addresses.get(0).getAddressLine(i)).append("\n");
                }
                finalAddress = address.toString();
            } else {
                finalAddress = "No Address Returned";
            }
        } catch (IOException e) {
            e.printStackTrace();
            finalAddress = "Cannot Find Address";
        }
        return finalAddress;
    }

    public boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
