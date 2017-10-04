package com.sklein.ubimo_pager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {
    public static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
    };
    private static final int REQUEST_CODE = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final Button btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        checkForPermission(NEEDED_PERMISSIONS);
    }

    private void checkForPermission(String[] permissions) {
        ArrayList<String> permissionsToAsk = new ArrayList<String>();
        for (String permission : permissions) {
            int permissionStatus = ContextCompat.checkSelfPermission(this, permission);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                permissionsToAsk.add(permission);
            }
        }

        if (!permissionsToAsk.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsToAsk.toArray(new String[permissionsToAsk.size()]), REQUEST_CODE);
        }
    }
}
