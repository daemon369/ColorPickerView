package me.daemon.colorpicker

/**
 * @author yc
 * @since 2019-03-30 01:06
 */
enum class Orientation {
    HORIZONTAL,
    VERTICAL,
    UNKNOWN;

    companion object {
        fun from(orientation: Int): Orientation {
            return when (orientation) {
                HORIZONTAL.ordinal -> HORIZONTAL
                VERTICAL.ordinal -> VERTICAL
                else -> UNKNOWN
            }
        }
    }
}