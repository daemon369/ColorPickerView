package me.daemon.colorpicker.painter

import android.graphics.Canvas
import me.daemon.colorpicker.view.PaletteView
import me.daemon.colorpicker.view.PaletteView.PaletteValue

/**
 * @author daemon
 * @since 2019-02-23 23:35
 */
interface PalettePainter : IPainter<PaletteView, PaletteValue> {

    /**
     * draw palette
     *
     * 绘制调色板
     *
     * @param view PaletteView
     * @param canvas      canvas to draw
     * @param color       current color
     * @param isChanging  whether PaletteView is changing
     */
    override fun onDraw(
            view: PaletteView,
            canvas: Canvas,
            color: Int,
            value: PaletteValue,
            isChanging: Boolean
    ) {
        onDrawPalette(
                view,
                canvas,
                isChanging
        )

        if (
                indicatorPainter?.drawIndicator(
                        view,
                        canvas,
                        color,
                        isChanging
                ) == true
        ) return

        onDrawIndicator(
                view,
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

    fun updateByValue(
            paletteView: PaletteView,
            paletteValue: PaletteValue
    )

    /**
     * indicator painter
     */
    var indicatorPainter: IndicatorPainter?

}