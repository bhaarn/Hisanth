package com.padhuga.hishanth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Utils {
    private Activity activity;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    Utils(Context context){
        activity = (Activity) context;
    }

    protected boolean checkAndRequestPermissions() {
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

    protected void callNumber() {
        if (MainActivity.all_permission) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(activity.getResources().getString(R.string.static_tel) + activity.getResources().getString(R.string.static_mobile_number)));
            activity.startActivity(callIntent);
        }
    }

    protected void sendEmail(String details) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{activity.getResources().getString(R.string.static_email_id)});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, activity.getString(R.string.static_mail_subject));
        if(!details.equals("")) {
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, details);
        }
        try {
            activity.startActivity(Intent.createChooser(emailIntent,activity.getString(R.string.static_mail_select_title)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, activity.getString(R.string.static_mail_error), Toast.LENGTH_SHORT).show();
        }
        sendSMSMessage(details);
    }

    private void sendSMSMessage(String details) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
        if(MainActivity.all_permission) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getResources().getString(R.string.static_sms) + activity.getResources().getString(R.string.static_mobile_number)));
            intent.putExtra(Constants.ARG_SMS_BODY, details);
            activity.startActivity(intent);
        }
    }

    protected void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(activity.getResources().getString(android.R.string.ok), okListener)
                .setNegativeButton(activity.getResources().getString(android.R.string.cancel), okListener)
                .create()
                .show();
    }

    protected void updateLabel(Calendar myCalendar, EditText editText) {
        SimpleDateFormat sdf = new SimpleDateFormat(activity.getResources().getString(R.string.simple_date_format), Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    protected void showAlert(String title, String message, String positiveButton, String negetiveButton) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        alertPositiveButtonOnClick(activity.getResources().getString(R.string.alert_location_module_name));
                    }
                })
                .setNegativeButton(negetiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        alertNegativeButtonOnClick(activity.getResources().getString(R.string.alert_location_module_name));
                    }
                });
        dialog.show();
    }

    private void alertPositiveButtonOnClick(String parent) {
        switch (parent) {
            case "location":
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(myIntent);
                break;
        }
    }

    private void alertNegativeButtonOnClick(String parent) {
        switch (parent) {
            case "location":
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(myIntent);
                break;
        }
    }
}