package me.daemon.colorpicker

/**
 * @author daemon
 * @since 2019-02-22 00:00
 */
enum class Gravity {
    UNKNOWN(0),

    // basic
    LEFT(1),
    TOP(LEFT.value shl 1),
    RIGHT(TOP.value shl 1),
    BOTTOM(RIGHT.value shl 1),
    CENTER_HORIZONTAL(BOTTOM.value shl 1),
    CENTER_VERTICAL(CENTER_HORIZONTAL.value shl 1),

    // combination
    LEFT_TOP(
            LEFT,
            TOP
    ),
    LEFT_CENTER(
            LEFT,
            CENTER_VERTICAL
    ),
    LEFT_BOTTOM(
            LEFT,
            BOTTOM
    ),
    CENTER_TOP(
            CENTER_HORIZONTAL,
            TOP
    ),
    CENTER(
            CENTER_HORIZONTAL,
            CENTER_VERTICAL
    ),
    CENTER_BOTTOM(
            CENTER_HORIZONTAL,
            BOTTOM
    ),
    RIGHT_TOP(
            RIGHT,
            TOP
    ),
    RIGHT_CENTER(
            RIGHT,
            CENTER_VERTICAL
    ),
    RIGHT_BOTTOM(
            RIGHT,
            BOTTOM
    );

    val value: Int

    constructor(value: Int) {
        this.value = value
    }

    constructor(horizontal: Gravity, vertical: Gravity) {
        this.value = horizontal.value or vertical.value
    }

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