package com.example.arch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.arch.onepixel.OnePixelService;

/**
 * 入口app
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startOnePixelService();
    }

    private void startOnePixelService() {
        Intent intent = new Intent(MainActivity.this, OnePixelService.class);
        startService(intent);
    }
}
