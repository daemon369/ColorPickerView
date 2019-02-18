package me.daemon.colorpicker.internal

/**
 * color factor
 *
 * @author daemon
 * @since 2019-02-18 10:55
 */
internal enum class Factor {
    HUE,
    SATURATION,
    BRIGHTNESS,
    ALPHA;

    var value = 0f

    fun value(value: Float): Factor {
        this.value = value
        return this
    }
}