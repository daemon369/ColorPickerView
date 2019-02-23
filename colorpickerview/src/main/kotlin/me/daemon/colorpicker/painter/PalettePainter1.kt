package me.daemon.colorpicker.painter

import android.graphics.Canvas
import me.daemon.colorpicker.PaletteView

/**
 * @author daemon
 * @since 2019-02-23 23:35
 */
interface PalettePainter1 {

    /**
     * on palette view size changed
     *
     * @param paletteView PaletteView
     * @param w           width of this paletteView
     * @param h           height of this paletteView
     */
    fun onSizeChanged(
            paletteView: PaletteView,
            w: Int,
            h: Int
    )

    /**
     * draw palette
     *
     * 绘制调色板
     *
     * @param paletteView PaletteView
     * @param canvas      canvas to draw
     */
    fun drawPalette(
            paletteView: PaletteView,
            canvas: Canvas
    )

    /**
     * update palette value based on touch event coordinate
     *
     * @param paletteView PaletteView
     * @param x           touch event x
     * @param y           touch event y
     */
    fun update(
            paletteView: PaletteView,
            x: Float,
            y: Float
    )

}