package me.daemon.colorpicker.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import me.daemon.colorpicker.ColorObserver;
import me.daemon.colorpicker.ColorPickerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ColorPickerView colorPickerView = findViewById(R.id.color_picker);
        colorPickerView.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color) {
                Log.e("MainActivity", "onColor: " + color + " " + String.format("#%06X", (0xFFFFFF & color)));
            }
        });
        colorPickerView.setIndicatorPainter(new ShowPopIndicator());

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerView.setEnabled(!colorPickerView.isEnabled());
            }
        });
    }
}
