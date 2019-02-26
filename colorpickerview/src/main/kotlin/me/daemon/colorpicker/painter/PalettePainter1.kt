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
     * @param color       current color
     * @param isChanging  whether PaletteView is changing
     */
    fun onDraw(
            paletteView: PaletteView,
            canvas: Canvas,
            color: Int,
            isChanging: Boolean
    ) {
        onDrawPalette(
                paletteView,
                canvas,
                isChanging
        )

        onDrawIndicator(
                paletteView,
                canvas,
                color,
                isChanging
        )
    }

    fun onDrawPalette(
            paletteView: PaletteView,
            canvas: Canvas,
            isChanging: Boolean
    )

    /**
     * draw indicator
     *
     * 绘制指示器
     *
     * @param paletteView PaletteView
     *                    颜色选择View
     * @param canvas      canvas for indicator
     *                    画布
     * @param color       current color
     *                    当前颜色
     * @param isChanging  whether color is changing
     *                    颜色是否在变化中，手指按下到抬起之前的过程属于变化中
     */
    fun onDrawIndicator(
            paletteView: PaletteView,
            canvas: Canvas,
            color: Int,
            isChanging: Boolean
    ) {
    }

    /**
     * update palette value based on touch event coordinate
     *
     * @param paletteView PaletteView
     * @param x           touch event x
     * @param y           touch event y
     * @param propagate   whether propagate this update
     */
    fun onUpdate(
            paletteView: PaletteView,
            x: Float,
            y: Float,
            propagate: Boolean
    )

}