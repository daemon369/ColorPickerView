package me.daemon.colorpicker.painter

import me.daemon.colorpicker.view.BrightnessView

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

}