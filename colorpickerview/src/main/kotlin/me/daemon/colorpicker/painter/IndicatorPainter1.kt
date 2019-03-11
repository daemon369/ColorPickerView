package me.daemon.colorpicker.painter

import android.graphics.Canvas
import me.daemon.colorpicker.view.PaletteView

/**
 * indicator painter
 *
 * 颜色选择指示器绘制
 *
 * @author daemon
 * @since 2019-03-01 19:29
 */
interface IndicatorPainter1 {

    /**
     * draw indicator
     *
     * 绘制指示器
     *
     * @param paletteView palette view
     *                    颜色选择View
     * @param canvas      canvas for indicator
     *                    画布
     * @param color       current color
     *                    当前颜色
     * @param isChanging  whether color is changing, true when finger down and false when finger up
     *                    颜色是否在变化中，手指按下到抬起之前的过程属于变化中
     *
     * @return            true if the painter has handled drawing, false otherwise
     */
    fun drawIndicator(
            paletteView: PaletteView,
            canvas: Canvas,
            color: Int,
            isChanging: Boolean
    ): Boolean
}
