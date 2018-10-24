package com.sklein.ubimo_pager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    public static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.VIBRATE
    };
    private static final int REQUEST_CODE = 13;
    public SharedPreferences sharedPref;
    public  SharedPreferences.Editor editor;
    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

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
        List<String> permissionsToAsk = new ArrayList<String>();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        optionsMenu = menu;

        setMenuVisibility();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disable:
                final Dialog d = new Dialog(this);
                d.setContentView(R.layout.dialog);

                Button b = (Button) d.findViewById(R.id.disable_button);
                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
                np.setMaxValue(120); // If you want to disable for more than 2 hours just quit your job man...
                np.setMinValue(1);
                np.setValue(20);
                np.setWrapSelectorWheel(false);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDisableMode(true);

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                setDisableMode(false);
                            }
                        }, 60000 * np.getValue());

                        d.dismiss();
                    }
                });
                d.show();
                return true;
            case R.id.enable:
                setDisableMode(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMenuVisibility() {
        boolean disabled = sharedPref.getBoolean(getString(R.string.disabled), false);
        MenuItem d = optionsMenu.findItem(R.id.disable);
        d.setVisible(!disabled);

        MenuItem e = optionsMenu.findItem(R.id.enable);
        e.setVisible(disabled);
    }

    private void setDisableMode(boolean isDisabled) {
        editor.putBoolean(getString(R.string.disabled), isDisabled);
        editor.commit();

        setMenuVisibility();
    }
}
