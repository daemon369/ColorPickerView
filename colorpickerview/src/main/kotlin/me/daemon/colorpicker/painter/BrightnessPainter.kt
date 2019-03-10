package me.daemon.colorpicker.painter

import android.graphics.Canvas
import me.daemon.colorpicker.BrightnessView

/**
 * @author daemon
 * @since 2019-02-22 14:44
 */
interface BrightnessPainter : IPainter<BrightnessView, Float> {

    /**
     * update brightness value based on touch event coordinate
     *
     * @param brightnessView  BrightnessView
     * @param x               touch event x
     * @param y               touch event y
     */
    fun onUpdate(
            brightnessView: BrightnessView,
            x: Float,
            y: Float
    ): Float

    fun updateByValue(
            brightnessView: BrightnessView,
            brightness: Float
    )

    /**
     * @param brightnessView BrightnessView
     * @param canvas         canvas
     * @param brightness     brightness
     * @param isChanging     whether BrightnessView is changing
     */
    fun drawBrightness(
            brightnessView: BrightnessView,
            canvas: Canvas,
            brightness: Float,
            isChanging: Boolean
    )

}