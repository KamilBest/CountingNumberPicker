package com.best.countingnumberpicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CountingNumberPicker countingNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countingNumberPicker=findViewById(R.id.customView);
        countingNumberPicker.addCustomClickListener(new CustomClickListener() {
            @Override
            public void onPlusClick() {
                Toast.makeText(getApplicationContext(), "PLUS",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMinusClick() {
                Toast.makeText(getApplicationContext(), "MINUS",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
