package me.daemon.colorpicker.painter

import android.graphics.*
import me.daemon.colorpicker.BrightnessView

/**
 * @author daemon
 * @since 2019-02-22 15:15
 */
class DefaultBrightnessPainter : BrightnessPainter {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val solidPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectorPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val borderPath = Path()
    private val soldPath = Path()
    private val selectorPath = Path()

    private var currentValue = 1f

    private var selectorSize = 0f

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
        val hsv = FloatArray(3).apply {
            Color.colorToHSV(Color.BLACK, this)
        }

        val startColor = Color.HSVToColor(hsv.apply { this[2] = 0f })
        val endColor = Color.HSVToColor(hsv.apply { this[2] = 1f })

        val shader = LinearGradient(
                0f,
                0f,
                brightnessView.width.toFloat(),
                brightnessView.height.toFloat(),
                startColor,
                endColor,
                Shader.TileMode.CLAMP
        )

        solidPaint.shader = shader

        selectorSize = h * 0.25f

        selectorPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(selectorSize * 2, 0f)
            lineTo(selectorSize, selectorSize)
            close()
        }
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
        val width = brightnessView.width
        val height = brightnessView.height

        canvas.drawRect(selectorSize, selectorSize, width - selectorSize, height.toFloat(), solidPaint)
        canvas.drawRect(selectorSize, selectorSize, width - selectorSize, height.toFloat(), borderPaint)
    }

}