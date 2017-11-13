package com.example.birat.Docassist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartScreen extends Activity {

    Button buttonProceed;
    Button buttonEmerg;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        buttonEmerg=(Button) findViewById(R.id.emergency);
        buttonProceed = (Button) findViewById(R.id.proceedButton);
        textView = (TextView) findViewById(R.id.connection);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();{
        if (networkInfo != null && networkInfo.isConnected()) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setText("Connect to wifi or mobile data.");
            buttonProceed.setEnabled(false);
        }

    }}

    public void Proceed(View view) {
        Intent intent = new Intent(this, loginAsDoctor.class);
        startActivity(intent);
    }
    public void emergency_onClick(View view) {
        String number = "911";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
}}
