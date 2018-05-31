package com.padhuga.hishanth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {
    private Activity activity;
    private static final int REQUEST_READ_PHONE_STATE = 0;

    Utils(Context context){
        activity = (Activity) context;
    }

    protected void callNumber() {
        if (MainActivity.all_permission) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + activity.getResources().getString(R.string.static_mobile_number)));
            activity.startActivity(callIntent);
        }
    }

    protected void sendEmail() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{activity.getResources().getString(R.string.static_email_id)});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hisanth Photography Contact Us Query");
        try {
            activity.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void sendEmail(String details) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{activity.getResources().getString(R.string.static_email_id)});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hisanth Photography Registration Query");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, details);

        try {
            activity.startActivity(Intent.createChooser(emailIntent,"Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
        sendSMSMessage(details);
    }

    private void sendSMSMessage(String details) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
        if(MainActivity.all_permission) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + activity.getResources().getString(R.string.static_mobile_number)));
            intent.putExtra("sms_body", details);
            activity.startActivity(intent);
        }
    }

    protected void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    protected void updateLabel(Calendar myCalendar, EditText editText) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }
}
