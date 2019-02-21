package me.daemon.colorpicker

/**
 * @author daemon
 * @since 2019-02-22 00:00
 */
enum class Gravity(val value: Int) {
    UNKNOWN(0),

    // basic
    LEFT(1),
    TOP(LEFT.value shl 1),
    RIGHT(TOP.value shl 1),
    BOTTOM(RIGHT.value shl 1),
    CENTER_HORIZONTAL(BOTTOM.value shl 1),
    CENTER_VERTICAL(CENTER_HORIZONTAL.value shl 1),

    // combination
    LEFT_TOP(LEFT.value or TOP.value),
    LEFT_CENTER(LEFT.value or CENTER_VERTICAL.value),
    LEFT_BOTTOM(LEFT.value or BOTTOM.value),
    CENTER_TOP(CENTER_HORIZONTAL.value or TOP.value),
    CENTER(CENTER_HORIZONTAL.value or CENTER_VERTICAL.value),
    CENTER_BOTTOM(CENTER_HORIZONTAL.value or BOTTOM.value),
    RIGHT_TOP(RIGHT.value or TOP.value),
    RIGHT_CENTER(RIGHT.value or CENTER_VERTICAL.value),
    RIGHT_BOTTOM(RIGHT.value or BOTTOM.value);

    companion object {
        private val map = HashMap<Int, Gravity>()

        init {
            for (gravity in values()) {
                map[gravity.value] = gravity
            }
        }

        fun from(gravity: Int): Gravity {
            return map[gravity] ?: UNKNOWN
        }
    }

}