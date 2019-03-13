//package me.daemon.colorpicker.demo;
//
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//
//import me.daemon.colorpicker.ColorPickerView;
//import me.daemon.colorpicker.painter.impl.DefaultPalettePainter;
//
///**
// * @author daemon
// * @since 2019-02-11 22:07
// */
//public class DisabledStatePalettePainter extends DefaultPalettePainter {
//
//    private final Paint disabledPaint;
//
//    public DisabledStatePalettePainter() {
//        this(Color.GRAY);
//    }
//
//    public DisabledStatePalettePainter(final int disabledColor) {
//        disabledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        disabledPaint.setColor(disabledColor);
//    }
//
//    @Override
//    public void drawPalette(ColorPickerView colorPickerView, Canvas canvas, int radius, int centerX, int centerY) {
//        if (colorPickerView.isEnabled()) {
//            super.drawPalette(colorPickerView, canvas, radius, centerX, centerY);
//        } else {
//            canvas.drawCircle(centerX, centerY, radius, disabledPaint);
//        }
//    }
//}
