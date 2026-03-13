package com.sample.plantleafdiseasedetectionapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HomePageActivity extends BaseActivity {

    Button btn_realtime, btn_upload;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        checkAllPermission();

        btn_realtime = findViewById(R.id.btn_realtime);
        btn_upload = findViewById(R.id.btn_upload);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectingToInternet()){
                    noInternetAlertDialog();
                    return;
                }
                Intent intent = new Intent(HomePageActivity.this,UploadActivity.class);
                startActivity(intent);
            }
        });

        btn_realtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnectingToInternet()){
                    noInternetAlertDialog();
                    return;
                }
                Intent intent = new Intent(HomePageActivity.this,RealTimeDetectionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkAllPermission(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case PERMISSION_WRITE_EXTERNAL_STORAGE:
                case PERMISSION_READ_EXTERNAL_STORAGE:
                    checkAllPermission();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
