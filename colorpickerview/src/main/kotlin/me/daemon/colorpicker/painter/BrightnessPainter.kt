package me.daemon.colorpicker.painter

import android.graphics.Canvas
import me.daemon.colorpicker.BrightnessView

/**
 * @author daemon
 * @since 2019-02-22 14:44
 */
interface BrightnessPainter {

    fun update(
            brightnessView: BrightnessView,
            x: Float,
            y: Float
    )

    fun drawBrightness(
            brightnessView: BrightnessView,
            canvas: Canvas,
            brightness: Float
    )

}