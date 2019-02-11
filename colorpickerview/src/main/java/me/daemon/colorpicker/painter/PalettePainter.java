package me.daemon.colorpicker.painter;

import android.graphics.Canvas;

import me.daemon.colorpicker.ColorPickerView;

/**
 * @author daemon
 * @since 2019-02-11 20:21
 */
public interface PalettePainter {

    /**
     * @param w       Current width of this view.
     * @param h       Current height of this view.
     * @param radius  palette radius
     * @param centerX palette center x
     * @param centerY palette center y
     */
    void onSizeChanged(
            final int w,
            final int h,
            final int radius,
            final int centerX,
            final int centerY
    );

    /**
     * draw palette
     * <p>
     * 绘制调色板
     *  @param colorPickerView ColorPickerView
     * @param canvas          canvas to draw
     * @param radius          palette radius
     * @param centerX         palette center x
     * @param centerY         palette center y
     */
    void drawPalette(
            final ColorPickerView colorPickerView,
            final Canvas canvas,
            final int radius, final int centerX,
            final int centerY

    );

}
