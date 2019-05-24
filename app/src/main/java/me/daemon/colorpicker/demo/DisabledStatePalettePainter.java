package me.daemon.colorpicker.demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import me.daemon.colorpicker.view.PaletteView;
import me.daemon.colorpickerview.palette.PalettePiePainter;

/**
 * @author daemon
 * @since 2019-02-11 22:07
 */
public class DisabledStatePalettePainter extends PalettePiePainter {

    private final Paint disabledPaint;

    private final Paint indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final float innerSize1 = 33;
    private final float outerSize1 = 45;

    private final float innerSize2 = 46.5f;
    private final float outerSize2 = 63;

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

    @Override
    public void onDrawIndicator(
            @NonNull final PaletteView paletteView,
            @NonNull final Canvas canvas,
            int color,
            boolean isChanging
    ) {
        final float x = getCurrentPoint().x;
        final float y = getCurrentPoint().y;

        indicatorPaint.setColor(Color.WHITE);

        canvas.drawCircle(x, y, outerSize1, indicatorPaint);

        if (paletteView.isEnabled()) {
            if (isChanging) {
                canvas.drawCircle(x, y - 123, outerSize2, indicatorPaint);
            }

            indicatorPaint.setColor(color);

            canvas.drawCircle(x, y, innerSize1, indicatorPaint);

            if (isChanging) {
                canvas.drawCircle(x, y - 123, innerSize2, indicatorPaint);
            }
        } else {

            indicatorPaint.setColor(Color.GRAY);

            canvas.drawCircle(x, y, innerSize1, indicatorPaint);
        }
    }
}
