package me.daemon.colorpicker.painter

import me.daemon.colorpicker.view.BrightnessView

/**
 * @author daemon
 * @since 2019-02-22 14:44
 */
interface BrightnessPainter : IPainter<BrightnessView, BrightnessView.BrightnessValue> {

    fun updateByValue(
            brightnessView: BrightnessView,
            brightness: Float
    )

}