package me.daemon.colorpicker

/**
 * @author daemon
 * @since 2019-03-30 01:06
 */
enum class Orientation {
    HORIZONTAL,
    VERTICAL;

    companion object {
        fun from(orientation: Int): Orientation {
            return when (orientation) {
                HORIZONTAL.ordinal -> HORIZONTAL
                VERTICAL.ordinal -> VERTICAL
                else -> throw IllegalArgumentException("unknown orientation: $orientation")
            }
        }
    }
}
