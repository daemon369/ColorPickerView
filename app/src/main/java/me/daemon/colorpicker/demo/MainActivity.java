package me.daemon.colorpicker.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import me.daemon.colorpicker.ColorObserver;
import me.daemon.colorpicker.ColorPickerView;

public class MainActivity extends AppCompatActivity {

    private ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorPickerView = findViewById(R.id.color_picker);
        colorPickerView.subscribe(colorObserver);
        colorPickerView.setIndicatorPainter(new ShowPopIndicator());
        colorPickerView.setSquare(true);
        colorPickerView.setPalettePainter(new DisabledStatePalettePainter(Color.LTGRAY));

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerView.setEnabled(!colorPickerView.isEnabled());
            }
        });

        colorPickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity", "onClick: " + colorPickerView.getColor());
            }
        });
    }

    @Override
    protected void onDestroy() {
        colorPickerView.unsubscribe(colorObserver);
        super.onDestroy();
    }

    private final ColorObserver colorObserver = new ColorObserver() {
        @Override
        public void onColor(int color) {
            Log.e("MainActivity", "onColor: " + color + " " + String.format("#%06X", (0xFFFFFF & color)));
        }
    };
}
