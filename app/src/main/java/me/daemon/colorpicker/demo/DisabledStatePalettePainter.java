package me.daemon.colorpicker.demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import me.daemon.colorpicker.painter.impl.DefaultPalettePainter;
import me.daemon.colorpicker.view.PaletteView;

/**
 * @author daemon
 * @since 2019-02-11 22:07
 */
public class DisabledStatePalettePainter extends DefaultPalettePainter {

    private final Paint disabledPaint;

    public DisabledStatePalettePainter() {
        this(Color.GRAY);
    }

    public DisabledStatePalettePainter(final int disabledColor) {
        disabledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        disabledPaint.setColor(disabledColor);
    }

    @Override
    public void onDrawPalette(
            @NonNull final PaletteView paletteView,
            @NonNull final Canvas canvas,
            boolean isChanging
    ) {
        super.onDrawPalette(paletteView, canvas, isChanging);
        if (paletteView.isEnabled()) {
            super.onDrawPalette(paletteView, canvas, isChanging);
        } else {
            canvas.drawCircle(getPaletteCenterX(), getPaletteCenterY(), getPaletteRadius(), disabledPaint);
        }
    }
}
