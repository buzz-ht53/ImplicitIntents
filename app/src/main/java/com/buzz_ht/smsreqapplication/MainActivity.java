package com.buzz_ht.smsreqapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    EditText editTextPhone, editTextSms, editTextNumber, editTextTo, editTextSub, editTextMsg;
    Button buttonSendEmail, buttonCall, buttonSendSms;

    //static final int PERMISSION_REQUEST


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

        buttonSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int smsPermissions = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);
                if (smsPermissions == PackageManager.PERMISSION_GRANTED) {
                    sendSms();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 2);
                }
            }
        });

    }

    void init() {
        editTextPhone = findViewById(R.id.edittextphone);
        editTextSms = findViewById(R.id.edittextsms);
        editTextNumber = findViewById(R.id.edittextnumber);
        editTextTo = findViewById(R.id.edittextto);
        editTextSub = findViewById(R.id.edittextsub);
        editTextMsg = findViewById(R.id.edittextmsg);

        buttonCall = findViewById(R.id.buttoncall);
        buttonSendEmail = findViewById(R.id.buttonsendemail);
        buttonSendSms = findViewById(R.id.buttonsendsms);

    }

    private void sendEmail() {

        String To = editTextTo.getText().toString().trim();
        String SUB = editTextSub.getText().toString().trim();
        String MSG = editTextMsg.getText().toString().trim();


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{To});
        i.putExtra(Intent.EXTRA_SUBJECT, SUB);
        i.putExtra(Intent.EXTRA_TEXT, MSG);
        i.setType("text/plain");
        try {
            startActivity(Intent.createChooser(i, "Choose Mail"));
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void sendSms() {

        String number = editTextPhone.getText().toString().trim();
        String message = editTextSms.getText().toString().trim();
        if (!number.equals("") && !message.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            editTextPhone.setText("");
            editTextSms.setText("");
            Toast.makeText(MainActivity.this, "Text Sent", Toast.LENGTH_SHORT).show();
        }

    }

    public void makeCall() {
        String number = editTextNumber.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have sms permission...", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms();
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have sms permission...", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }


}