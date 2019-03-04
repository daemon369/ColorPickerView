package me.daemon.colorpicker.painter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import me.daemon.colorpicker.BrightnessView

/**
 * @author daemon
 * @since 2019-02-22 15:15
 */
class DefaultBrightnessPainter : BrightnessPainter {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val soldPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectorPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val borderPath = Path()
    private val soldPath = Path()
    private val selectorPath = Path()

    init {
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 0F
        borderPaint.color = Color.BLACK

        selectorPaint.color = Color.BLACK

        selectorPath.fillType = Path.FillType.WINDING
    }

    override fun onSizeChange(
            brightnessView: BrightnessView,
            w: Int,
            h: Int
    ) {

    }

    override fun onUpdate(
            brightnessView: BrightnessView,
            x: Float,
            y: Float): Float {
        return 0f
    }

    override fun updateByValue(brightnessView: BrightnessView, brightness: Float) {
    }

    override fun drawBrightness(
            brightnessView: BrightnessView,
            canvas: Canvas,
            brightness: Float,
            isChanging: Boolean
    ) {
    }

}