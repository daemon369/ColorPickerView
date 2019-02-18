package me.daemon.colorpicker.internal

/**
 * @author daemon
 * @since 2019-02-18 22:58
 */
internal interface Callback {

    fun callback(
            color: Int,
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    )

}