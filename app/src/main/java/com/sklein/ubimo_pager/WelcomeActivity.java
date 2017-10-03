package com.sklein.ubimo_pager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final Button button = (Button) findViewById(R.id.btn_close);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        checkForPermission(Manifest.permission.RECEIVE_SMS);
        checkForPermission(Manifest.permission.READ_SMS);
    }

    private void checkForPermission(String permission) {
        final int permissionStatus = ContextCompat.checkSelfPermission(this, permission);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
    }
}
