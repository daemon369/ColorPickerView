package me.daemon.colorpicker.painter

import android.graphics.Canvas

import me.daemon.colorpicker.ColorPickerView

/**
 * @author daemon
 * @since 2019-02-11 20:21
 */
interface PalettePainter {

    /**
     * @param w       Current width of this view.
     * @param h       Current height of this view.
     * @param radius  palette radius
     * @param centerX palette center x
     * @param centerY palette center y
     */
    fun onSizeChanged(
            w: Int,
            h: Int,
            radius: Int,
            centerX: Int,
            centerY: Int
    )

    /**
     * draw palette
     *
     *
     * 绘制调色板
     * @param colorPickerView ColorPickerView
     * @param canvas          canvas to draw
     * @param radius          palette radius
     * @param centerX         palette center x
     * @param centerY         palette center y
     */
    fun drawPalette(
            colorPickerView: ColorPickerView,
            canvas: Canvas,
            radius: Int, centerX: Int,
            centerY: Int

    )

}
