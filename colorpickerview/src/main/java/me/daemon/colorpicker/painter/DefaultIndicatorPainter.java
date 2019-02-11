package me.daemon.colorpicker.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import me.daemon.colorpicker.ColorPickerView;

/**
 * @author daemon
 * @since 2019-02-09 22:29
 */
public class DefaultIndicatorPainter implements IndicatorPainter {

    private final Paint indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void drawIndicator(
            final ColorPickerView colorPickerView,
            final Canvas canvas,
            final PointF point,
            final int color,
            final boolean isChanging
    ) {

        indicatorPaint.setColor(color);
        indicatorPaint.setStrokeWidth(2);
        canvas.drawLine(
                point.x - colorPickerView.getPalettePadding(),
                point.y,
                point.x + colorPickerView.getPalettePadding(),
                point.y,
                indicatorPaint);
        canvas.drawLine(
                point.x,
                point.y - colorPickerView.getPalettePadding(),
                point.x,
                point.y + colorPickerView.getPalettePadding(),
                indicatorPaint);
    }
}
